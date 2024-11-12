package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.dto.OrderDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.*;
import com.dailycodework.dreamshops.repository.CartRepository;
import com.dailycodework.dreamshops.repository.OrderRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.service.cart.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartServiceImpl cartService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void placeOrder_WhenCartDoesNotEmpty() {
        // Подготовка данных
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        Set<OrderItem> orderItemList = new HashSet<>();

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setPrice(new BigDecimal(10));
        orderItem.setQuantity(2);

        orderItemList.add(orderItem);

        Order order = new Order();
        order.setOrderId(1L);
        order.setOrderItems(orderItemList);
        order.setTotalAmount(new BigDecimal(20));
        order.setUser(user);

        // Настройка моков
        when(cartService.getCartByUserId(userId))
                .thenReturn(cart);
        when(productRepository.save(any(Product.class)))
                .thenReturn(new Product());
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        // Выполнение метода
        Order savedOrder = orderService.placeOrder(userId);

        // Проверки
        assertNotNull(savedOrder);
        assertEquals(1L, savedOrder.getOrderId());
        assertEquals(1, order.getOrderItems().size());
        assertEquals(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())), savedOrder.getTotalAmount());
        assertEquals(userId, savedOrder.getUser().getId());

        verify(cartService, times(1)).getCartByUserId(userId);
        verify(cartService, times(1)).clearCart(cart.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void placeOrder_WhenEmptyCart() {
//        // Подготовка данных
//        Long userId = 1L;
//
//        User user = new User();
//        user.setId(userId);
//
//        Cart cart = new Cart();
//        cart.setId(1L);
//        cart.setUser(user);
//
//        // Настройка моков
//        when(cartService.getCartByUserId(userId))
//                .thenReturn(cart);
//        when(productRepository.save(any(Product.class)))
//                .thenReturn(new Product());
//        when(orderRepository.save(any(Order.class)))
//                .thenReturn(new Order());
//
//        // Выполнение метода
//        Order savedOrder = orderService.placeOrder(userId);
//
//        // Проверки
//        assertNotNull(savedOrder);
//        assertEquals(1L, savedOrder.getOrderId());
//        assertEquals(1, order.getOrderItems().size());
//        assertEquals(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())), savedOrder.getTotalAmount());
//        assertEquals(userId, savedOrder.getUser().getId());
//
//        verify(cartService, times(1)).clearCart(cart.getId());
//        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void placeOrder_WhenNullCart() {
        // Подготовка данных
        Long userId = 1L;

        // Настройка моков
        when(cartService.getCartByUserId(userId))
                .thenReturn(null);

        assertThrows(NullPointerException.class,
                () -> orderService.placeOrder(userId));

        verify(cartService, never()).clearCart(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getOrder_WhenOrderExists() {
        // Подготовка данных
        Long orderId = 1L;

        Order order = new Order();
        order.setOrderId(orderId);
        order.setTotalAmount(new BigDecimal(1000));

        OrderDto dto = new OrderDto();
        dto.setOrderId(orderId);
        dto.setTotalAmount(new BigDecimal(1000));

        // Настройка моков
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(modelMapper.map(any(Order.class), eq(OrderDto.class)))
                .thenReturn(dto);

        // Выполнение метода
        OrderDto orderDto = orderService.getOrder(orderId);

        // Проверки
        assertNotNull(orderDto);
        assertEquals(orderId, orderDto.getOrderId());
        assertEquals(order.getTotalAmount(), orderDto.getTotalAmount());
    }

    @Test
    void getOrder_WhenOrderDoesNotExists() {
        // Подготовка данных
        Long orderId = 1L;

        // Настройка моков
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Выполнение метода
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrder(orderId));

        // Проверки
        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    void getUsersOrders_WhenOrdersExists(){
        // Подготовка данных
        Long userId = 1L;

        Order order = new Order();
        order.setOrderId(1L);

        List<Order> orders = new ArrayList<>();
        orders.add(order);

        // Настройка моков
        when(orderRepository.findByUserId(userId))
                .thenReturn(orders);

        // Выполнение метода
        List<OrderDto> orderDtos = orderService.getUserOrders(userId);

        // Проверки
        assertNotNull(orderDtos);
        assertEquals(1, orderDtos.size());
    }

    @Test
    void getUsersOrders_WhenOrdersDoesNotExists(){
        // Подготовка данных
        Long userId = 1L;

        // Настройка моков
        when(orderRepository.findByUserId(userId))
                .thenReturn(null);

        // Выполнение метода
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.getUserOrders(userId));

        // Проверки
        assertEquals("This User have no orders", exception.getMessage());
    }
}