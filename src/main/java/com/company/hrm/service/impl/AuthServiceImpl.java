package com.company.hrm.service.impl;

import com.company.hrm.entity.User;
import com.company.hrm.exception.BadRequestException;
import com.company.hrm.repository.UserRepo;
import com.company.hrm.security.JwtUtil;
import com.company.hrm.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String email, String password) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid Email"));

        if (! passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Invalid Password");
        }

        return JwtUtil.generateToken(email, user.getRole().name());
    }
}
