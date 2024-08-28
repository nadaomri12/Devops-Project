package backend.stageproject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrossConfiguration  {

        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOrigins("http://localhost:4200", "http://13.64.76.130:4200") // Spécifiez les origines exactes
                       
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Autoriser ces méthodes
                            .allowedHeaders("*") // Autoriser tous les en-têtes
                            .allowCredentials(true); // Autoriser les informations d'identification
                }
            };
        }
    }

