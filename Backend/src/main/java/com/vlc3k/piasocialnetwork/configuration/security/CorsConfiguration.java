package com.vlc3k.piasocialnetwork.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(getOrigins())
                .allowedMethods("*");
    }

    @Bean(name = "origins")
    public String[] getOrigins() {
        return new String[]{
                "http://localhost:3000",
                "null"
        };
    }
}