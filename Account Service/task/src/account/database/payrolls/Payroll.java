package account.database.payrolls;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "payrolls", uniqueConstraints = {@UniqueConstraint(columnNames = {"employee", "period"})})
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @Column(name = "employee")
    private String employee;

    @NotEmpty
    @Column(name = "period")
    @Pattern(regexp = "^(01|02|03|04|05|06|07|08|09|10|11|12)-[0-9]{4}$")
    private String period;

    @NotNull
    @Min(value = 0, message = "The salary must be non-negative.")
    @Column(name = "salary")
    private long salary;

    public Payroll() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }


}
