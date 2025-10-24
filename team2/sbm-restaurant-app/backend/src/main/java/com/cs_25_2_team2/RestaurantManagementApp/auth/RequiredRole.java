package com.cs_25_2_team2.RestaurantManagementApp.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify required roles for accessing controller methods.
 * Used for role-based access control in the authentication system.
 * 
 * @author Team 2 - Phase 3 Authentication
 * @version 3.1
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredRole {
    /**
     * Array of roles that are allowed to access the method.
     * User must have at least one of these roles.
     */
    String[] value();
    
    /**
     * Whether authentication is required (default: true)
     */
    boolean requireAuth() default true;
}
