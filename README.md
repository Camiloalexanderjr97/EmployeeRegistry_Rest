# Sistema de Gestión de Usuarios con Autenticación JWT

Este es un sistema de gestión de usuarios con autenticación basada en JWT (JSON Web Tokens). El sistema permite el registro, autenticación y gestión de usuarios con diferentes roles de acceso.

## Características principales

- Autenticación y autorización con JWT
- Gestión de usuarios y roles (ADMIN, USER)
- Registro de nuevos usuarios
- Inicio y cierre de sesión con tokens JWT
- Validación de datos
- Documentación de la API con Swagger/OpenAPI
- Manejo global de excepciones
- Seguridad mejorada con protección CSRF y CORS
- Blacklist de tokens para cierre de sesión seguro

## 🚀 Requisitos previos

- Java 17 o superior
- Maven 3.9.6 o superior
- MySQL 8.0 o superior
- Git (opcional, para control de versiones)

## 🛠 Configuración inicial

1. **Base de datos**:
   ```sql
   CREATE DATABASE user_management;
   ```
   
2. **Configuración de la aplicación**:
   - El archivo `application.properties` contiene la configuración por defecto
   - Asegúrate de que las credenciales de la base de datos coincidan con tu entorno
   - Configura el `jwt.secret` con una cadena segura en producción

3. **Variables de entorno (opcional)**:
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/user_management
   SPRING_DATASOURCE_USERNAME=tu_usuario
   SPRING_DATASOURCE_PASSWORD=tu_contraseña
   JWT_SECRET=secretoSeguroParaJWT
   JWT_EXPIRATION_MS=86400000
   ```

## Ejecución

1. **Compilar el proyecto**:
   ```bash
   mvn clean install
   ```

2. **Ejecutar la aplicación**:
   ```bash
   mvn spring-boot:run
   ```

3. **Acceder a la documentación de la API**:
   - Swagger UI: http://localhost:8080/api/swagger-ui.html
   - OpenAPI JSON: http://localhost:8080/api/api-docs

## 🏗 Arquitectura y Estructura del Proyecto

El proyecto sigue una arquitectura hexagonal con las siguientes capas:

```
src/
├── main/
│   ├── java/
│   │   └── com/example/usuariojwt/
│   │       ├── application/           # Casos de uso y lógica de negocio
│   │       │   ├── dto/               # Objetos de transferencia de datos
│   │       │   │   ├── request/       # DTOs para peticiones
│   │       │   │   └── response/      # DTOs para respuestas
│   │       │   └── mapper/            # Mapeadores entre entidades y DTOs
│   │       │
│   │       ├── domain/                # Lógica de dominio
│   │       │   ├── model/             # Entidades del dominio
│   │       │   ├── repository/        # Interfaces de repositorio
│   │       │   └── service/           # Servicios de dominio
│   │       │
│   │       └── infrastructure/        # Adaptadores y configuración
│   │           ├── config/            # Configuraciones de Spring
│   │           │   ├── security/      # Configuración de seguridad
│   │           │   └── swagger/       # Configuración de documentación
│   │           │
│   │           ├── controller/        # Controladores REST
│   │           ├── exception/         # Manejo de excepciones
│   │           └── security/          # Implementación de seguridad
│   │               ├── jwt/           # Utilidades JWT
│   │               └── services/      # Servicios de autenticación
│   │
│   └── resources/
│       ├── static/                   # Archivos estáticos
│       ├── templates/                # Plantillas (si se usa MVC)
│       └── application.properties    # Configuración de la aplicación
│
└── test/                            # Pruebas unitarias y de integración
    ├── java/                       # Código de pruebas
    └── resources/                  # Recursos para pruebas
```

### 🔄 Flujo de Autenticación

1. El cliente envía credenciales a `/api/auth/signin`
2. El servidor valida las credenciales
3. Si son correctas, genera un token JWT
4. El token se envía al cliente en la respuesta
5. El cliente incluye el token en el encabezado `Authorization: Bearer <token>`
6. El servidor valida el token en cada solicitud protegida
7. Al cerrar sesión, el token se invalida en el servidor

## 🚀 Endpoints de la API

### 🔐 Autenticación
| Método | Endpoint | Descripción | Rol Requerido |
|--------|----------|-------------|----------------|
| `POST` | `/api/auth/signin` | Iniciar sesión y obtener token JWT | Público |
| `POST` | `/api/auth/signup` | Registrar nuevo usuario | Público |
| `POST` | `/api/auth/signout` | Cerrar sesión (invalida el token) | Cualquier usuario autenticado |

### 👥 Usuarios
| Método | Endpoint | Descripción | Rol Requerido |
|--------|----------|-------------|----------------|
| `GET` | `/api/users` | Obtener todos los usuarios | ADMIN |
| `GET` | `/api/users/me` | Obtener información del usuario actual | USER, ADMIN |
| `GET` | `/api/users/{id}` | Obtener usuario por ID | ADMIN |
| `PUT` | `/api/users/{id}` | Actualizar usuario | ADMIN o el propio usuario |
| `DELETE` | `/api/users/{id}` | Eliminar usuario (solo desactiva) | ADMIN |

## 🛠 Instalación y Ejecución

### Opción 1: Ejecución Local

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/tu-usuario/usuarioJWT.git
   cd usuarioJWT
   ```

2. **Configurar la base de datos**:
   ```sql
   CREATE DATABASE user_management;
   ```

3. **Configurar las variables de entorno** (opcional):
   Crea un archivo `.env` en la raíz del proyecto:
   ```env
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/user_management
   SPRING_DATASOURCE_USERNAME=tu_usuario
   SPRING_DATASOURCE_PASSWORD=tu_contraseña
   JWT_SECRET=secretoSeguroParaJWT
   JWT_EXPIRATION_MS=86400000
   ```

4. **Compilar y ejecutar**:
   ```bash
   # Compilar el proyecto
   mvn clean install
   
   # Ejecutar la aplicación
   mvn spring-boot:run
   ```

5. **Acceder a la documentación**:
   - Swagger UI: http://localhost:8080/api/swagger-ui.html
   - OpenAPI JSON: http://localhost:8080/api/v3/api-docs

### 🐳 Opción 2: Usando Docker

1. **Asegúrate de tener instalado**:
   - Docker Engine 20.10.0 o superior
   - Docker Compose 2.0.0 o superior

2. **Clonar el repositorio** (si no lo has hecho):
   ```bash
   git clone https://github.com/tu-usuario/usuarioJWT.git
   cd usuarioJWT
   ```

3. **Construir y ejecutar con Docker Compose**:
   ```bash
   # Construir y ejecutar los contenedores
   docker-compose up --build
   
   # Para ejecutar en segundo plano
   # docker-compose up -d --build
   ```

4. **La aplicación estará disponible en**:
   - Aplicación: http://localhost:8080
   - Base de datos MySQL: localhost:3306
   - Usuario administrador por defecto:
     - Usuario: `admin`
     - Contraseña: `admin123`

5. **Comandos útiles de Docker**:
   ```bash
   # Detener los contenedores
   docker-compose down
   
   # Ver logs de los contenedores
   docker-compose logs -f
   
   # Eliminar volúmenes (incluyendo la base de datos)
   docker-compose down -v
   ```

6. **Configuración del entorno** (opcional):
   Puedes modificar las variables de entorno en el archivo `docker-compose.yml` o crear un archivo `.env` en la raíz del proyecto.

## 🔧 Personalización

### Variables de Entorno

Puedes personalizar la configuración mediante variables de entorno. Crea un archivo `.env` en la raíz del proyecto con las siguientes variables:

```env
# Base de datos
MYSQL_ROOT_PASSWORD=your_secure_password
MYSQL_DATABASE=user_management
MYSQL_USER=app_user
MYSQL_PASSWORD=app_password

# Aplicación Spring Boot
SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/user_management?useSSL=false&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_secure_password
JWT_SECRET=your_jwt_secret_key_here
JWT_EXPIRATION_MS=86400000
```

### Puerto de la Aplicación

Para cambiar el puerto de la aplicación (por defecto 8080), modifica el archivo `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"  # Cambia el primer puerto (8081) al que prefieras
```

## 🔒 Seguridad

- Autenticación basada en JWT
- Protección contra CSRF
- Configuración CORS segura
- Blacklist de tokens para cierre de sesión
- Validación de entradas
- Manejo seguro de contraseñas con BCrypt

## 📚 Documentación Adicional

- [Documentación de Spring Security](https://spring.io/projects/spring-security)
- [Documentación de JWT](https://jwt.io/)
- [Guía de Spring Boot](https://spring.io/guides/gs/spring-boot/)

## 🤝 Contribución

1. Haz un fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Haz commit de tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Haz push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## Validaciones

- **Empleados**:
  - Nombre y apellidos son obligatorios (máx. 100 caracteres)
  - Tipo y número de documento son obligatorios (máx. 20 caracteres)
  - Fecha de nacimiento debe ser pasada y el empleado debe ser mayor de edad
  - Fecha de vinculación no puede ser futura
  - Cargo es obligatorio (máx. 100 caracteres)
  - Salario debe ser mayor que 0

## Pruebas

Para ejecutar las pruebas unitarias:

```bash
mvn test
```

## Despliegue

Para crear un archivo JAR ejecutable:

```bash
mvn clean package
```

El archivo JAR se generará en el directorio `target/` y podrá ejecutarse con:

```bash
java -jar target/usuario-jwt-0.0.1-SNAPSHOT.jar
```

