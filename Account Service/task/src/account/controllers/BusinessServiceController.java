package account.controllers;

import account.database.log.LogRepository;
import account.database.payrolls.Payroll;
import account.database.payrolls.PayrollRepository;
import account.database.user.User;
import account.database.user.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.text.DateFormatSymbols;
import java.util.List;

@RestController
public class BusinessServiceController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private Gson gson;

    @PostMapping("/api/acct/payments")
    ResponseEntity<String> addPayments(@Valid @RequestBody List<Payroll> payrollList, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errors.getFieldError().getDefaultMessage());
        }
        payrollList.forEach(a -> {
            if (!userRepository.existsByEmailIgnoreCase(a.getEmployee())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee doesn't exists.");
            }
        });
        try {
            payrollRepository.saveAll(payrollList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The employee-period pair already exists.");
        }
        JsonObject object = new JsonObject();
        object.addProperty("status", "Added successfully!");
        return new ResponseEntity<>(gson.toJson(object), HttpStatus.OK);
    }

    @PutMapping("/api/acct/payments")
    ResponseEntity<String> updatePayment(@Valid @RequestBody Payroll payroll, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errors.getFieldError().getDefaultMessage());
        } else if (!userRepository.existsByEmailIgnoreCase(payroll.getEmployee())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee doesn't exists.");
        } else if (!payrollRepository.existsByEmployee(payroll.getEmployee())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee salary doesn't exists.");
        }
        Payroll user = payrollRepository.findByEmployeeAndPeriod(payroll.getEmployee(), payroll.getPeriod()).get(0);
        user.setSalary(payroll.getSalary());
        payrollRepository.save(user);
        JsonObject object = new JsonObject();
        object.addProperty("status", "Updated successfully!");
        return new ResponseEntity<>(gson.toJson(object), HttpStatus.OK);
    }

    @GetMapping("/api/empl/payment")
    ResponseEntity<String> getPaymentsByPeriod(Authentication auth, @RequestParam(required = false) String period) {
        List<Payroll> payrollList;
        if (period != null) {
            if (!period.matches("^(01|02|03|04|05|06|07|08|09|10|11|12)-[0-9]{4}$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Period has wrong format.");
            } else {
                payrollList = payrollRepository.findByEmployeeAndPeriod(auth.getName(), period);
            }
        } else {
            payrollList = payrollRepository.findByEmployeeOrderByPeriodDesc(auth.getName());
        }

        User user = userRepository.findByEmailIgnoreCase(auth.getName());
        JsonArray array = new JsonArray();
        if (payrollList != null) {
            for (Payroll data :
                    payrollList) {
                int month = Integer.parseInt(data.getPeriod().substring(0, 2));
                String monthName = new DateFormatSymbols().getMonths()[month - 1]
                        .concat(data.getPeriod().substring(2));
                String salary = String.format("%d dollar(s) %d cent(s)",
                        data.getSalary() / 100,
                        data.getSalary() % 100);
                JsonObject object = new JsonObject();
                object.addProperty("name", user.getName());
                object.addProperty("lastname", user.getLastname());
                object.addProperty("period", monthName);
                object.addProperty("salary", salary);
                array.add(object);
            }
        }
        if (array.size() == 1) {
            return new ResponseEntity<>(gson.toJson(array.get(0).getAsJsonObject()), HttpStatus.OK);
        }
        return new ResponseEntity<>(gson.toJson(array), HttpStatus.OK);
    }

    @GetMapping("/api/security/events")
    ResponseEntity<String> getLog() {
        JsonArray array = new JsonArray();
        logRepository.findAll().forEach(a -> array.add(a.toJsonObject()));
        return new ResponseEntity<>(gson.toJson(array), HttpStatus.OK);
    }
}
