# Week 06 Lab 01 — Autenticación y Autorización con JWT

Lab del curso CS2031 (UTEC) que implementa autenticación stateless usando JSON Web Tokens (JWT) con Spring Security 6.

## Stack

| | |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.6 |
| Spring Security | 6.x |
| JWT | JJWT 0.12.6 |
| Base de datos | H2 in-memory |
| Build | Maven Wrapper |

---

## Estructura del proyecto

```
src/main/java/org/test/week06lab01/
├── config/
│   └── SecurityConfig.java          # Configuración de Spring Security
├── account/
│   ├── application/
│   │   └── AccountController.java   # Endpoint protegido
│   ├── domain/
│   │   ├── Account.java             # Entidad que implementa UserDetails
│   │   └── AccountService.java      # Implementa UserDetailsService
│   ├── dto/
│   │   └── PrivateResponse.java
│   └── infrastructure/
│       └── AccountRepository.java
└── auth/
    ├── application/
    │   └── AuthController.java      # Endpoints de signup/signin
    ├── components/
    │   ├── JwtService.java          # Generación y validación de tokens
    │   └── JwtAuthorizationFilter.java  # Filtro que intercepta cada request
    ├── domain/
    │   └── AuthService.java         # Lógica de registro y login
    └── dto/
        ├── SignUpRequest.java
        ├── SignInRequest.java
        ├── TokenResponse.java
        └── UserInfoDto.java
```

---

## Conceptos implementados

### 1. JWT (JSON Web Token)

`JwtService` genera tokens firmados con HS256 que incluyen:
- **subject**: email del usuario
- **roles**: lista de autoridades del usuario
- **issuedAt**: timestamp de emisión
- **expiration**: timestamp de expiración (1 hora por defecto)

La validación verifica la firma y que el token no haya expirado.

### 2. Spring Security — SecurityFilterChain

`SecurityConfig` configura:
- **CSRF deshabilitado**: la API es stateless, no usa cookies
- **Sesión STATELESS**: el servidor no guarda estado de sesión
- **Rutas públicas**: `/auth/**` no requiere autenticación
- **Todo lo demás**: requiere token válido
- **BCrypt**: las contraseñas se hashean antes de guardarse

### 3. Filtro personalizado — JwtAuthorizationFilter

Extiende `OncePerRequestFilter` y se ejecuta en cada request antes del filtro estándar de Spring Security:

```
Request entrante
      │
      ▼
JwtAuthorizationFilter.doFilterInternal()
      │
      ├── Extrae "Bearer <token>" del header Authorization
      ├── Valida firma y expiración del token
      ├── Extrae el username (email) del payload
      ├── Carga el Account desde la base de datos
      ├── Crea UsernamePasswordAuthenticationToken
      └── Establece el SecurityContext
      │
      ▼
Controlador ejecutado con usuario autenticado
```

### 4. Account como UserDetails

La entidad `Account` implementa `UserDetails` directamente, lo que permite que Spring Security la use sin adaptadores adicionales:

- `getUsername()` retorna el email
- `getPassword()` retorna el hash BCrypt
- `getAuthorities()` retorna lista vacía (sin roles diferenciados en este lab)

### 5. DaoAuthenticationProvider

`SecurityConfig` configura un `DaoAuthenticationProvider` que usa `AccountService` (que implementa `UserDetailsService`) para cargar el usuario y BCrypt para comparar contraseñas durante el login.

---

## Endpoints

### Públicos (no requieren token)

#### `POST /auth/signup` — Registro

```json
// Request body
{
  "firstName": "Ana",
  "lastName": "Torres",
  "email": "ana@utec.edu.pe",
  "password": "secret123"
}

// Response 200
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

#### `POST /auth/signin` — Login

```json
// Request body
{
  "email": "ana@utec.edu.pe",
  "password": "secret123"
}

// Response 200
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Protegidos (requieren `Authorization: Bearer <token>`)

#### `GET /accounts/private` — Info del usuario autenticado

```json
// Response 200
{
  "username": "ana@utec.edu.pe"
}

// Response 403 (sin token o token inválido)
```

---

## Flujo completo de autenticación

```
1. signup/signin  →  contraseña verificada / hasheada
                  →  JWT generado y retornado al cliente

2. Request a /accounts/private
   ├── Header: Authorization: Bearer eyJhbGci...
   ├── JwtAuthorizationFilter extrae el token
   ├── JwtService valida firma y expiración
   ├── Se carga el Account por email desde la DB
   ├── SecurityContext se puebla con el usuario
   └── AccountController recibe la llamada autenticada
```

---

## Configuración

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

jwt.secret=2bb80d537b1da3e38bd30361aa855686bde0eacd7162fef6a25fe97bf527a25b
jwt.expiration-access=3600000   # 1 hora en milisegundos
jwt.expiration-refresh=3600000
```

> **Nota**: En producción el `jwt.secret` debe cargarse desde variables de entorno o un secrets manager, nunca hardcodeado en el repositorio.

---

## Ejecución

No requiere Docker. La base de datos H2 corre en memoria y se destruye al apagar la app.

```bash
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

### Prueba rápida con curl

```bash
# 1. Registrar usuario
curl -s -X POST http://localhost:8080/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ana","lastName":"Torres","email":"ana@utec.edu.pe","password":"secret123"}'

# 2. Login (guarda el token)
TOKEN=$(curl -s -X POST http://localhost:8080/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"email":"ana@utec.edu.pe","password":"secret123"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

# 3. Endpoint protegido
curl -s http://localhost:8080/accounts/private \
  -H "Authorization: Bearer $TOKEN"
```

---

## Dependencias principales

```xml
<dependency>spring-boot-starter-security</dependency>
<dependency>spring-boot-starter-data-jpa</dependency>
<dependency>spring-boot-starter-web</dependency>
<dependency>jjwt-api (0.12.6)</dependency>
<dependency>h2database</dependency>
```
