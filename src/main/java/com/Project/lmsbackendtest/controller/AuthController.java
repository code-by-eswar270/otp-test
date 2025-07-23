package com.Project.lmsbackendtest.controller;

import com.Project.lmsbackendtest.dto.LoginRequest;
import com.Project.lmsbackendtest.dto.OtpRequest;
import com.Project.lmsbackendtest.dto.SignupRequest;
import com.Project.lmsbackendtest.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody OtpRequest request) {
        return authService.verifyOtp(request);
    }

    @PostMapping("/resend-otp")
    public String resendOtp(@RequestParam String email) {
        return authService.resendOtp(email);
    }
}
