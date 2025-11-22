# VitalAgenda - Sistema de Gestión de Citas Médicas

Un sistema integral de gestión de citas médicas basado en web construido con Spring Boot, diseñado para optimizar la administración de la atención médica en hospitales y clínicas.

## ✨ Características

### 👥 Sistema de Usuarios Multi-Rol
- **Administrador**: Gestión completa del sistema y administración de usuarios
- **Médico**: Gestión de pacientes, programación de citas, registros médicos
- **Paciente**: Reserva de citas, acceso al historial médico, gestión de perfil

### 📅 Gestión de Citas
- Programar y gestionar citas médicas
- Verificación de disponibilidad en tiempo real
- Seguimiento del estado de citas (programada, completada, cancelada)
- Notificaciones automáticas para citas próximas

### 🏥 Registros Médicos
- Historial médico completo de pacientes
- Seguimiento de diagnósticos y tratamientos
- Control de acceso seguro para información sensible

### 🔔 Sistema de Notificaciones
- Notificaciones diarias automáticas para citas
- Notificaciones por email/SMS (configurable)
- Tareas programadas para sistemas de recordatorios

### 🔐 Características de Seguridad
- Integración con Spring Security
- Control de acceso basado en roles (RBAC)
- Encriptación de contraseñas con BCrypt
- Autenticación y autorización seguras

## 🛠 Tecnologías Utilizadas

### Backend
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.5.6** - Framework
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Autenticación y autorización
- **Spring Scheduling** - Automatización de tareas

### Frontend
- **Thymeleaf** - Plantillas del lado del servidor
- **Bootstrap** - Framework CSS (a través de plantillas)
- **JavaScript** - Interacciones del lado del cliente

### Base de Datos
- **MariaDB** - Base de datos principal
- **MySQL** - Soporte alternativo
- **PostgreSQL** - Soporte alternativo

### Herramientas de Desarrollo
- **Maven** - Gestión de dependencias
- **Lombok** - Generación de código
- **DevTools** - Utilidades de desarrollo

## 📋 Prerrequisitos

Antes de ejecutar esta aplicación, asegúrate de tener instalado lo siguiente:

- **Java 21** o superior
- **Maven 3.6+**
- Servidor de base de datos **MariaDB/MySQL/PostgreSQL**
- **Git** (para clonar el repositorio)

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone https://github.com/your-username/vitalagenda.git
cd vitalagenda
```

### 2. Configurar Base de Datos
Actualiza la conexión a la base de datos en `src/main/resources/application.properties`:

```properties
# Configuración de Base de Datos
spring.datasource.url=jdbc:mariadb://localhost:3306/vitalagenda
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
```

### 3. Construir la Aplicación
```bash
mvn clean install
```

### 4. Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

La aplicación se iniciará en `http://localhost:9090`

## 🗄 Configuración de Base de Datos

### Configuración de MariaDB
```sql
-- Crear base de datos
CREATE DATABASE vitalagenda;

-- Otorgar permisos
GRANT ALL PRIVILEGES ON vitalagenda.* TO 'user'@'localhost' IDENTIFIED BY 'password';
FLUSH PRIVILEGES;
```
## 🎯 Ejecutando la Aplicación

### Modo Desarrollo
```bash
mvn spring-boot:run
```

### Modo Producción
```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Usando Maven Wrapper
```bash
./mvnw spring-boot:run  # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

## 👤 Usuarios de Prueba

La aplicación crea automáticamente usuarios de prueba al iniciar:

| Rol | Email | Contraseña | Descripción |
|-----|-------|------------|-------------|
| Admin | `admin@hospital.com` | `admin123` | Administrador del sistema |
| Médico | `medico@hospital.com` | `medico123` | Médico general |
| Médico | `laura.martinez@hospital.com` | `laura123` | Cardióloga |
| Paciente | `paciente@hospital.com` | `paciente123` | Paciente de prueba |
| Paciente | `juan@example.com` | `123456` | Paciente existente con historial médico |

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── config/          # Configuración de seguridad
│   │   ├── controller/      # Controladores REST
│   │   ├── entity/          # Entidades JPA
│   │   ├── repository/      # Repositorios de datos
│   │   ├── service/         # Lógica de negocio
│   │   └── DemoApplication.java
│   └── resources/
│       ├── static/          # CSS, JS, imágenes
│       ├── templates/       # Plantillas Thymeleaf
│       └── application.properties
└── test/                    # Pruebas unitarias
```

## 🔗 Endpoints de API

### Autenticación
- `GET /login` - Página de inicio de sesión
- `POST /login` - Procesar inicio de sesión
- `POST /logout` - Cerrar sesión

### Endpoints de Administrador
- `GET /admin/dashboard` - Panel de administrador
- `GET /usuarios` - Gestión de usuarios
- `GET /medicos` - Gestión de médicos

### Endpoints de Médico
- `GET /medico/dashboard` - Panel de médico
- `GET /pacientes` - Lista de pacientes
- `GET /citas` - Gestión de citas
- `GET /historialclinico` - Registros médicos

### Endpoints de Paciente
- `GET /paciente/dashboard` - Panel de paciente
- `GET /paciente/citas` - Mis citas
- `GET /paciente/historial` - Mi historial médico

