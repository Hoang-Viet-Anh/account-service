package account.database.user.role;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class ChangeRole {
    @NotEmpty
    private String user;

    @NotEmpty
    @Pattern(regexp = "ANONYMOUS|USER|ACCOUNTANT|ADMINISTRATOR", message = "Role not found!")
    private String role;

    @NotEmpty
    @Pattern(regexp = "GRANT|REMOVE", message = "Invalid operation!")
    private String operation;

    private Opration oprationType;

    private Role roleType;

    public ChangeRole() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Opration getOprationType() {
        return oprationType;
    }

    public void setOprationType(Opration oprationType) {
        this.oprationType = oprationType;
    }

    public Role getRoleType() {
        return roleType;
    }

    public void setRoleType(Role roleType) {
        this.roleType = roleType;
    }
}
