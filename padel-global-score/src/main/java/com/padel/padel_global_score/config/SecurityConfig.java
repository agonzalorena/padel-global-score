package com.padel.padel_global_score.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.padel.padel_global_score.presentation.dto.response.ErrorResponse;
import com.padel.padel_global_score.security.jwt.JwtFilter;
import com.padel.padel_global_score.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(TokenProvider tokenProvider, CorsConfigurationSource corsConfigurationSource) {
        this.tokenProvider = tokenProvider;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean // hashear la contraseña
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                // Desactiva CSRF porque usamos JWT (no cookies)
                .csrf(AbstractHttpConfigurer::disable);
        http

                // Política de sesión sin estado (fundamental para JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                // Manejo de errores de acceso no autorizado
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            ErrorResponse errorResponse = new ErrorResponse(
                                    HttpStatus.UNAUTHORIZED.value(),
                                    "No autenticado"
                            );
                            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
                        }));
        http
                // Aplica la seguridad solo a las rutas del backend (no recursos estáticos, etc.)
                .securityMatcher("/**")
                // Configuración de CORS
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource))
                // Autorizaciones de ejemplo
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                        .requestMatchers(HttpMethod.GET, "/statistics/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/matches/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/teams/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/locations/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/groups/**").permitAll()
                        .anyRequest().authenticated()
                )

                // Desactiva login por formulario y HTTP Basic (si solo usás JWT)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // Inserta el filtro JWT antes del UsernamePasswordAuthenticationFilter
                .addFilterBefore(new JwtFilter(this.tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
