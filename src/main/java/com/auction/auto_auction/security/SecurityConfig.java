package com.auction.auto_auction.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Allow All Http GET-Methods without authenticate
        // http.csrf().disable()
        // .authorizeHttpRequests((authorize) -> authorize.requestMatchers(HttpMethod.GET,"/**")
        //              .permitAll().anyRequest().authenticated())
        // .httpBasic(Customizer.withDefaults());

        http.csrf().disable()
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(request -> true)
                        .permitAll().anyRequest().authenticated()
                );

        return http.build();
    }
}
