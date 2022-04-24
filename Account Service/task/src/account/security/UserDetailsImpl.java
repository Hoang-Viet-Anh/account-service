package account.security;

import account.database.log.Actions;
import account.database.log.Log;
import account.database.log.LogRepository;
import account.database.user.User;
import account.database.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private String username;
    private String password;
    private List<GrantedAuthority> roles;
    private boolean access;

    public UserDetailsImpl(User user) {
        username = user.getEmail();
        password = user.getPassword();
        access = user.isAccess();
        roles = new ArrayList<>();
        user.getRoles().forEach(a -> roles.add(new SimpleGrantedAuthority("ROLE_" + a.toString())));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return access;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
