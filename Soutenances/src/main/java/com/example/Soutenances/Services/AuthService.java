package com.example.Soutenances.Services;

import com.example.Soutenances.DTO.LoginRequest;
import com.example.Soutenances.Entities.User;
import com.example.Soutenances.Repositories.UserRepository;
import com.example.Soutenances.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé !");
        }

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect !");
        }

        if (!user.getRole().name().equals(loginRequest.getRole())) {
            throw new RuntimeException("Le rôle ne correspond pas !");
        }

        // Générer un JWT avec l'email et le rôle
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}
