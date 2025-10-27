package com.cs_25_2_team2.RestaurantManagementApp.services;

import com.cs_25_2_team2.RestaurantManagementApp.entities.*;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize cartItems list in CartEntity to prevent NullPointerException
        when(cartRepository.save(any(CartEntity.class))).thenAnswer(invocation -> {
            CartEntity cart = invocation.getArgument(0);
            if (cart.getCartItems() == null) {
                cart.setCartItems(new ArrayList<>());
            }
            return cart;
        });
        // Ensure CustomerEntity has a CartEntity
        when(customerRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long customerId = invocation.getArgument(0);
            CustomerEntity customer = new CustomerEntity();
            customer.setCustomerId(customerId);
            CartEntity cart = new CartEntity(customer);
            cart.setCartItems(new ArrayList<>());
            customer.setCart(cart);
        return Optional.of(customer);
    });
        
    }

    @Test
    void testAddItemToCart_NewCart() {
        // Arrange
        Long customerId = 1L;
        Long menuItemId = 1L;
        int quantity = 2;

        CustomerEntity customer = new CustomerEntity();
        customer.setCustomerId(customerId);
        CartEntity cart = new CartEntity(customer);
        cart.setCartItems(new ArrayList<>());
        customer.setCart(cart);

        MenuItemEntity menuItem = new MenuItemEntity();
        menuItem.setDishId(menuItemId);
        menuItem.setIsAvailable(true);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cart);
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

        // Act
        CartEntity result = restaurantService.addItemToCart(customerId, menuItemId, quantity);

        // Assert
        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItemEntity.class));
    }

    @Test
    void testRemoveItemFromCart_RemoveCompletely() {
        // Arrange
        Long customerId = 1L;
        Long menuItemId = 1L;
        int quantityToRemove = 2;

        CustomerEntity customer = new CustomerEntity();
        customer.setCustomerId(customerId);
        CartEntity cart = new CartEntity(customer);
        cart.setCartItems(new ArrayList<>());
        customer.setCart(cart);

        MenuItemEntity menuItem = new MenuItemEntity();
        menuItem.setDishId(menuItemId);

        CartItemEntity cartItem = new CartItemEntity(cart, menuItem, quantityToRemove);
        cart.getCartItems().add(cartItem);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));

        // Act
        CartEntity result = restaurantService.removeItemFromCart(customerId, menuItemId, quantityToRemove);

        // Assert
        assertNotNull(result);
        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    void testClearCart() {
        // Arrange
        Long customerId = 1L;

        CustomerEntity customer = new CustomerEntity();
        customer.setCustomerId(customerId);
        CartEntity cart = new CartEntity(customer);
        cart.setCartItems(new ArrayList<>());
        customer.setCart(cart);

        CartItemEntity cartItem = new CartItemEntity(cart, new MenuItemEntity(), 1);
        cart.getCartItems().add(cartItem);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        restaurantService.clearCart(customerId);

        // Assert
        verify(cartItemRepository, times(1)).deleteAll(cart.getCartItems());
        assertTrue(cart.getCartItems().isEmpty());
    }
}