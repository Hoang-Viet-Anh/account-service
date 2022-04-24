package account.security;

import account.database.log.Actions;
import account.database.log.Log;
import account.database.log.LogRepository;
import account.database.user.User;
import account.database.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName().toLowerCase();
        User user = userRepository.findByEmailIgnoreCase(username);
        user.setAttempts(0);
        userRepository.save(user);
    }
}
