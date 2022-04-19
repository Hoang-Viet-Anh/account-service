package account;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    @PostMapping("api/auth/signup")
    ResponseEntity<String> regUser(@Valid @RequestBody User user) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return new ResponseEntity<>(gson.toJson(user.toJson()), HttpStatus.OK);
    }
}
