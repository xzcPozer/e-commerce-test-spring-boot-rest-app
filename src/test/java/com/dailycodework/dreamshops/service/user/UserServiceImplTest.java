package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.dto.UserDto;
import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.request.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getById_WhenUserExists(){
        // Arrange
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setFirstName("some firstname");
        user.setLastName("some lastname");
        user.setEmail("Some email");
        user.setPassword("123456");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertEquals(user, result);

        verify(userRepository).findById(userId);
    }

    @Test
    public void getById_WhenUserDoesNotExists(){
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));

        assertEquals("User Not Found", exception.getMessage());

        verify(userRepository).findById(userId);
    }

    @Test
    public void create_WhenUserAlreadyExists(){
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("email@example.com");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        // Act and Assert
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> userService.createUser(request));

        assertEquals(request.getEmail() + "already exist", exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
    }

    @Test
    public void create_WhenUserDoesNotExists(){
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("email@example.com");
        request.setPassword("password");
        request.setFirstName("First Name");
        request.setLastName("Last Name");

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword("encodedPassword");
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);
        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        // Act
        User result = userService.createUser(request);

        // Assert
        assertEquals(user, result);

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateById_WhenUserExists(){
        // Arrange
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("Existing First Name");
        existingUser.setLastName("Existing Last Name");

        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("Updated First Name");
        request.setLastName("Updated Last Name");

        User user = new User();
        user.setFirstName("Updated First Name");
        user.setLastName("Updated Last Name");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        // Act
        User result = userService.updateUser(request, userId);

        // Assert
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());

        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
    }

    @Test
    public void updateById_WhenUserDoesNotExists(){
        // Arrange
        Long userId = 1L;
        UserUpdateRequest request = new UserUpdateRequest();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(request, userId));

        assertEquals("User Not Found", exception.getMessage());

        verify(userRepository).findById(userId);
    }

    @Test
    public void deleteById_WhenUserExists(){
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).
                thenReturn(Optional.of(new User()));

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(userRepository).delete(any(User.class));
    }

    @Test
    public void deleteById_WhenUserDoesNotExists(){
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(userId));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(userId);
    }

    @Test
    public void convertedToDTO_WhenUserExists(){
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setEmail("email@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("First Name");
        userDto.setLastName("Last Name");
        userDto.setEmail("email@example.com");

        when(modelMapper.map(user, UserDto.class))
                .thenReturn(userDto);

        // Act
        UserDto result = userService.convertUserToDto(user);

        // Assert
        assertEquals(userDto, result);

        verify(modelMapper).map(user, UserDto.class);
    }

    @Test
    public void getByAuthentication_WhenUserAuth(){
        // Arrange
        String email = "email@example.com";
        User user = new User();
        user.setEmail(email);

        Authentication authentication = Mockito.mock(Authentication.class);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.getName())
                .thenReturn(email);
        when(userRepository.findByEmail(email))
                .thenReturn(user);

        // Act
        User result = userService.getAuthenticatedUser();

        // Assert
        assertEquals(user, result);

        verify(userRepository).findByEmail(email);
    }
}