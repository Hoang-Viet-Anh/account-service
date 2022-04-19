package account.controllers;

import account.database.user.User;
import account.database.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessServiceController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/empl/payment")
    ResponseEntity<String> getUserInfo(Authentication auth) {
        User user = userRepository.findByEmailIgnoreCase(auth.getName()).get(0);
        return new ResponseEntity<>(user.toJson(), HttpStatus.OK);
    }
}
