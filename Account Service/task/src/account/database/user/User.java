package account.database.user;

import account.database.user.role.ListToStringConverter;
import account.database.user.role.Role;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotEmpty
    @Email
    @Pattern(regexp = ".+@acme.com$", message = "Email should ends with @acme.com")
    @Column(name = "email", nullable = false)
    private String email;

    @NotEmpty
    @Size(min = 12, message = "The password length must be at least 12 chars!")
    @Column(name = "password", nullable = false)
    private String password;

    @Convert(converter = ListToStringConverter.class)
    @Column(name = "role", nullable = false)
    private List<Role> roles = List.of(Role.USER);

    @Column(name = "access", nullable = false)
    private boolean access = true;

    @Column(name = "login_attempts")
    private int attempts = 0;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public int getAttempts() {
        return attempts;
    }

    public User setAttempts(int attempts) {
        this.attempts = attempts;
        return this;
    }

    public void increaseAttempt() {
        attempts++;
    }

    public void decreaseAttempts() {
        attempts--;
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        JsonObject object = new JsonObject();
        object.addProperty("id", id);
        object.addProperty("name", name);
        object.addProperty("lastname", lastname);
        object.addProperty("email", email);
        JsonArray list = new JsonArray();
        List<Role> roleList = new ArrayList<>(roles);
        roleList.sort(Comparator.comparing(Enum::toString));
        roleList.forEach(a -> list.add("ROLE_" + a.toString()));
        object.add("roles", list);
        return gson.toJson(object);
    }

    public JsonObject toJsonObject() {
        JsonObject object = new JsonObject();
        object.addProperty("id", id);
        object.addProperty("name", name);
        object.addProperty("lastname", lastname);
        object.addProperty("email", email);
        JsonArray list = new JsonArray();
        List<Role> roleList = new ArrayList<>(roles);
        roleList.sort(Comparator.comparing(Enum::toString));
        roleList.forEach(a -> list.add("ROLE_" + a.toString()));
        object.add("roles", list);
        return object;
    }
}
