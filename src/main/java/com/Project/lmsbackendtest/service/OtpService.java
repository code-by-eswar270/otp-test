package com.Project.lmsbackendtest.service;

import com.Project.lmsbackendtest.model.Otp;
import com.Project.lmsbackendtest.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void saveOtp(String email, String code, String type) {
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtpCode(code);
        otp.setType(type);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpRepository.save(otp);
    }
}
