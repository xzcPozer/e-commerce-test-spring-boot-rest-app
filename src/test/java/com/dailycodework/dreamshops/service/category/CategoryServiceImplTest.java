package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    // service for test
    @InjectMocks
    private CategoryServiceImpl categoryService;

    // declare the dependencies
    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addCategory_WhenCategoryDoesNotExist() {
        // Given
        Category newCategory = new Category(
                "electronics2"
        );

        // Mock the calls
        when(categoryRepository.existsByName(newCategory.getName()))
                .thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(new Category(
                        "electronics2"
                ));

        // When
        Category saveCategory = categoryService.addCategory(newCategory);

        // Then
        assertEquals(newCategory.getName(), saveCategory.getName());

        verify(categoryRepository, times(1)).existsByName(newCategory.getName());
        verify(categoryRepository, times(1)).save(newCategory);
    }

    @Test
    public void addCategory_WhenCategoryAlreadyExists() {
        // Given
        Category existCategory = new Category(
                "electronics"
        );

        // Mock the calls
        when(categoryRepository.existsByName(existCategory.getName()))
                .thenReturn(true);

        // When
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> categoryService.addCategory(existCategory)
        );

        // Then
        assertEquals(existCategory.getName() + " already exists", exception.getMessage());

        verify(categoryRepository, times(1)).existsByName(existCategory.getName());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void findCategoryById_WhenCategoryDoesNotExists() {
        // Given
        Long categoryId = 1L;
        Category category = new Category(
                "electronics"
        );

        // Mock the calls
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        // When
        Category savedCategory = categoryService.getCategoryById(categoryId);

        // Then
        assertEquals(category.getName(), savedCategory.getName());

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void findCategoryById_WhenCategoryAlreadyExists() {
        // Given
        Long categoryId = 1L;

        // Mock the calls
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getCategoryById(categoryId));

        // Then
        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void findCategoryByName_WhenCategoryDoesNotExists(){
        // Given
        String name = "electronics";

        // Mock the calls
        when(categoryRepository.findByName(name))
                .thenReturn(null);

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getCategoryByName(name));

        // Then
        assertEquals("Category not found by this name: " + name, exception.getMessage());

        verify(categoryRepository, times(1)).findByName(name);
    }

    @Test
    public void findCategoryByName_WhenCategoryExists(){
        // Given
        String name = "electronics";
        Category category = new Category(
                "electronics"
        );

        // Mock the calls
        when(categoryRepository.findByName(name))
                .thenReturn(category);

        // When
        Category getCategory = categoryService.getCategoryByName(name);

        // Then
        assertEquals(category.getName(), getCategory.getName());

        verify(categoryRepository, times(1)).findByName(name);
    }

    @Test
    public void findAllCategory_WhenCategoryExists(){
        // Given
        List<Category> categories = List.of(
                new Category("electronics")
        );

        // Mock the calls
        when(categoryRepository.findAll())
                .thenReturn(categories);

        // When
        List<Category> getCategories = categoryService.getAllCategories();

        // Then
        assertEquals(getCategories.size(), categories.size());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void findAllCategory_WhenThereAreNoCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of());

        // Act
        List<Category> categories = categoryRepository.findAll();

        // Assert
        assertEquals(0, categories.size());
    }

    @Test
    public void updateCategoryById_WhenCategoryExists(){
        // Given
        Long categoryId = 1L;
        Category category = new Category(
                "electronicsss"
        );

        // Mock the calls
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(new Category(
                        "electronicsss"
                ));

        // When
        Category saveCategory = categoryService.updateCategory(category, categoryId);

        // Then
        assertEquals(category.getName(), saveCategory.getName());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void updateCategoryById_WhenCategoryDoesNotExists(){
        // Given
        Long categoryId = 1L;
        Category category = new Category(
                "electronics"
        );

        // Mock the calls
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateCategory(category, categoryId)
        );

        // Then
        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void deleteCategoryById_WhenCategoryExists(){
        // Given
        Long categoryId = 1L;
        Category category = new Category(
                "electronics"
        );

        // Mock the calls
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        // When
        categoryService.deleteCategoryById(categoryId);

        // Then
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    public void deleteCategoryById_WhenCategoryDoesNotExists(){
        // Given
        Long categoryId = 1L;

        // Mock the calls
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> categoryService.deleteCategoryById(categoryId)
        );

        // Then
        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

}