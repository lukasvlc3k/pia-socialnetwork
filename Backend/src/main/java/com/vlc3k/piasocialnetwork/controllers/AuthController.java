package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.dto.request.auth.LoginRequest;
import com.vlc3k.piasocialnetwork.dto.request.auth.SignupRequest;
import com.vlc3k.piasocialnetwork.dto.response.auth.LoginResponse;
import com.vlc3k.piasocialnetwork.dto.response.auth.SignupResponse;
import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.enums.ERole;
import com.vlc3k.piasocialnetwork.enums.ESignupState;
import com.vlc3k.piasocialnetwork.services.AuthService;
import com.vlc3k.piasocialnetwork.services.RoleService;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;

    private final AuthService authService;

    // RFC822 compliant regex for email parsing
    private final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private final double MINIMUM_PASSWORD_STRENGTH = 5;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        var loginResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Test if email valid
        if (!EMAIL_PATTERN.matcher(signUpRequest.getEmail()).matches()) {
            return ResponseEntity
                    .ok()
                    .body(SignupResponse.Error(ESignupState.INVALID_EMAIL));
        }

        // Test if email is unique
        if (userService.existsEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .ok()
                    .body(SignupResponse.Error(ESignupState.EMAIL_IN_USE));
        }

        if (authService.passwordStrength(signUpRequest.getPassword()) < MINIMUM_PASSWORD_STRENGTH) {
            return ResponseEntity
                    .ok()
                    .body(SignupResponse.Error(ESignupState.WEAK_PASSWORD));
        }

        Set<String> strRoles = Set.of("user");
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleService.tryGetByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleService.tryGetByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleService.tryGetByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        userService.registerUser(signUpRequest.getEmail(), signUpRequest.getName(), signUpRequest.getPassword(), roles);
        return ResponseEntity.ok(new SignupResponse(ESignupState.OK, "User registered successfully!"));
    }
}