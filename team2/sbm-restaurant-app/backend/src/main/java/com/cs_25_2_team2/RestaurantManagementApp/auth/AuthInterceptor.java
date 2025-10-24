package com.cs_25_2_team2.RestaurantManagementApp.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Interceptor for role-based access control.
 * Checks if user has required roles to access protected endpoints.
 * 
 * @author Team 2 - Phase 3 Authentication
 * @version 3.1
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // Skip auth check for non-controller methods
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        
        // Check if method has @RequiredRole annotation
        RequiredRole requiredRole = method.getAnnotation(RequiredRole.class);
        if (requiredRole == null) {
            // No role requirement, allow access
            return true;
        }

        HttpSession session = request.getSession(false);
        
        // Check if authentication is required and user is authenticated
        if (requiredRole.requireAuth()) {
            if (session == null || session.getAttribute("username") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Authentication required\"}");
                return false;
            }
        }

        // Get user's role from session
        String userRole = (String) session.getAttribute("userType");
        if (userRole == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"No user role found\"}");
            return false;
        }

        // Check if user has one of the required roles
        String[] allowedRoles = requiredRole.value();
        boolean hasRequiredRole = Arrays.stream(allowedRoles)
                .anyMatch(role -> role.equalsIgnoreCase(userRole));

        if (!hasRequiredRole) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Access denied. Required roles: " + 
                Arrays.toString(allowedRoles) + ", your role: " + userRole + "\"}");
            return false;
        }

        return true;
    }
}
