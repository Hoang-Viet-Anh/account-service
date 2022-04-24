package account.security;

import account.database.log.Actions;
import account.database.log.Log;
import account.database.log.LogRepository;
import account.database.user.role.Role;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    LogRepository logRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // manage access
                .mvcMatchers(HttpMethod.GET, "/api/empl/payment")
                    .hasAnyRole(Role.USER.toString(), Role.ACCOUNTANT.toString())
                .mvcMatchers("/api/acct/payments").hasRole(Role.ACCOUNTANT.toString())
                .mvcMatchers("/api/admin/**").hasRole(Role.ADMINISTRATOR.toString())
                .mvcMatchers(HttpMethod.GET, "/api/security/events").hasRole(Role.AUDITOR.toString())
                .mvcMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .mvcMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint()) // Handle auth error
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return ((request, response, accessDeniedException) -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            String path = request.getServletPath();
            logRepository.save(new Log.Builder()
                    .setDate(new Date())
                    .setAction(Actions.ACCESS_DENIED)
                    .setSubject(username)
                    .setObject(path)
                    .setPath(path)
                    .build());

            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
        });
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            String message = authException.getMessage();
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("timestamp", new Date().toString());
            jsonObject.addProperty("status", response.getStatus());
            jsonObject.addProperty("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            jsonObject.addProperty("message", message);
            jsonObject.addProperty("path", request.getServletPath());
            response.getWriter().write(jsonObject.toString());
        };
    }
}
