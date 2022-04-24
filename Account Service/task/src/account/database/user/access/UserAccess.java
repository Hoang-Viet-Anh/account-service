package account.database.user.access;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserAccess {
    @NotEmpty(message = "Username mustn't be empty")
    private String user;

    @NotEmpty
    @Pattern(regexp = "LOCK|UNLOCK", message ="Wrong operation.")
    private String operation;

    private ActionType actionType;

    public UserAccess() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getOperation() {
        return operation;
    }

    public UserAccess setOperation(String operation) {
        this.operation = operation;
        return this;
    }
}
