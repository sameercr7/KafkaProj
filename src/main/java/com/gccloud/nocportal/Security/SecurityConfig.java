package com.gccloud.nocportal.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationSuccessHandler customAuthSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/noc/dashboard").permitAll()
                        .requestMatchers("/noc/departmentlogin").permitAll()
                                .requestMatchers("/noc/signup").permitAll()
                                .requestMatchers("/data/signUpProcess").permitAll()
                        .requestMatchers("/noc/**").hasAnyAuthority("admin", "deputyAdmin","subAdmin","hod","administration","agency", "ce1", "ce2", "se", "ee")
                                .requestMatchers("/noc/orders").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/noc/login").permitAll()
                        .loginProcessingUrl("/process_login")
                        .failureForwardUrl("/unauthenticated")
                        .successHandler(customAuthSuccessHandler())
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/unauthorised")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/noc/portal")
                        .logoutUrl("/perform_logout").permitAll()
                        .deleteCookies("JSESSIONID")
                )
                .headers(headers -> headers
                        .frameOptions( frame -> frame.sameOrigin()));


        return http.build();
    }
}

