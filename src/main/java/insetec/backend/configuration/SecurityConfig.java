package insetec.backend.configuration;

import insetec.backend.components.SecurityFilter;
import insetec.backend.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        .requestMatchers(HttpMethod.POST, "/announcements").hasRole("SELLER")
                        .requestMatchers(HttpMethod.POST, "/announcements/upload").hasRole("SELLER")
                        .requestMatchers(HttpMethod.GET, "/announcements/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/announcements/**").hasRole("SELLER")
                        .requestMatchers(HttpMethod.PUT, "/announcements/**").hasRole("SELLER")

                        .requestMatchers(HttpMethod.POST, "/reports").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/reports/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/reports/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/reports/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/users/current").hasRole("USER")

                        .requestMatchers(HttpMethod.PUT, "/users/block/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/unlock/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/sms/send").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/sms/validate").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

