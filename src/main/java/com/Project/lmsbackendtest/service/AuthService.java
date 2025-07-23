package com.Project.lmsbackendtest.service;

import com.Project.lmsbackendtest.dto.LoginRequest;
import com.Project.lmsbackendtest.dto.OtpRequest;
import com.Project.lmsbackendtest.dto.SignupRequest;
import com.Project.lmsbackendtest.model.Otp;
import com.Project.lmsbackendtest.model.User;
import com.Project.lmsbackendtest.repository.OtpRepository;
import com.Project.lmsbackendtest.repository.UserRepository;
import com.Project.lmsbackendtest.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailUtil emailUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already registered.";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        return "Signup successful.";
    }

    public String login(LoginRequest request) {
        Optional<User> optional = userRepository.findByEmail(request.getEmail());
        if (optional.isEmpty()) return "User not found";

        User user = optional.get();
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            return "Invalid credentials";
        }

        String otp = otpService.generateOtp();
        otpService.saveOtp(user.getEmail(), otp, "LOGIN_VERIFICATION");
        emailUtil.sendOtpEmail(user.getEmail(), otp);
        return "OTP sent to email.";
    }

    public String verifyOtp(OtpRequest request) {
        Optional<Otp> optionalOtp = otpRepository.findTopByEmailAndTypeOrderByCreatedAtDesc(request.getEmail(), "LOGIN_VERIFICATION");
        if (optionalOtp.isEmpty()) return "No OTP found";

        Otp otp = optionalOtp.get();
        if (otp.isUsed()) return "OTP already used";
        if (!otp.getOtpCode().equals(request.getOtp())) return "Invalid OTP";
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) return "OTP expired";

        otp.setUsed(true);
        otpRepository.save(otp);

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            user.setVerified(true);
            userRepository.save(user);
        });

        return "OTP verified successfully";
    }

    public String resendOtp(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) return "User not found";

        String otp = otpService.generateOtp();
        otpService.saveOtp(email, otp, "LOGIN_VERIFICATION");
        emailUtil.sendOtpEmail(email, otp);
        return "OTP resent successfully";
    }

	
}
