package account.database.user.role;

public enum Opration {
    GRANT(1), REMOVE(0);

    private int type;

    Opration(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
