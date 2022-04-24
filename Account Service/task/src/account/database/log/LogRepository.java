package account.database.log;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LogRepository extends CrudRepository<Log, Long> {
    List<Log> findFirst5BySubjectOrderByIdDesc(String subject);
    boolean existsBySubject(String subject);
    Log findFirstBySubjectOrderByIdDesc(String subject);
}
