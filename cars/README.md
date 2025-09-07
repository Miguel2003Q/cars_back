# 🚗 Cars API - Backend Documentation

## 📋 Descripción General

API REST para gestión de autos con autenticación JWT, integración con Cloudinary para manejo de imágenes, y base de datos MySQL.

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **MySQL**
- **Cloudinary** (Gestión de imágenes)
- **Maven**

## 🚀 Configuración

### Variables de Entorno
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/cars_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=1234

# JWT Configuration
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400000

# Cloudinary Configuration
cloudinary.cloud_name=di5zocq4s
cloudinary.api_key=886599331438313
cloudinary.api_secret=V9IW_wBDHwFx9woEn916cwq7jZI

# Server Configuration
server.port=8080
```

### Ejecutar la Aplicación
```bash
.\mvnw.cmd spring-boot:run
```

## 🔐 Autenticación

Todas las rutas protegidas requieren el header:
```
Authorization: Bearer {JWT_TOKEN}
```

---

## 📚 API Endpoints

### 🔑 **Autenticación**

#### **Registrar Usuario**
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "string",
  "password": "string",
  "email": "string"
}
```

**Respuesta exitosa:**
```json
{
  "message": "Usuario registrado exitosamente"
}
```

#### **Login**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}
```

**Respuesta exitosa:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer"
}
```

---

### 🚗 **Gestión de Autos**

#### **Obtener Todos los Autos del Usuario**
```http
GET /api/cars
Authorization: Bearer {token}
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2023,
    "licensePlate": "ABC123",
    "color": "Blanco",
    "photoUrl": "https://res.cloudinary.com/...",
    "user": {
      "id": 1,
      "username": "testuser"
    }
  }
]
```

#### **Obtener Auto por ID**
```http
GET /api/cars/{id}
Authorization: Bearer {token}
```

**Respuesta:**
```json
{
  "id": 1,
  "brand": "Toyota",
  "model": "Corolla",
  "year": 2023,
  "licensePlate": "ABC123",
  "color": "Blanco",
  "photoUrl": "https://res.cloudinary.com/...",
  "user": {
    "id": 1,
    "username": "testuser"
  }
}
```

#### **Crear Auto**
```http
POST /api/cars
Authorization: Bearer {token}
Content-Type: application/json

{
  "brand": "string",
  "model": "string",
  "year": 2023,
  "licensePlate": "string",
  "color": "string"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "brand": "Toyota",
  "model": "Corolla",
  "year": 2023,
  "licensePlate": "ABC123",
  "color": "Blanco",
  "photoUrl": null,
  "user": {
    "id": 1,
    "username": "testuser"
  }
}
```

#### **Actualizar Auto**
```http
PUT /api/cars/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "brand": "string",
  "model": "string",
  "year": 2023,
  "licensePlate": "string",
  "color": "string"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "brand": "Toyota",
  "model": "Camry",
  "year": 2024,
  "licensePlate": "XYZ789",
  "color": "Azul",
  "photoUrl": "https://res.cloudinary.com/...",
  "user": {
    "id": 1,
    "username": "testuser"
  }
}
```

#### **Eliminar Auto**
```http
DELETE /api/cars/{id}
Authorization: Bearer {token}
```

**Respuesta:**
```json
"Auto eliminado exitosamente"
```

---

### 🔍 **Búsqueda y Filtros**

#### **Buscar Autos por Placa o Modelo**
```http
GET /api/cars/search?q={searchTerm}
Authorization: Bearer {token}
```

**Ejemplo:**
```http
GET /api/cars/search?q=Corolla
Authorization: Bearer {token}
```

#### **Filtrar por Año**
```http
GET /api/cars/by-year/{year}
Authorization: Bearer {token}
```

**Ejemplo:**
```http
GET /api/cars/by-year/2023
Authorization: Bearer {token}
```

#### **Filtrar por Marca**
```http
GET /api/cars/by-brand/{brand}
Authorization: Bearer {token}
```

**Ejemplo:**
```http
GET /api/cars/by-brand/Toyota
Authorization: Bearer {token}
```

---

### 📸 **Gestión de Imágenes (Cloudinary)**

#### **Subir Foto de Auto**
```http
POST /api/cars/{id}/upload-photo
Authorization: Bearer {token}
Content-Type: multipart/form-data

Body (form-data):
- Key: file
- Type: File
- Value: [archivo de imagen]
```

**Respuesta exitosa:**
```json
{
  "message": "Foto subida exitosamente",
  "imageUrl": "https://res.cloudinary.com/di5zocq4s/image/upload/v1234567890/cars/auto123.jpg"
}
```

#### **Actualizar Foto de Auto**
```http
PUT /api/cars/{id}/update-photo
Authorization: Bearer {token}
Content-Type: multipart/form-data

Body (form-data):
- Key: file
- Type: File
- Value: [nuevo archivo de imagen]
```

**Respuesta exitosa:**
```json
{
  "message": "Foto actualizada exitosamente",
  "imageUrl": "https://res.cloudinary.com/di5zocq4s/image/upload/v1234567890/cars/auto123_nuevo.jpg"
}
```

#### **Eliminar Foto de Auto**
```http
DELETE /api/cars/{id}/delete-photo
Authorization: Bearer {token}
```

**Respuesta exitosa:**
```json
{
  "message": "Foto eliminada exitosamente"
}
```

#### **Obtener URL de Foto**
```http
GET /api/cars/{id}/photo
Authorization: Bearer {token}
```

**Respuesta exitosa:**
```json
{
  "imageUrl": "https://res.cloudinary.com/di5zocq4s/image/upload/v1234567890/cars/auto123.jpg"
}
```

**Sin foto:**
```json
{
  "message": "El auto no tiene foto"
}
```

---

### 🛠️ **Endpoints de Debug/Utilidad**

#### **Debug - Verificar Autos del Usuario**
```http
GET /api/cars/debug
Authorization: Bearer {token}
```

**Respuesta:**
```json
{
  "user": "testuser",
  "userId": 1,
  "carsCount": 2,
  "cars": [
    {
      "id": 1,
      "brand": "Toyota",
      "model": "Corolla",
      "year": 2023,
      "licensePlate": "ABC123",
      "color": "Blanco",
      "photoUrl": "https://res.cloudinary.com/..."
    }
  ]
}
```

#### **Probar Formatos de Imagen**
```http
POST /api/cars/test-image-formats
Authorization: Bearer {token}
Content-Type: multipart/form-data

Body (form-data):
- Key: file
- Type: File
- Value: [archivo de imagen]
```

**Respuesta exitosa:**
```json
{
  "message": "Formato de imagen válido",
  "originalFilename": "foto.jpg",
  "contentType": "image/jpeg",
  "fileSize": 1234567,
  "imageUrl": "https://res.cloudinary.com/...",
  "supportedFormats": ["jpg", "jpeg", "png", "gif", "webp", "avif"]
}
```

---

## 📊 **Códigos de Estado HTTP**

| Código | Descripción |
|--------|-------------|
| 200 | OK - Operación exitosa |
| 201 | Created - Recurso creado exitosamente |
| 400 | Bad Request - Error en la petición |
| 401 | Unauthorized - Token inválido o expirado |
| 403 | Forbidden - Sin permisos |
| 404 | Not Found - Recurso no encontrado |
| 500 | Internal Server Error - Error del servidor |

--

## 🚨 **Manejo de Errores**

### **Errores Comunes**

#### **401 - Unauthorized**
```json
{
  "error": "Token JWT inválido o expirado"
}
```

#### **404 - Not Found**
```json
{
  "error": "Auto no encontrado"
}
```

#### **400 - Bad Request**
```json
{
  "error": "Error al subir la foto: [mensaje específico]"
}
```

#### **400 - Validación**
```json
{
  "error": "La placa ya existe"
}
```

---

## 📸 **Formatos de Imagen Soportados**

| Formato | Extensión | Tipo MIME | Tamaño Máximo |
|---------|-----------|-----------|---------------|
| JPEG | .jpg, .jpeg | image/jpeg | 10MB |
| PNG | .png | image/png | 10MB |
| GIF | .gif | image/gif | 10MB |
| WebP | .webp | image/webp | 10MB |
| AVIF | .avif | image/avif | 10MB |

---

## 🔒 **Seguridad**

- **Autenticación JWT** requerida para todas las rutas protegidas
- **Usuarios solo pueden gestionar sus propios autos**
- **Validación de tipos de archivo** para imágenes
- **Límites de tamaño** de archivo (10MB)
- **URLs seguras** (HTTPS) para Cloudinary

---

## 🗄️ **Estructura de Base de Datos**

### **Tabla: users**
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Primary Key |
| username | VARCHAR(50) | Usuario único |
| password | VARCHAR(255) | Contraseña encriptada |
| email | VARCHAR(100) | Email único |

### **Tabla: cars**
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT | Primary Key |
| brand | VARCHAR(100) | Marca del auto |
| model | VARCHAR(100) | Modelo del auto |
| year | INT | Año del auto |
| license_plate | VARCHAR(20) | Placa única |
| color | VARCHAR(50) | Color del auto |
| photo_url | VARCHAR(500) | URL de imagen en Cloudinary |
| user_id | BIGINT | Foreign Key a users |

---

## 🌐 **URLs de Cloudinary**

Las imágenes se organizan en:
```
https://res.cloudinary.com/di5zocq4s/image/upload/v{timestamp}/cars/{filename}.{ext}
```

**Ejemplo:**
```
https://res.cloudinary.com/di5zocq4s/image/upload/v1757025495/cars/auto123.jpg
```

---

## 📝 **Ejemplos de Uso con Frontend**

### **JavaScript/Fetch**
```javascript
// Subir foto
const uploadPhoto = async (carId, file, token) => {
  const formData = new FormData();
  formData.append('file', file);
  
  const response = await fetch(`/api/cars/${carId}/upload-photo`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`
    },
    body: formData
  });
  
  return await response.json();
};
```

### **React/HTML**
```jsx
const handleFileUpload = async (carId, file) => {
  const formData = new FormData();
  formData.append('file', file);
  
  try {
    const response = await fetch(`/api/cars/${carId}/upload-photo`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: formData
    });
    
    const result = await response.json();
    if (response.ok) {
      setImageUrl(result.imageUrl);
    } else {
      console.error('Error:', result.error);
    }
  } catch (error) {
    console.error('Error de red:', error);
  }
};
```

---

## 🚀 **Despliegue**

### **Variables de Entorno para Producción**
```properties
# Database
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}

# Cloudinary
cloudinary.cloud_name=${CLOUDINARY_CLOUD_NAME}
cloudinary.api_key=${CLOUDINARY_API_KEY}
cloudinary.api_secret=${CLOUDINARY_API_SECRET}
```

---

## 📞 **Soporte**

Para problemas o dudas sobre la API, revisar:
1. Logs de la aplicación
2. Códigos de estado HTTP
3. Mensajes de error en las respuestas
4. Cloudinary Dashboard para imágenes

---

**Versión:** 1.0.0  
**Última actualización:** Enero 2025
