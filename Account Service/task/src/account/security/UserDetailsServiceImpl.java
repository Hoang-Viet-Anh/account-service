package account.security;

import account.database.user.User;
import account.database.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        user = userRepo.findByEmailIgnoreCase(username).isEmpty() ? null :
                userRepo.findByEmailIgnoreCase(username).get(0);
        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        return new UserDetailsImpl(user);
    }
}