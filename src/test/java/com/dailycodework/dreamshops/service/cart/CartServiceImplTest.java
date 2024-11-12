package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CartServiceImplTest {
    @InjectMocks
    CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCartById_WhenCartExists(){
        // Arrange
        Long id = 1L;

        Cart cart = new Cart();
        cart.setId(id);
        cart.setTotalAmount(BigDecimal.valueOf(100));

        when(cartRepository.findById(id))
                .thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        // Act
        Cart result = cartService.getCart(id);

        // Assert
        assertEquals(cart, result);

        verify(cartRepository).findById(id);
        verify(cartRepository).save(cart);
    }

    @Test
    public void getCartById_WhenCartDoesNotExists(){
        // Arrange
        Long id = 1L;

        when(cartRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.getCart(id));

        assertEquals("Cart not found", exception.getMessage());

        verify(cartRepository).findById(id);
    }

    @Test
    public void clearCart_WhenCartExists(){
        // Arrange
        Long id = 1L;
        Set<CartItem> items= new HashSet<>();
        items.add(new CartItem());

        Cart cart = new Cart();
        cart.setId(id);
        cart.setItems(items);

        when(cartRepository.findById(id))
                .thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        // Act
        cartService.clearCart(id);

        // Assert
        verify(cartItemRepository).deleteAllByCartId(id);
        verify(cartRepository).findById(id);
    }

    @Test
    public void clearCart_WhenCartDoesNotExists(){
        // Arrange
        Long id = 1L;

        when(cartRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> cartService.clearCart(id));

        verify(cartRepository).findById(id);
    }

    @Test
    public void getTotalPrice_WhenCartExists(){
        // Arrange
        Long id = 1L;

        Cart cart = new Cart();
        cart.setId(id);
        cart.setTotalAmount(BigDecimal.valueOf(100));

        when(cartRepository.findById(id))
                .thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        // Act
        BigDecimal result = cartService.getTotalPrice(id);

        // Assert
        assertEquals(cart.getTotalAmount(), result);

        verify(cartRepository).findById(id);
    }

    @Test
    public void getTotalPrice_WhenCartDoesNotExists(){
        // Arrange
        Long id = 1L;

        when(cartRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> cartService.getTotalPrice(id));

        verify(cartRepository).findById(id);
    }

    @Test
    public void createCart_WhenCartExists(){
        // Arrange
        User user = new User();
        user.setId(1L);

        Cart existingCart = new Cart();
        existingCart.setId(1L);
        existingCart.setUser(user);

        when(cartRepository.findByUserId(user.getId()))
                .thenReturn(existingCart);

        // Act
        Cart result = cartService.initializeNewCart(user);

        // Assert
        assertNotNull(result);
        assertEquals(existingCart, result);
    }

    @Test
    public void createCart_WhenCartDoesNotExists(){
        // Arrange
        User user = new User();
        user.setId(1L);

        when(cartRepository.findByUserId(user.getId()))
                .thenReturn(null);
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cart result = cartService.initializeNewCart(user);

        // Assert
        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    public void getCartByUserId_WhenCartExists(){
        // Arrange
        Long userId = 1L;

        Cart existingCart = new Cart();
        existingCart.setId(1L);

        when(cartRepository.findByUserId(userId))
                .thenReturn(existingCart);

        // Act
        Cart result = cartService.getCartByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(existingCart, result);
    }

    @Test
    public void getCartByUserId_WhenCartDoesNotExists(){
        // Arrange
        Long userId = 1L;

        when(cartRepository.findByUserId(userId))
                .thenReturn(null);

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.getCartByUserId(userId));

        assertEquals("This user have no cart", exception.getMessage());
    }
}