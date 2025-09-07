package com.cars.cars.controller;

import com.cars.cars.entity.User;
import com.cars.cars.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        return authService.register(user);
    }
    
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        return authService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = authService.getUserByUsername(username);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        return response;
    }

    @PutMapping("/update")
    public Map<String, Object> updateUser(Authentication authentication, @RequestBody User user) {
        String username = authentication.getName();
        return authService.updateUser(username, user);
    }
}
