package com.company.hrm.controller;

import com.company.hrm.dto.LoginRequestDTO;
import com.company.hrm.dto.UserRequestDTO;
import com.company.hrm.service.AuthService;
import com.company.hrm.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserService userService;

    private AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDTO dto) {

        userService.registerUser(dto);
        return ResponseEntity.ok("User Registered Successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO dto){

        String token = authService.login(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(token);

    }
}
