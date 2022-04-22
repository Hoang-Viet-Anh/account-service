package account;

import account.database.password.Password;
import account.database.password.PasswordRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Autowired
    PasswordRepository passwordRepository;

    @Bean
    public Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    public void addHackedPasswords() {
        List<String> passwords = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
        List<Password> objects = new ArrayList<>();
        passwords.forEach(a -> objects.add(new Password(a)));
        objects.forEach(a -> {
            try {
                if (!passwordRepository.existsByPassword(a.getPassword())) {
                    passwordRepository.save(a);
                }
            } catch (Exception ignore) {}
        });
    }
}
