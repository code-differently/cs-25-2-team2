package com.cs_25_2_team2.RestaurantManagementApp.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.HandlerMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthInterceptorTest {

    private AuthInterceptor authInterceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HandlerMethod handlerMethod;

    @BeforeEach
    void setUp() {
        authInterceptor = new AuthInterceptor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        handlerMethod = mock(HandlerMethod.class);
    }

    @Test
    void testNoHandlerMethod() throws Exception {
        boolean result = authInterceptor.preHandle(request, response, new Object());
        assertTrue(result, "Should allow access for non-controller methods");
    }

    @Test
    void testNoRequiredRoleAnnotation() throws Exception {
        Method mockMethod = mock(Method.class);
        when(handlerMethod.getMethod()).thenReturn(mockMethod);
        when(mockMethod.getAnnotation(RequiredRole.class)).thenReturn(null);

        boolean result = authInterceptor.preHandle(request, response, handlerMethod);
        assertTrue(result, "Should allow access when no @RequiredRole annotation is present");
    }

    @Test
    void testRequireAuthNoSession() throws Exception {
        Method mockMethod = mock(Method.class);
        RequiredRole mockRequiredRole = mock(RequiredRole.class);
        when(mockRequiredRole.requireAuth()).thenReturn(true);
        when(mockRequiredRole.value()).thenReturn(new String[]{"ROLE_USER"});
        when(handlerMethod.getMethod()).thenReturn(mockMethod);
        when(mockMethod.getAnnotation(RequiredRole.class)).thenReturn(mockRequiredRole);

        HttpSession mockSession = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("username")).thenReturn("testUser");
        when(mockSession.getAttribute("userType")).thenReturn("ROLE_USER");

        boolean result = authInterceptor.preHandle(request, response, handlerMethod);
        assertTrue(result, "Should allow access when valid session attributes are present");
    }

    // Additional tests for other scenarios...
}