package account.database.user.role;

public enum Role {
    ACCOUNTANT(2), ADMINISTRATOR(1), ANONYMOUS(3), USER(2), AUDITOR(2);

    private int group;

    Role(int group) {
        this.group = group;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
