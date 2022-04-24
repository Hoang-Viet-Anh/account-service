package account.security;

import account.database.log.Actions;
import account.database.log.Log;
import account.database.log.LogRepository;
import account.database.user.User;
import account.database.user.UserRepository;
import account.database.user.role.Role;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class AuthFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Autowired
    LogRepository logRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebRequest request;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        String path = request.getDescription(false).substring(4);
        logRepository.save(new Log.Builder()
                .setDate(new Date())
                .setAction(Actions.LOGIN_FAILED)
                .setSubject(username)
                .setObject(path)
                .setPath(path)
                .build());
        username = username.contains("anonymous") ? "Anonymous" : username;
        if (userRepository.existsByEmailIgnoreCase(username.toLowerCase())
        && !userRepository.findByEmailIgnoreCase(username).getRoles().contains(Role.ADMINISTRATOR)) {
            User user = userRepository.findByEmailIgnoreCase(username);
            user.increaseAttempt();
            if (user.getAttempts() == 5) {
                logRepository.save(new Log.Builder()
                        .setDate(new Date())
                        .setAction(Actions.BRUTE_FORCE)
                        .setSubject(username)
                        .setObject(path)
                        .setPath(path)
                        .build());

                user.setAccess(false);
                logRepository.save(new Log.Builder()
                        .setDate(new Date())
                        .setAction(Actions.LOCK_USER)
                        .setSubject(username)
                        .setObject(String.format("Lock user %s", username))
                        .setPath(path)
                        .build());
            }
            userRepository.save(user);
        }
    }
}