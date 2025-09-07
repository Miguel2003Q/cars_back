package com.cars.cars.service;

import com.cars.cars.entity.User;
import com.cars.cars.exception.NotFoundException;
import com.cars.cars.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
    
    public Map<String, Object> register(User user) {
        // Verificar si el usuario ya existe
        if (userRepository.existsByUsername(user.getUsername())) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "El nombre de usuario ya existe");
            return error;
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "El correo electrónico ya existe");
            return error;
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String token = jwtService.generateToken(loadUserByUsername(user.getUsername()));
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Usuario registrado exitosamente");
        response.put("token", token);
        response.put("user", Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "email", user.getEmail()
        ));
        return response;
    }
    
    public Map<String, Object> login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Usuario no encontrado");
            return error;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Contraseña incorrecta");
            return error;
        }

        String token = jwtService.generateToken(loadUserByUsername(username));
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Inicio de sesión exitoso");
        response.put("token", token);
        response.put("user", Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "email", user.getEmail()
        ));
        return response;
    }
    
    public Map<String, Object> updateUser(String username, User updatedUser) {
        try {
            System.out.println("Actualizando usuario: " + username);
            System.out.println("Email recibido: " + updatedUser.getEmail());
            System.out.println("Contraseña recibida: " + (updatedUser.getPassword() != null ? "***" : "null"));
            
            User existingUser = userRepository.findByUsername(username)
                    .orElse(null);

            if (existingUser == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Usuario no encontrado");
                return error;
            }

            // Validar que al menos un campo venga para actualizar
            if ((updatedUser.getEmail() == null || updatedUser.getEmail().trim().isEmpty()) && 
                (updatedUser.getPassword() == null || updatedUser.getPassword().trim().isEmpty())) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Debe proporcionar al menos un campo (email o contraseña)");
                return error;
            }

            // Actualizar email si se proporciona
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().trim().isEmpty()) {
                // Verificar si el nuevo email ya existe en otro usuario
                if (!existingUser.getEmail().equals(updatedUser.getEmail()) && 
                    userRepository.existsByEmail(updatedUser.getEmail())) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("message", "El correo electrónico ya existe");
                    return error;
                }
                existingUser.setEmail(updatedUser.getEmail().trim());
            }

            // Actualizar contraseña si se proporciona
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
                System.out.println("Actualizando contraseña para usuario: " + username);
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword().trim()));
                System.out.println("Contraseña actualizada exitosamente");
            } else {
                System.out.println("No se proporcionó contraseña o está vacía");
            }
            
            userRepository.save(existingUser);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario actualizado exitosamente");
            response.put("user", Map.of(
                "id", existingUser.getId(),
                "username", existingUser.getUsername(),
                "email", existingUser.getEmail()
            ));
            return response;
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al actualizar usuario");
            return error;
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }
}
