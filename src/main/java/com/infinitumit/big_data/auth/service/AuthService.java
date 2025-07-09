package com.infinitumit.big_data.auth.service;

import com.infinitumit.big_data.entity.User;
import com.infinitumit.big_data.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Bu kullanıcı adı zaten alınmış.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);
    }

    public void login(String username, String password, HttpServletResponse response) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı veya şifre yanlış!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Kullanıcı bulunamadı veya şifre yanlış!");
        }

        // Gerçek session cookie ayarı yapılmadığı için burada statik bir değer var
        jakarta.servlet.http.Cookie authCookie = new jakarta.servlet.http.Cookie("JSESSIONID", "dummy-session-id");
        authCookie.setHttpOnly(true);
        authCookie.setPath("/");
        authCookie.setMaxAge(7 * 24 * 60 * 60); // 1 hafta
        response.addCookie(authCookie);
    }

    public void logout(HttpServletResponse response) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);

        if (session != null) {
            session.invalidate();
        }

        jakarta.servlet.http.Cookie authCookie = new jakarta.servlet.http.Cookie("JSESSIONID", null);
        authCookie.setMaxAge(0);
        authCookie.setPath("/");
        authCookie.setHttpOnly(true);
        response.addCookie(authCookie);
    }
}
