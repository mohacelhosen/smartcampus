package com.smartcampus.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/university/**")
                .allowedOrigins("http://localhost:9090",
                        "http://localhost:5171",
                        "http://localhost:5172",
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://localhost:5175",
                        "https://campusmastermind.onrender.com", "http://campusmastermind.onrender.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
	
	
}
