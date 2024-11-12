package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import com.dailycodework.dreamshops.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartItemServiceImplTest {
    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addItemToCart_WhenCartItemDoesNotExist() {
        // Arrange
        Long cartId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("10.00"));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setTotalPrice(new BigDecimal("20.00"));

        when(cartService.getCart(cartId))
                .thenReturn(cart);
        when(productService.getProductById(productId))
                .thenReturn(product);
        when(cartItemRepository.save(any(CartItem.class)))
                .thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        // Act
        cartItemService.addItemToCart(cartId, productId, quantity);

        // Assert
        assertNotNull(cart.getItems());
        assertEquals(1, cart.getItems().size());
        assertEquals(cart, cartItem.getCart());
        assertEquals(product, cartItem.getProduct());
        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(product.getPrice(), cartItem.getUnitPrice());
        assertEquals(product.getPrice().multiply(new BigDecimal(quantity)), cartItem.getTotalPrice());
    }

    @Test
    public void addItemToCart_WhenCartItemExists() {
        // Arrange
        Long cartId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("10.00"));

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setTotalPrice();

        cart.addItem(cartItem);

        when(cartService.getCart(cartId))
                .thenReturn(cart);
        when(productService.getProductById(productId))
                .thenReturn(product);
        when(cartItemRepository.save(any(CartItem.class)))
                .thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        // Act
        cartItemService.addItemToCart(cartId, productId, quantity);

        // Assert
        assertNotNull(cart.getItems());
        assertEquals(1, cart.getItems().size());
        assertEquals(quantity + 1, cartItem.getQuantity());
        assertNotNull(cart.getItems());
        assertEquals(cart, cartItem.getCart());
        assertEquals(product, cartItem.getProduct());
        assertEquals(product.getPrice(), cartItem.getUnitPrice());
        assertEquals(product.getPrice().multiply(new BigDecimal(quantity + 1)), cartItem.getTotalPrice());
    }

    @Test
    public void testUpdateItemQuantity() {
        // Arrange
        Long cartId = 1L;
        Long productId = 1L;
        int quantity = 2;

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("10.0"));

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setTotalPrice();

        cart.addItem(cartItem);

        Set<CartItem> items = new HashSet<>();
        items.add(cartItem);
        cart.setItems(items);

        when(cartService.getCart(cartId))
                .thenReturn(cart);
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        // Act
        cartItemService.updateItemQuantity(cartId, productId, quantity);

        // Assert
        verify(cartRepository, times(1)).save(any(Cart.class));

        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(BigDecimal.valueOf(20.00), cartItem.getTotalPrice());
        assertEquals(BigDecimal.valueOf(20.00), cart.getTotalAmount());
    }

    @Test
    public void testUpdateItemQuantity_CartNotFound() {
        // Arrange
        Long cartId = 1L;
        Long productId = 1L;
        int quantity = 2;

        when(cartService.getCart(cartId))
                .thenReturn(null);

        // Act and Assert
        assertThrows(NullPointerException.class,
                () -> cartItemService.updateItemQuantity(cartId, productId, quantity));
    }
}