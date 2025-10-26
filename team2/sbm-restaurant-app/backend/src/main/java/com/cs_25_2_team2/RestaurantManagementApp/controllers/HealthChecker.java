package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

@RestController
@RequestMapping("/api/health")
public class HealthChecker {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public HealthChecker() {
    }

    @GetMapping
    public int testSelectOne() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM carts;", Integer.class);
    }
}
