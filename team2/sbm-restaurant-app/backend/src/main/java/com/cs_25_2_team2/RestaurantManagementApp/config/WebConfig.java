package com.cs_25_2_team2.RestaurantManagementApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cs_25_2_team2.RestaurantManagementApp.auth.AuthInterceptor;

/**
 * Web MVC configuration for authentication system.
 * Registers the authentication interceptor for role-based access control.
 * 
 * @author Team 2 - Phase 3 Authentication
 * @version 3.1
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")  // Apply to all API endpoints
                .excludePathPatterns(
                    "/api/auth/**",          // Exclude auth endpoints
                    "/api/public/**",        // Exclude public endpoints
                    "/api/menu/**"           // Allow public menu access
                );
    }
}
