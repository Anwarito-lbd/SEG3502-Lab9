# Case Study: Securing a Stateless Kotlin/Spring Boot REST API with Spring Security 6

## 1. Problem & Context
In distributed web services, unauthenticated REST endpoints expose compute and API surface to arbitrary callers. For SEG3502 Lab 9, the baseline temperature conversion microservice (`ConverterController`) needed an access-control layer that enforces authentication across every route—including OpenAPI documentation endpoints—without introducing server-side HTTP session state.

## 2. Architectural Decisions

### 2.1 Stateless Filter Chain (`SecurityConfig.kt`)
- **HTTP Basic over Form Login:** Configured `httpBasic(Customizer.withDefaults())` so programmatic clients (`curl`, service callers) and browsers (via `WWW-Authenticate: Basic`) authenticate on each request without redirect-based login flows.
- **Stateless Session Policy:** Enforced `SessionCreationPolicy.STATELESS` so the Spring Security context is never persisted in an `HttpSession` (`JSESSIONID`), preserving horizontal scalability.
- **CSRF Disabled:** Disabled CSRF protection because stateless HTTP Basic APIs do not rely on browser-managed session cookies, avoiding false `403 Forbidden` rejections on API clients.
- **Uniform Authorization Rule:** Applied `anyRequest().authenticated()` rather than allowlisting `/swagger-ui.html` or `/v3/api-docs`, ensuring API schema discovery is also gated behind authentication.

### 2.2 Credential Storage & Password Encoding
- Used `PasswordEncoderFactories.createDelegatingPasswordEncoder()` (defaulting to bcrypt) rather than `{noop}` plaintext passwords, ensuring in-memory `UserDetails` entries (`user1`, `user2` with role `USER`) go through cryptographic verification on every request.
- Note: `InMemoryUserDetailsManager` with demo credentials is intentional for the academic lab specification; production deployments would swap the `UserDetailsService` bean for an external IdP (OAuth2/OIDC JWT resource server) or database-backed credential store over TLS.

### 2.3 Error Handling Separation
- Authentication failures are intercepted at the servlet filter layer (`401 Unauthorized` with `WWW-Authenticate: Basic realm="Realm"`).
- Authenticated requests with malformed path variables (e.g., `/celsius-fahrenheit/abc`) pass security and are handled cleanly by `ControllerExceptionHandler` (`@RestControllerAdvice`), returning `400 Bad Request` with `"Unable to process request"` instead of leaking stack traces.

## 3. Verification Strategy
Integration behavior is verified end-to-end with `@SpringBootTest` + `@AutoConfigureMockMvc` in `TempConverterApiApplicationTests.kt`, exercising the full Spring Security filter chain against:
1. Unauthenticated requests (`401`)
2. Valid `user1` Basic credentials on `/celsius-fahrenheit/50` (`200`, body `"122.0"`)
3. Valid `user2` Basic credentials on `/fahrenheit-celsius/122` (`200`, body `"50.0"`)
4. Invalid password for `user1` (`401`)
