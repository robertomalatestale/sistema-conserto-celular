package br.edu.ifsudeste.demo.config;

import br.edu.ifsudeste.demo.model.service.UsuarioService;
import br.edu.ifsudeste.demo.security.JwtAuthFilter;
import br.edu.ifsudeste.demo.security.JwtService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;


    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/v1/clientes/**").permitAll()
                        .requestMatchers("/api/v1/consertos/**").permitAll()
                        .requestMatchers("/api/v1/dispositivos/**").permitAll()
                        .requestMatchers("/api/v1/funcionarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/marcas/**").permitAll()
                        .requestMatchers("/api/v1/modelos/**").permitAll()
                        .requestMatchers("/api/v1/produtos/**").permitAll()
                        .requestMatchers("/api/v1/tipoProdutos/**").permitAll()
                        .requestMatchers("/api/v1/usuarios/**")
                        .permitAll()

                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        jwtFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }
}