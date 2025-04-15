package com.taavi.jobseekerapp.security;

import com.taavi.jobseekerapp.repository.UserRepository;
import com.taavi.jobseekerapp.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Registreerimine & sisselogimine on avalik
                        .requestMatchers("/").permitAll() //Testkontrolleri lubamine
                        .requestMatchers(HttpMethod.GET, "/jobs", "/jobs/{id}").permitAll() // GET päringud kõigile (kuulutuste vaatamine)
                        .requestMatchers(HttpMethod.POST, "/jobs").hasRole("EMPLOYER") // Ainult tööandjad saavad postitada
                        .requestMatchers(HttpMethod.PUT, "/jobs/{id}").hasRole("EMPLOYER") // Ainult tööandjad saavad uuendada
                        .requestMatchers(HttpMethod.DELETE, "/jobs/{id}").hasRole("EMPLOYER") // Ainult tööandjad saavad kustutada
                        .requestMatchers("/h2-console/**").permitAll() // Lubab H2 konsoolile ligipääsu
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
