package account.controllers;

import account.database.user.Password;
import account.database.user.PasswordRepository;
import account.database.user.User;
import account.database.user.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private Gson gson;

    @PostMapping("/api/auth/signup")
    ResponseEntity<String> regUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errors.getFieldError().getDefaultMessage());
        } else if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        } else if (passwordRepository.existsByPassword(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(encoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return new ResponseEntity<>(user.toJson(), HttpStatus.OK);
    }

    @PostMapping("/api/auth/changepass")
    ResponseEntity<String> changePassword(Authentication auth,@Valid @RequestBody Password password, Errors errors) {
        User user = userRepository.findByEmailIgnoreCase(auth.getName()).get(0);
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errors.getFieldError().getDefaultMessage());
        } else if (passwordRepository.existsByPassword(password.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        } else if (encoder.matches(password.getPassword(),
                        user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }
        user.setPassword(encoder.encode(password.getPassword()));
        user = userRepository.save(user);
        JsonObject object = new JsonObject();
        object.addProperty("email", user.getEmail());
        object.addProperty("status", "The password has been updated successfully");
        return new ResponseEntity<>(gson.toJson(object), HttpStatus.OK);
    }
}
