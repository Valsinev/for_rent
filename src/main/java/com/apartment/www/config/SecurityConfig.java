package com.apartment.www.config;

import org.springframework.context.annotation.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/attractions", "/services", "/location", "/pricing", "/photos").permitAll()
                        .requestMatchers("/admin/**").authenticated()
                        .requestMatchers("/reservations/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(withDefaults()) // enable login form
                .logout(withDefaults());   // enable logout

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("admin")
                .password("{noop}admin") // {noop} means "no encoding"
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}

