package com.example.Soutenances.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    // Mariem Tlatli : 3asslama maysam zidit il cors bach yaccepti il front w zidit il requete ali a3maliteha bach najouti fil base de données les soutenaces

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())) // ✅ Activer CORS
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**","/api/disponibilites/ajouter" , "/api/soutenance/saveAll").permitAll() // Routes publiques (ex: login, register)
                        .requestMatchers("/api/excel/upload", "/api/excel/soutenances/**").hasAuthority("ADMINISTRATEUR") // Seul ADMINISTRATEUR peut accéder
                        .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Pas de session
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //  Ajouter JwtFilter avant la validation de connexion

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
