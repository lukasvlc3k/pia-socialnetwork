package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.dto.response.general.BooleanResponse;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {
    private final UserService userService;


    @GetMapping("/email/{email}/available")
    public ResponseEntity<BooleanResponse> emailAvailable(@PathVariable(value="email") String email) {
        boolean emailInUse = userService.existsEmail(email);
        return new ResponseEntity<BooleanResponse>(new BooleanResponse(!emailInUse), HttpStatus.OK);
    }
}
