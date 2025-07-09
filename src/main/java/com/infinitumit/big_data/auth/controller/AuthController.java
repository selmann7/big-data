package com.infinitumit.big_data.auth.controller;

import com.infinitumit.big_data.auth.dto.LoginRequest;
import com.infinitumit.big_data.auth.dto.RegisterRequest;
import com.infinitumit.big_data.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Kayıt API'si
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("Kullanıcı başarıyla kaydedildi.");
    }

    // Giriş API'si
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        authService.login(request.getUsername(), request.getPassword(), response);
        return ResponseEntity.ok("Giriş başarılı.");
    }

    // Çıkış API'si
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok("Çıkış başarılı.");
    }
}
