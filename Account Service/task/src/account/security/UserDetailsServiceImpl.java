package account.security;

import account.database.log.Actions;
import account.database.log.Log;
import account.database.log.LogRepository;
import account.database.user.User;
import account.database.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    LogRepository logRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        user = userRepo.existsByEmailIgnoreCase(username) ?
                userRepo.findByEmailIgnoreCase(username) : null;
        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
//        if (logRepository.existsBySubject(user.getEmail())) {
//            if (logRepository.findFirstBySubjectOrderByIdDesc(user.getEmail())
//                    .getAction().equals(Actions.LOCK_USER)) {
//                user.setAccess(true);
//            }
//        }
        return new UserDetailsImpl(user);
    }
}