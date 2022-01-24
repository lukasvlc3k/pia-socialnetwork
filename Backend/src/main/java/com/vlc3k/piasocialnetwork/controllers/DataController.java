package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.entities.User;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataController {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class MeResponse {
        boolean ok;
        String user;
        String msg;
        User usr;
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> meResponse() {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<MeResponse>(new MeResponse(true, userDetails.getEmail(), "me", userDetails), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/me/admin")
    public ResponseEntity<MeResponse> meResponseAdmin() {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(new MeResponse(true, userDetails.getEmail(), "admin", userDetails));
    }
}
