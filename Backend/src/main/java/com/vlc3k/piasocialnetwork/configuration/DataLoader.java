package com.vlc3k.piasocialnetwork.configuration;

import com.vlc3k.piasocialnetwork.configuration.security.AuthEntryPointJwt;
import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.enums.ERole;
import com.vlc3k.piasocialnetwork.services.RoleService;
import com.vlc3k.piasocialnetwork.services.UserService;
import com.vlc3k.piasocialnetwork.utils.utils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final UserService userService;
    private final RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    private void checkRoles() {
        roleService.getAllRoles();

        var roleNameList = roleService.getAllRoles().stream().map(Role::getName).toList();
        for (var nameRole : ERole.values()) {
            if (!roleNameList.contains(nameRole)) {
                roleService.addRole(nameRole);
            }
        }
    }

    private void checkAdmin() throws Exception {
        var usersInAdminRole = userService.findUsersInRole(ERole.ROLE_ADMIN);

        if (usersInAdminRole.isEmpty()) {
            addAdmin();
        }
    }

    private void addAdmin() throws Exception {
        String username = "admin";
        String domain = "pia.cz";

        boolean first = true;
        boolean found = false;

        var role = roleService.getByName(ERole.ROLE_ADMIN);
        if (role.isEmpty()) {
            throw new Exception("Admin role not found");
        }

        int i = 0;
        while (!found) {
            if (first) {
                first = false;
            } else {
                i++;
                username = "admin." + i;
            }

            String email = username + "@" + domain;
            String password = utils.generateRandomPassword(20);

            if (userService.getByEmail(email).isEmpty()) {
                found = true;
                userService.registerUser(email, "Admin", password, List.of(role.get()));

                logger.info("Admin user not found, adding a new one");
                logger.info("\n\nNew admin credentials:\nemail: " + email+"\npassword: " + password + "\n\n");
            }
        }
    }

    private void makeAllOffline() {
        // mark all users as offline on application start
        var users = userService.getByOnline(true);

        for (var user : users) {
            userService.changeIsOnline(user, false);
        }
    }

    public void run(ApplicationArguments args) throws Exception {
        checkRoles();
        checkAdmin();
        makeAllOffline();
    }
}