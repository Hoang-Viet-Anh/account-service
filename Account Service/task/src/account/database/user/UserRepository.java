package account.database.user;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByEmailIgnoreCase(String email);

    @Transactional
    void deleteByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);
}
