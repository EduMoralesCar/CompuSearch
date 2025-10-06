# 🖥️ Backend - CompuSearch

Este directorio contiene el backend para el proyecto **CompuSearch**.

## 🚀 Tecnologías utilizadas

| Tecnología        | Versión    | Descripción                                                            |
|-------------------|------------|------------------------------------------------------------------------|
| ☕ **Java**        | 23         | Lenguaje de programación principal                                     |
| 🌱 **Spring Boot**| 3.5.4      | Framework para aplicaciones Java modernas                              |
| 🌐 **Spring Web** | Incluida   | API REST y manejo de peticiones HTTP                                   |
| 🗄️ **Spring Data JPA** | Incluida   | Persistencia de datos con JPA e integración con MySQL                  |
| 🛡️ **Spring Security** | Incluida   | Seguridad y control de acceso a endpoints                              |
| 🔑 **JWT**        | (por implementar) | Autenticación y autorización para usuarios y administradores       |
| ✨ **Lombok**      | Incluida   | Reducción de código repetitivo (boilerplate)                           |
| 🐬 **MySQL**      | -          | Base de datos relacional                                               |
| 🛠️ **Maven**      | 3.8.9      | Gestión de dependencias y ciclo de vida del proyecto                   |

## 📦 Dependencias principales (`pom.xml`)

- `spring-boot-starter-actuator`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `spring-boot-starter-validation`
- `spring-boot-starter-web`
- `spring-boot-devtools` (desarrollo)
- `mysql-connector-j` (driver de MySQL)
- `lombok`
- `spring-boot-starter-test`, `spring-security-test` (testing)

## 🧑‍💻 IDE Recomendado

- **[IntelliJ IDEA](https://www.jetbrains.com/es-es/idea/download/?section=windows)** 🟢
- **[Visual Studio Code (VSCode)](https://code.visualstudio.com/)** 🔵
  > con extensiones de Java y Spring Boot

## 🗂️ Estructura del Backend

```
Backend/
│
├── 📁 .mvn/
│   └── 📁 wrapper/
│       └── 📄 maven-wrapper.properties
├── 📁 logs/
│   └── 📋 app.log 🚫 (auto-hidden)
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/
│   │   │   └── 📁 com/
│   │   │       └── 📁 universidad/
│   │   │           └── 📁 compuSearch/
│   │   │               ├── 📁 config/
│   │   │               │   ├── ☕ CorsConfig.java
│   │   │               │   ├── ☕ DataInitializer.java
│   │   │               │   └── ☕ SecurityConfig.java
│   │   │               ├── 📁 controller/
│   │   │               │   ├── ☕ AuthController.java
│   │   │               │   ├── ☕ RefreshTokenController.java
│   │   │               │   └── ☕ ResetPasswordController.java
│   │   │               ├── 📁 dto/
│   │   │               │   ├── ☕ LoginRequest.java
│   │   │               │   └── ☕ RegisterRequest.java
│   │   │               ├── 📁 entity/
│   │   │               │   ├── ☕ Empleado.java
│   │   │               │   ├── ☕ EstadoToken.java
│   │   │               │   ├── ☕ RefreshToken.java
│   │   │               │   ├── ☕ ResetToken.java
│   │   │               │   ├── ☕ Rol.java
│   │   │               │   ├── ☕ Tienda.java
│   │   │               │   ├── ☕ TipoUsuario.java
│   │   │               │   └── ☕ Usuario.java
│   │   │               ├── 📁 exception/
│   │   │               │   ├── ☕ AuthException.java
│   │   │               │   └── ☕ TokenNotFoundException.java
│   │   │               ├── 📁 filter/
│   │   │               │   └── ☕ JwtAuthenticationFilter.java
│   │   │               ├── 📁 repository/
│   │   │               │   ├── ☕ RefreshTokenRepository.java
│   │   │               │   ├── ☕ ResetTokenRepository.java
│   │   │               │   └── ☕ UsuarioRepository.java
│   │   │               ├── 📁 service/
│   │   │               │   ├── ☕ AuthService.java
│   │   │               │   ├── ☕ EmailService.java
│   │   │               │   ├── ☕ JwtService.java
│   │   │               │   ├── ☕ LoginAttemptService.java
│   │   │               │   ├── ☕ RefreshTokenService.java
│   │   │               │   ├── ☕ ResetPasswordAttemptService.java
│   │   │               │   ├── ☕ ResetTokenService.java
│   │   │               │   └── ☕ UsuarioService.java
│   │   │               └── ☕ CompuSearchApplication.java
│   │   └── 📁 resources/
│   │       ├── 📄 application.properties
│   │       └── 📄 logback-spring.xml
│   └── 📁 test/
│       ├── 📁 java/
│       │   └── 📁 com/
│       │       └── 📁 universidad/
│       │           └── 📁 compuSearch/
│       │               ├── 📁 repository/
│       │               │   ├── ☕ RefreshTokenRepositoryTest.java
│       │               │   ├── ☕ ResetTokenRepositoryTest.java
│       │               │   └── ☕ UsuarioRepositoryTest.java
│       │               └── ☕ CompuSearchApplicationTests.java
│       └── 📁 resources/
│           └── 📄 application-test.properties
├── 📁 target/ 🚫 (auto-hidden)
├── 📄 .gitattributes
├── 🚫 .gitignore
├── 📖 README.md
├── 📄 mvnw
├── 🐚 mvnw.cmd
└── 📄 pom.xml
```

### 📁 Descripción de carpetas

| Carpeta              | Descripción                                                                      |
|----------------------|----------------------------------------------------------------------------------|
| `controller/`        | Controladores REST para los endpoints.                                           |
| `model/`             | Entidades JPA que representan la lógica de datos.                                |
| `repository/`        | Interfaces para el acceso a la base de datos.                                    |
| `service/`           | Lógica de negocio.                                                               |
| `security/`          | Configuración de seguridad con Spring Security y JWT (a desarrollar).            |
| `resources/`         | Configuración y recursos.                                                        |
| `test/`              | Pruebas unitarias y de integración.                                              |

## 📈 Avance actual

- Proyecto inicializado con Spring Boot y Maven.
- Dependencias y plugins configurados en `pom.xml`.
- Estructura de carpetas propuesta.
- Configuración preparada para MySQL.
- Dependencias de Spring Security y JWT listas para implementar autenticación y roles.
- Falta implementar la lógica de autenticación, endpoints y modelos de negocio específicos.

## ⚙️ Ejecución

1. Ingresa a la carpeta Backend:
    ```bash
    cd CompuSearch/Backend
    ```
2. Importa el proyecto en tu IDE.
3. Crea una Base de Datos en MySQL llamada: `compusearch`
4. Configura tu base de datos y credenciales en `src/main/resources/application.properties`.
5. Ejecuta la aplicación desde tu IDE o usando Maven:
    ```bash
    ./mvnw spring-boot:run
    # Otra manera de Ejecutar
    ./mvnw.cmd clean spring-boot:run
    ```

## 📝 Notas

- Este README será actualizado conforme avance la implementación.
- Spring Security y el manejo de JWT se desarrollarán en las siguientes etapas.
- Cualquier contribución debe seguir la estructura y las tecnologías aquí descritas.

---
