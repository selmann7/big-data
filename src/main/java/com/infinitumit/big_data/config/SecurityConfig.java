package com.infinitumit.big_data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Uygulama için Spring Security yapılandırması.
 * Bu class, güvenlik kurallarını, oturum yönetimini ve parola şifrelemeyi tanımlar.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Güvenlik filtre zincirini yapılandırır.
     * Bu method, farklı HTTP istekleri için yetkilendirme (authentication) kurallarını tanımlar.
     *
     * @param http Yapılandırılacak HttpSecurity nesnesi.
     * @return Bir SecurityFilterChain instance'ı.
     * @throws Exception Yapılandırma sırasında bir hata oluşursa fırlatılır (throw).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Sprint 2 Intern C CSRF
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        // IF_REQUIRED
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                // Configure form login.
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .permitAll()
                )
        ;

        return http.build(); // Build and return the SecurityFilterChain
    }

    /**
     * Bir parola şifreleyici (encoder) bean'i tanımlar.
     * BCryptPasswordEncoder şifre hashleme.
     *
     * @return BCryptPasswordEncoder instance'ı.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
