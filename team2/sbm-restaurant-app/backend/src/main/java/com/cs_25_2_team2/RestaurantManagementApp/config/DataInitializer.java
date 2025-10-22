package com.cs_25_2_team2.RestaurantManagementApp.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cs_25_2_team2.RestaurantManagementApp.entities.CartEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.CustomerEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.MenuItemEntity;
import com.cs_25_2_team2.RestaurantManagementApp.entities.StaffEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CartRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.CustomerRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.MenuItemRepository;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.StaffRepository;

/**
 * Data initialization component to populate database with sample data.
 * This runs on application startup and creates initial menu items, customers, and staff.
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (customerRepository.count() == 0) {
            initializeData();
        }
    }
    
    private void initializeData() {
        System.out.println("Initializing database with sample data...");
        
        // Create sample customers
        createSampleCustomers();
        
        // Create sample staff
        createSampleStaff();
        
        // Create sample menu items
        createSampleMenuItems();
        
        System.out.println("Database initialization complete!");
    }
    
    private void createSampleCustomers() {
        // Customer 1
        CustomerEntity customer1 = new CustomerEntity("CUST000001", "johndoe", "John Doe", "123 Main St", "555-0001");
        customer1.setPasswordHash("hashedPassword");
        customer1.setEmail("john.doe@email.com");
        CustomerEntity savedCustomer1 = customerRepository.save(customer1);
        
        // Create cart for customer
        CartEntity cart1 = new CartEntity(savedCustomer1);
        cartRepository.save(cart1);
        
        // Customer 2
        CustomerEntity customer2 = new CustomerEntity("CUST000002", "janesmith", "Jane Smith", "456 Oak Ave", "555-0002");
        customer2.setPasswordHash("hashedPassword");
        customer2.setEmail("jane.smith@email.com");
        CustomerEntity savedCustomer2 = customerRepository.save(customer2);
        
        // Create cart for customer
        CartEntity cart2 = new CartEntity(savedCustomer2);
        cartRepository.save(cart2);
        
        System.out.println("Created sample customers: " + customerRepository.count());
    }
    
    private void createSampleStaff() {
        // Chef 1
        StaffEntity chef1 = new StaffEntity("CHEF001", "gordon", "Gordon Ramsay", "555-1001", StaffEntity.StaffRole.Chef);
        chef1.setPasswordHash("hashedPassword");
        staffRepository.save(chef1);
        
        // Chef 2
        StaffEntity chef2 = new StaffEntity("CHEF002", "julia", "Julia Child", "555-1002", StaffEntity.StaffRole.Chef);
        chef2.setPasswordHash("hashedPassword");
        staffRepository.save(chef2);
        
        // Delivery Staff
        StaffEntity delivery1 = new StaffEntity("DEL001", "speedster", "Fast Eddie", "555-2001", StaffEntity.StaffRole.Delivery);
        delivery1.setPasswordHash("hashedPassword");
        staffRepository.save(delivery1);
        
        System.out.println("Created sample staff: " + staffRepository.count());
    }
    
    private void createSampleMenuItems() {
        // French Fries
        MenuItemEntity fries = new MenuItemEntity("DISH001", "French Fries", 
            MenuItemEntity.Category.SIDE, new BigDecimal("3.99"), 
            MenuItemEntity.CookedType.Fried, MenuItemEntity.PotatoType.Russet);
        fries.setDescription("Golden crispy potato fries");
        menuItemRepository.save(fries);
        
        // Loaded Potato Skins
        MenuItemEntity potatoSkins = new MenuItemEntity("DISH002", "Loaded Potato Skins", 
            MenuItemEntity.Category.MAIN_DISH, new BigDecimal("5.99"), 
            MenuItemEntity.CookedType.Baked, MenuItemEntity.PotatoType.Russet);
        potatoSkins.setDescription("Baked potato skins with cheese and bacon");
        menuItemRepository.save(potatoSkins);
        
        // Potato Soup
        MenuItemEntity soup = new MenuItemEntity("DISH003", "Potato Soup", 
            MenuItemEntity.Category.SOUP, new BigDecimal("4.99"), 
            MenuItemEntity.CookedType.Soupped, MenuItemEntity.PotatoType.YukonGold);
        soup.setDescription("Creamy potato soup with herbs");
        menuItemRepository.save(soup);
        
        // Baked Potato
        MenuItemEntity bakedPotato = new MenuItemEntity("DISH004", "Baked Potato", 
            MenuItemEntity.Category.MAIN_DISH, new BigDecimal("4.49"), 
            MenuItemEntity.CookedType.Baked, MenuItemEntity.PotatoType.Russet);
        bakedPotato.setDescription("Fluffy baked potato with toppings");
        menuItemRepository.save(bakedPotato);
        
        // Sweet Potato Fries
        MenuItemEntity sweetFries = new MenuItemEntity("DISH005", "Sweet Potato Fries", 
            MenuItemEntity.Category.SIDE, new BigDecimal("4.29"), 
            MenuItemEntity.CookedType.Fried, MenuItemEntity.PotatoType.JapaneseSweet);
        sweetFries.setDescription("Crispy sweet potato fries with cinnamon");
        menuItemRepository.save(sweetFries);
        
        System.out.println("Created sample menu items: " + menuItemRepository.count());
    }
}