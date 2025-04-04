package com.example.Soutenances.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableMethodSecurity // Permet d'utiliser @PreAuthorize
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.setAllowCredentials(true);
                    corsConfig.addAllowedOriginPattern("http://localhost:*"); // Accepte tous les localhost avec n'importe quel port
                    corsConfig.addAllowedHeader("*");
                    corsConfig.addAllowedMethod("*"); // Autorise toutes les méthodes HTTP
                    return corsConfig;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**","/api/disponibilites/ajouter" , "/api/soutenance/saveAll").permitAll() // Routes publiques (ex: login, register)
                        .requestMatchers("/api/salles/**", "/api/soutenances/**","/api/departement/**").hasAuthority("ADMINISTRATEUR") // Seul ADMINISTRATEUR peut accéder
                        .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
