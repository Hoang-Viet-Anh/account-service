package account.database.log;

import com.google.gson.JsonObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private Actions action;

    @Column(name = "subject")
    private String subject;

    @Column(name = "object")
    private String object;

    @Column(name = "path")
    private String path;

    public Log() {
    }

    private Log(Date date, Actions action, String subject, String object, String path) {
        this.date = date;
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Actions getAction() {
        return action;
    }

    public void setAction(Actions action) {
        this.action = action;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JsonObject toJsonObject() {
        JsonObject object = new JsonObject();
        object.addProperty("date", date.toString());
        object.addProperty("action", action.toString());
        object.addProperty("subject", subject);
        object.addProperty("object", this.object);
        object.addProperty("path", path);
        return object;
    }

    public static class Builder {
        private Date date;
        private Actions action;
        private String subject;
        private String object;
        private String path;

        public Builder() {
        }

        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder setAction(Actions action) {
            this.action = action;
            return this;
        }

        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder setObject(String object) {
            this.object = object;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Log build() {
            return new Log(date, action, subject, object, path);
        }

    }
}

