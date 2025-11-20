package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // Páginas públicas
                .requestMatchers("/", "/login", "/api/usuarios/**").permitAll()
                // APIs protegidas
                .requestMatchers("/api/**").authenticated()
                // Dashboards por rol
                .requestMatchers("/paciente/dashboard").hasRole("PACIENTE")
                .requestMatchers("/paciente/citas").hasRole("PACIENTE")
                .requestMatchers("/paciente/historial").hasRole("PACIENTE")
                .requestMatchers("/medico/dashboard").hasRole("MEDICO")
                .requestMatchers("/admin/dashboard").hasRole("ADMIN")
                // Páginas de pacientes
                .requestMatchers("/paciente/**").hasRole("PACIENTE")
                // Páginas de médicos
                .requestMatchers("/medico/**").hasRole("MEDICO")
                // Páginas de admin
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Todas las demás requieren autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Usuario usuario = usuarioRepository.findByEmail(username);
            if (usuario == null) {
                throw new UsernameNotFoundException("Usuario no encontrado: " + username);
            }
            return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getContrasena())
                .roles(usuario.getRol().name())
                .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().iterator().next().getAuthority();

            if (role.equals("ROLE_PACIENTE")) {
                response.sendRedirect("/paciente/dashboard");
            } else if (role.equals("ROLE_MEDICO")) {
                response.sendRedirect("/medico/dashboard");
            } else if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/dashboard");
            } else {
                response.sendRedirect("/");
            }
        };
    }
}
