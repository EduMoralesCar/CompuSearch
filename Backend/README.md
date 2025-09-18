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

- **IntelliJ IDEA** 🟢
- **VSCode** (con extensiones de Java y Spring) 🔵

## 🗂️ Estructura propuesta del Backend

```
Backend/
│
├── src/
│   ├── main/
│   │   ├── java/com/universidad/compusearch/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── security/
│   │   │   └── CompuSearchApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/com/universidad/compusearch/
│
├── pom.xml
└── README.md
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
3. Configura tu base de datos y credenciales en `src/main/resources/application.properties`.
4. Ejecuta la aplicación desde tu IDE o usando Maven:
    ```bash
    ./mvnw spring-boot:run
    ```

## 📝 Notas

- Este README será actualizado conforme avance la implementación.
- Spring Security y el manejo de JWT se desarrollarán en las siguientes etapas.
- Cualquier contribución debe seguir la estructura y las tecnologías aquí descritas.

---
