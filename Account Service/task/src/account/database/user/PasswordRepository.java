package account.database.user;


import org.springframework.data.repository.CrudRepository;

public interface PasswordRepository extends CrudRepository<Password, Long> {
    boolean existsByPassword(String password);
}
