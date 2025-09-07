package com.cars.cars.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder) {
        try {
            // Validar tipo de archivo
            validateImageFile(file);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> options = ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image",
                "transformation", "f_auto,q_auto",
                "allowed_formats", new String[]{"jpg", "jpeg", "png", "gif", "webp", "avif"},
                "max_file_size", 10485760 // 10MB
            );
            
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(), options);
            String imageUrl = (String) result.get("secure_url");
            
            log.info("Imagen subida exitosamente: {} - Formato: {}", imageUrl, getFileExtension(file.getOriginalFilename()));
            return imageUrl;
            
        } catch (IOException e) {
            log.error("Error al subir imagen: {}", e.getMessage());
            throw new RuntimeException("Error al subir la imagen", e);
        }
    }
    
    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new RuntimeException("El nombre del archivo es inválido");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "webp", "avif"};
        
        boolean isValidFormat = false;
        for (String allowedExt : allowedExtensions) {
            if (extension.equals(allowedExt)) {
                isValidFormat = true;
                break;
            }
        }
        
        if (!isValidFormat) {
            throw new RuntimeException("Formato de archivo no soportado. Formatos permitidos: JPG, JPEG, PNG, GIF, WebP, AVIF");
        }
        
        // Validar tamaño del archivo (10MB máximo)
        if (file.getSize() > 10485760) {
            throw new RuntimeException("El archivo es demasiado grande. Tamaño máximo: 10MB");
        }
        
        // Validar tipo MIME
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("El archivo no es una imagen válida");
        }
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public String updateImage(String publicId, MultipartFile file, String folder) {
        try {
            // Primero eliminar la imagen anterior
            deleteImage(publicId);
            
            // Subir la nueva imagen
            return uploadImage(file, folder);
            
        } catch (Exception e) {
            log.error("Error al actualizar imagen: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar la imagen", e);
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            // Extraer el public_id de la URL
            String publicId = extractPublicIdFromUrl(imageUrl);
            
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                log.info("Imagen eliminada exitosamente: {}", publicId);
            }
            
        } catch (Exception e) {
            log.error("Error al eliminar imagen: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar la imagen", e);
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            // La URL de Cloudinary tiene el formato:
            // https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{public_id}.{format}
            String[] parts = imageUrl.split("/upload/");
            if (parts.length > 1) {
                String path = parts[1];
                // Remover la versión (v1234567890/) si existe
                if (path.startsWith("v")) {
                    path = path.substring(path.indexOf("/") + 1);
                }
                // Remover la extensión del archivo
                int lastDotIndex = path.lastIndexOf(".");
                if (lastDotIndex != -1) {
                    path = path.substring(0, lastDotIndex);
                }
                return path;
            }
        } catch (Exception e) {
            log.warn("No se pudo extraer public_id de la URL: {}", imageUrl);
        }
        return null;
    }

    public String generateImageUrl(String publicId, String transformation) {
        try {
            return cloudinary.url()
                .transformation(new com.cloudinary.Transformation<>().rawTransformation(transformation))
                .generate(publicId);
        } catch (Exception e) {
            log.error("Error al generar URL de imagen: {}", e.getMessage());
            throw new RuntimeException("Error al generar URL de imagen", e);
        }
    }
}
