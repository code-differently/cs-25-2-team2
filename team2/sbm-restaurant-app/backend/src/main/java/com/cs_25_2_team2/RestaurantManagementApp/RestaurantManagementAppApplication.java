package com.cs_25_2_team2.RestaurantManagementApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.cs_25_2_team2.RestaurantManagementApp.repositories")
public class RestaurantManagementAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestaurantManagementAppApplication.class, args);
  }
}
