package account.database.payrolls;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface PayrollRepository extends CrudRepository<Payroll, Long> {
    boolean existsByEmployee(String email);

    List<Payroll> findByEmployeeAndPeriod(String email, String period);
    List<Payroll> findByEmployee(String email);
    List<Payroll> findByEmployeeOrderByPeriodDesc(String email);
}
