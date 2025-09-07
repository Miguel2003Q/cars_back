package com.cars.cars.controller;

import com.cars.cars.dto.CarCreateDTO;
import com.cars.cars.dto.CarDTO;
import com.cars.cars.dto.CarUpdateDTO;
import com.cars.cars.entity.User;
import com.cars.cars.service.AuthService;
import com.cars.cars.service.CarService;
import com.cars.cars.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.security.Principal;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CarController {
    
    private final CarService carService;
    private final AuthService authService;
    private final CloudinaryService cloudinaryService;
    
    @GetMapping
    public List<CarDTO> getAllCars(Principal principal) {
        User user = authService.getUserByUsername(principal.getName());
        return carService.getAllCarsByUser(user);
    }
    
    // Endpoint de debug para verificar autos
    @GetMapping("/debug")
    public ResponseEntity<Map<String, Object>> debugCars(Principal principal) {
        try {
            User user = authService.getUserByUsername(principal.getName());
            List<CarDTO> cars = carService.getAllCarsByUser(user);
            
            return ResponseEntity.ok(Map.of(
                "user", user.getUsername(),
                "userId", user.getId(),
                "carsCount", cars.size(),
                "cars", cars
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        }
    }
    
    // Endpoint para probar formatos de imagen
    @PostMapping("/test-image-formats")
    public ResponseEntity<Map<String, Object>> testImageFormats(
            @RequestParam("file") MultipartFile file,
            Principal principal) {
        try {
            System.out.println("=== TEST IMAGE FORMATS ===");
            System.out.println("Original filename: " + file.getOriginalFilename());
            System.out.println("Content type: " + file.getContentType());
            System.out.println("File size: " + file.getSize() + " bytes");
            System.out.println("Is empty: " + file.isEmpty());
            
            // Probar subida a Cloudinary
            String imageUrl = cloudinaryService.uploadImage(file, "test-formats");
            
            return ResponseEntity.ok(Map.of(
                "message", "Formato de imagen válido",
                "originalFilename", file.getOriginalFilename(),
                "contentType", file.getContentType(),
                "fileSize", file.getSize(),
                "imageUrl", imageUrl,
                "supportedFormats", new String[]{"jpg", "jpeg", "png", "gif", "webp", "avif"}
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "originalFilename", file.getOriginalFilename(),
                "contentType", file.getContentType(),
                "fileSize", file.getSize()
            ));
        }
    }
    
    // Búsqueda por placa
    @GetMapping("/search")
    public List<CarDTO> searchCars(@RequestParam String q, Principal principal) {
        User user = authService.getUserByUsername(principal.getName());
        return carService.searchCars(user, q);
    }
    
    // Filtrado por año
    @GetMapping("/by-year/{year}")
    public List<CarDTO> filterCarsByYear(@PathVariable Integer year, Principal principal) {
        User user = authService.getUserByUsername(principal.getName());
        return carService.filterCarsByYear(user, year);
    }
    
    // Filtrado por marca
    @GetMapping("/by-brand/{brand}")
    public List<CarDTO> filterCarsByBrand(@PathVariable String brand, Principal principal) {
        User user = authService.getUserByUsername(principal.getName());
        return carService.filterCarsByBrand(user, brand);
    }
    
    @GetMapping("/{id}")
    public CarDTO getCarById(@PathVariable Long id, Principal principal) {
        User user = authService.getUserByUsername(principal.getName());
        return carService.getCarByIdAndUser(id, user);
    }
    
    @PostMapping
    public CarDTO createCar(@RequestBody CarCreateDTO carCreateDTO, Principal principal) {
        User user = authService.getUserByUsername(principal.getName());
        return carService.createCar(carCreateDTO, user);
    }
    
    @PutMapping("/{id}")
    public CarDTO updateCar(@PathVariable Long id, @RequestBody CarUpdateDTO carUpdateDTO, Principal principal) {
        User user = authService.getUserByUsername(principal.getName());
        return carService.updateCar(id, carUpdateDTO, user);
    }
    
    @DeleteMapping("/{id}")
    public String deleteCar(@PathVariable Long id, Principal principal) {
        User user = authService.getUserByUsername(principal.getName());
        carService.deleteCar(id, user);
        return "Auto eliminado exitosamente";
    }
    
    // Endpoints para manejo de imágenes con Cloudinary
    
    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<Map<String, String>> uploadCarPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Principal principal) {
        try {
            System.out.println("=== DEBUG UPLOAD PHOTO ===");
            System.out.println("Car ID: " + id);
            System.out.println("Principal: " + (principal != null ? principal.getName() : "null"));
            
            User user = authService.getUserByUsername(principal.getName());
            System.out.println("User found: " + (user != null ? user.getUsername() : "null"));
            
            CarDTO carDTO = carService.getCarByIdAndUser(id, user);
            System.out.println("Car found: " + (carDTO != null ? carDTO.getId() : "null"));
            
            // Subir imagen a Cloudinary
            String imageUrl = cloudinaryService.uploadImage(file, "cars");
            
            // Actualizar el auto con la nueva URL de imagen
            CarUpdateDTO updateDTO = new CarUpdateDTO();
            updateDTO.setBrand(carDTO.getBrand());
            updateDTO.setModel(carDTO.getModel());
            updateDTO.setYear(carDTO.getYear());
            updateDTO.setLicensePlate(carDTO.getLicensePlate());
            updateDTO.setColor(carDTO.getColor());
            updateDTO.setPhotoUrl(imageUrl);
            
            carService.updateCar(id, updateDTO, user);
            
            return ResponseEntity.ok(Map.of(
                "message", "Foto subida exitosamente",
                "imageUrl", imageUrl
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al subir la foto: " + e.getMessage()
            ));
        }
    }
    
    @PutMapping("/{id}/update-photo")
    public ResponseEntity<Map<String, String>> updateCarPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Principal principal) {
        try {
            User user = authService.getUserByUsername(principal.getName());
            CarDTO carDTO = carService.getCarByIdAndUser(id, user);
            
            // Si ya tiene una imagen, eliminarla de Cloudinary
            if (carDTO.getPhotoUrl() != null && !carDTO.getPhotoUrl().isEmpty()) {
                cloudinaryService.deleteImage(carDTO.getPhotoUrl());
            }
            
            // Subir nueva imagen
            String imageUrl = cloudinaryService.uploadImage(file, "cars");
            
            // Actualizar el auto con la nueva URL
            CarUpdateDTO updateDTO = new CarUpdateDTO();
            updateDTO.setBrand(carDTO.getBrand());
            updateDTO.setModel(carDTO.getModel());
            updateDTO.setYear(carDTO.getYear());
            updateDTO.setLicensePlate(carDTO.getLicensePlate());
            updateDTO.setColor(carDTO.getColor());
            updateDTO.setPhotoUrl(imageUrl);
            
            carService.updateCar(id, updateDTO, user);
            
            return ResponseEntity.ok(Map.of(
                "message", "Foto actualizada exitosamente",
                "imageUrl", imageUrl
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al actualizar la foto: " + e.getMessage()
            ));
        }
    }
    
    @DeleteMapping("/{id}/delete-photo")
    public ResponseEntity<Map<String, String>> deleteCarPhoto(
            @PathVariable Long id,
            Principal principal) {
        try {
            User user = authService.getUserByUsername(principal.getName());
            CarDTO carDTO = carService.getCarByIdAndUser(id, user);
            
            if (carDTO.getPhotoUrl() != null && !carDTO.getPhotoUrl().isEmpty()) {
                // Eliminar imagen de Cloudinary
                cloudinaryService.deleteImage(carDTO.getPhotoUrl());
                
                // Limpiar URL en la base de datos
                CarUpdateDTO updateDTO = new CarUpdateDTO();
                updateDTO.setBrand(carDTO.getBrand());
                updateDTO.setModel(carDTO.getModel());
                updateDTO.setYear(carDTO.getYear());
                updateDTO.setLicensePlate(carDTO.getLicensePlate());
                updateDTO.setColor(carDTO.getColor());
                updateDTO.setPhotoUrl(null);
                
                carService.updateCar(id, updateDTO, user);
                
                return ResponseEntity.ok(Map.of(
                    "message", "Foto eliminada exitosamente"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "El auto no tiene foto para eliminar"
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al eliminar la foto: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/{id}/photo")
    public ResponseEntity<Map<String, String>> getCarPhoto(@PathVariable Long id, Principal principal) {
        try {
            User user = authService.getUserByUsername(principal.getName());
            CarDTO carDTO = carService.getCarByIdAndUser(id, user);
            
            if (carDTO.getPhotoUrl() != null && !carDTO.getPhotoUrl().isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "imageUrl", carDTO.getPhotoUrl()
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "message", "El auto no tiene foto"
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener la foto: " + e.getMessage()
            ));
        }
    }
}
