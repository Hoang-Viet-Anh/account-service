package account.controllers;

import account.database.log.Actions;
import account.database.log.Log;
import account.database.log.LogRepository;
import account.database.user.access.ActionType;
import account.database.user.access.UserAccess;
import account.database.user.role.ChangeRole;
import account.database.user.role.Opration;
import account.database.user.role.Role;
import account.database.user.User;
import account.database.user.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
public class AdministrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private Gson gson;

    @PutMapping("/api/admin/user/role")
    ResponseEntity<String> setUserRole(Authentication auth, @Valid @RequestBody ChangeRole changes, Errors errors) {
        if (errors.hasErrors()) {
            if (errors.getFieldError().getDefaultMessage().contains("Role")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        errors.getFieldError().getDefaultMessage());
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errors.getFieldError().getDefaultMessage());

        }
        changes.setOprationType(Opration.valueOf(changes.getOperation()));
        changes.setRoleType(Role.valueOf(changes.getRole()));
        User user = userRepository.findByEmailIgnoreCase(changes.getUser());

        if (!userRepository.existsByEmailIgnoreCase(changes.getUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");

        } else if (changes.getRoleType().equals(Role.ADMINISTRATOR) &&
                changes.getOprationType().equals(Opration.REMOVE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");

        } else if (!user.getRoles()
            .contains(changes.getRoleType()) && changes.getOprationType().equals(Opration.REMOVE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");

        } else if (changes.getOprationType().equals(Opration.REMOVE) && user.getRoles().size() == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");

        } else if (!user.getRoles()
                .stream()
                .allMatch(a -> a.getGroup() == changes.getRoleType().getGroup())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The user cannot combine administrative and business roles!");

        } else {
            List<Role> roleList = user.getRoles();
            if (changes.getOprationType().equals(Opration.GRANT)) {
                roleList.add(changes.getRoleType());
            } else {
                roleList.remove(changes.getRoleType());
            }
            user.setRoles(roleList);
            user = userRepository.save(user);
            String operationObject = changes.getOperation()
                    .charAt(0) +
                    changes.getOperation()
                            .substring(1)
                            .toLowerCase();
            logRepository.save(new Log.Builder()
                    .setDate(new Date())
                    .setAction(Actions.valueOf(changes.getOperation().toString() + "_ROLE"))
                    .setSubject(auth.getName())
                    .setObject(String.format("%s role %s %s %s",
                            operationObject, changes.getRole(),
                            changes.getOprationType().equals(Opration.REMOVE) ? "from" : "to",
                            changes.getUser().toLowerCase()))
                    .setPath("/api/admin/user/role")
                    .build());
            return new ResponseEntity<>(user.toJson(), HttpStatus.OK);
        }
    }

    @GetMapping("/api/admin/user")
    ResponseEntity<String> getUsers() {
        JsonArray array = new JsonArray();
        Iterable<User> users = userRepository.findAll();
        for (User user:
             users) {
            array.add(user.toJsonObject());
        }
        return new ResponseEntity<>(gson.toJson(array), HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/user/{email}")
    ResponseEntity<String> deleteUser(Authentication auth, @PathVariable String email)   {
        if (!userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        } else if (userRepository.findByEmailIgnoreCase(email)
                .getRoles()
                .contains(Role.ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        } else {
            userRepository.deleteByEmail(email);
            JsonObject response = new JsonObject();
            response.addProperty("user", email);
            response.addProperty("status", "Deleted successfully!");
            logRepository.save(new Log.Builder()
                    .setDate(new Date())
                    .setAction(Actions.DELETE_USER)
                    .setSubject(auth.getName())
                    .setObject(email.toLowerCase())
                    .setPath("/api/admin/user")
                    .build());
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
        }
    }

    @PutMapping("/api/admin/user/access")
    ResponseEntity<String> setAccess(Authentication auth, @Valid @RequestBody UserAccess access, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errors.getFieldError().getDefaultMessage());
        } else if (!userRepository.existsByEmailIgnoreCase(access.getUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        } else if (userRepository.findByEmailIgnoreCase(access.getUser())
                .getRoles().contains(Role.ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
        } else {
            access.setActionType(ActionType.valueOf(access.getOperation()));
            User user = userRepository.findByEmailIgnoreCase(access.getUser());
            if (access.getActionType().equals(ActionType.UNLOCK)) {
                user.setAccess(true);
            } else if (access.getActionType().equals(ActionType.LOCK)) {
                user.setAccess(false);
            }
            user = userRepository.save(user);
            JsonObject object = new JsonObject();
            object.addProperty("status",
                    String.format("User %s %sed!", access.getUser().toLowerCase(),
                            access.getActionType().toString().toLowerCase()));
            String action = access.getActionType().toString();
            logRepository.save(new Log.Builder()
                    .setDate(new Date())
                    .setAction(Actions.valueOf(access.getActionType().toString() + "_USER"))
                    .setSubject(auth.getName())
                    .setObject(String.format("%s user %s",
                            action.charAt(0) + action.substring(1).toLowerCase(),
                            access.getUser().toLowerCase()))
                    .setPath("/api/admin/user/access")
                    .build());
            return new ResponseEntity<>(gson.toJson(object), HttpStatus.OK);
        }
    }
}
