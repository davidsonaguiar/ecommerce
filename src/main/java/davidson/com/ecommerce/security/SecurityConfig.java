package davidson.com.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(customizer -> {
                customizer.requestMatchers(
                    requestMatchers -> requestMatchers.getMethod().equals("DELETE")).hasRole("ADMIN");
            });

        return http.build();
    }
}
