package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ProductNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    // service for test
    @InjectMocks
    private ProductServiceImpl productService;

    // declare the dependencies
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addProduct_WhenProductDoesNotExistsAndCategoryExists() {
        // Given
        Category category = new Category("Some category");
        AddProductRequest productRequest = new AddProductRequest();
        productRequest.setName("Some name");
        productRequest.setBrand("Some brand");
        productRequest.setPrice(new BigDecimal("123.00"));
        productRequest.setInventory(20);
        productRequest.setDescription("Some description");
        productRequest.setCategory(category);

        Product savedProduct = new Product(
                "Some name",
                "Some brand",
                new BigDecimal("123.00"),
                20,
                "Some description",
                category
        );

        // Mock the calls
        when(productRepository.existsByNameAndBrand(productRequest.getName(), productRequest.getBrand()))
                .thenReturn(false);
        when(categoryRepository.findByName(productRequest.getCategory().getName()))
                .thenReturn(new Category("Some category"));
        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        // When
        Product getProduct = productService.addProduct(productRequest);

        // Then
        assertEquals(productRequest.getName(), getProduct.getName());
        assertEquals(productRequest.getBrand(), getProduct.getBrand());
        assertEquals(productRequest.getPrice(), getProduct.getPrice());
        assertEquals(productRequest.getInventory(), getProduct.getInventory());
        assertEquals(productRequest.getDescription(), getProduct.getDescription());
        assertEquals(productRequest.getCategory().getName(), getProduct.getCategory().getName());

        verify(productRepository, times(1)).existsByNameAndBrand(productRequest.getName(), productRequest.getBrand());
        verify(categoryRepository, times(1)).findByName(productRequest.getCategory().getName());
        verify(categoryRepository, never()).save(category);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void addProduct_WhenProductAlreadyExistsAndCategoryExists() {
        // Given
        Category category = new Category("Some category");
        AddProductRequest productRequest = new AddProductRequest();
        productRequest.setName("Some name");
        productRequest.setBrand("Some brand");
        productRequest.setPrice(new BigDecimal("123.00"));
        productRequest.setInventory(20);
        productRequest.setDescription("Some description");
        productRequest.setCategory(category);

        // Mock the calls
        when(productRepository.existsByNameAndBrand(productRequest.getName(), productRequest.getBrand()))
                .thenReturn(true);

        // When
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> productService.addProduct(productRequest)
        );

        // Then
        assertEquals(productRequest.getBrand() + " " + productRequest.getName() + " already exists", exception.getMessage());

        verify(productRepository, times(1)).existsByNameAndBrand(productRequest.getName(), productRequest.getBrand());
        verify(categoryRepository, never()).findByName(productRequest.getCategory().getName());
        verify(categoryRepository, never()).save(category);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void addProduct_WhenProductDoesNotExistsAndCategoryDoesNotExists() {
        // Given
        Category category = new Category("Some category");
        AddProductRequest productRequest = new AddProductRequest();
        productRequest.setName("Some name");
        productRequest.setBrand("Some brand");
        productRequest.setPrice(new BigDecimal("123.00"));
        productRequest.setInventory(20);
        productRequest.setDescription("Some description");
        productRequest.setCategory(category);

        Product savedProduct = new Product(
                "Some name",
                "Some brand",
                new BigDecimal("123.00"),
                20,
                "Some description",
                category
        );

        // Mock the calls
        when(productRepository.existsByNameAndBrand(productRequest.getName(), productRequest.getBrand()))
                .thenReturn(false);
        when(categoryRepository.findByName(productRequest.getCategory().getName()))
                .thenReturn(null);
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);
        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        // When
        Product getProduct = productService.addProduct(productRequest);

        // Then
        assertEquals(productRequest.getName(), getProduct.getName());
        assertEquals(productRequest.getBrand(), getProduct.getBrand());
        assertEquals(productRequest.getPrice(), getProduct.getPrice());
        assertEquals(productRequest.getInventory(), getProduct.getInventory());
        assertEquals(productRequest.getDescription(), getProduct.getDescription());
        assertEquals(productRequest.getCategory().getName(), getProduct.getCategory().getName());

        verify(productRepository, times(1)).existsByNameAndBrand(productRequest.getName(), productRequest.getBrand());
        verify(categoryRepository, times(1)).findByName(productRequest.getCategory().getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }


    @Test
    public void getProductById_WhenProductExists() {
        // Given
        Long productId = 1L;
        Category category = new Category("Some category");
        Product savedProduct = new Product(
                "Some name",
                "Some brand",
                new BigDecimal("123.00"),
                20,
                "Some description",
                category
        );

        // Mock the calls
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(savedProduct));

        // When
        Product getProduct = productService.getProductById(productId);

        // Then
        assertEquals(savedProduct.getName(), getProduct.getName());
        assertEquals(savedProduct.getBrand(), getProduct.getBrand());
        assertEquals(savedProduct.getPrice(), getProduct.getPrice());
        assertEquals(savedProduct.getInventory(), getProduct.getInventory());
        assertEquals(savedProduct.getDescription(), getProduct.getDescription());
        assertEquals(savedProduct.getCategory().getName(), getProduct.getCategory().getName());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void getProductById_WhenProductDoesNotExists() {
        // Given
        Long productId = 1L;

        // Mock the calls
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        // When
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(productId)
        );

        // Then
        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void deleteProductById_WhenProductExists() {
        // Given
        Long productId = 1L;
        Category category = new Category("Some category");
        Product savedProduct = new Product(
                "Some name",
                "Some brand",
                new BigDecimal("123.00"),
                20,
                "Some description",
                category
        );

        // Mock the calls
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(savedProduct));

        // When
        productService.deleteProductById(productId);

        // Then
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(savedProduct);
    }

    @Test
    public void deleteProductById_WhenProductDoesNotExists() {
        // Given
        Long productId = 1L;

        // Mock the calls
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        // When
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProductById(productId)
        );

        // Then
        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    public void updateProductById_WhenProductAndCategoryExists() {
        // Given
        Long productId = 1L;
        Category category = new Category("Some category");

        ProductUpdateRequest requestProduct = new ProductUpdateRequest();
        requestProduct.setName("Some name");
        requestProduct.setBrand("Some brand");
        requestProduct.setPrice(new BigDecimal("123.00"));
        requestProduct.setInventory(20);
        requestProduct.setDescription("Some description");
        requestProduct.setCategory(category);

        Product savedProduct = new Product(
                "Some name",
                "Some brand",
                new BigDecimal("123.00"),
                20,
                "Some description",
                category
        );

        // Mock the calls
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(savedProduct));
        when(categoryRepository.findByName(savedProduct.getCategory().getName()))
                .thenReturn(category);
        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        // When
        Product getProduct = productService.updateProduct(requestProduct, productId);

        // Then
        assertEquals(savedProduct.getName(), getProduct.getName());
        assertEquals(savedProduct.getBrand(), getProduct.getBrand());
        assertEquals(savedProduct.getPrice(), getProduct.getPrice());
        assertEquals(savedProduct.getInventory(), getProduct.getInventory());
        assertEquals(savedProduct.getDescription(), getProduct.getDescription());
        assertEquals(savedProduct.getCategory().getName(), getProduct.getCategory().getName());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(categoryRepository, times(1)).findByName(requestProduct.getCategory().getName());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void updateProductById_WhenProductDoesNotExists() {
        // Given
        Long productId = 1L;
        Category category = new Category("Some category");

        ProductUpdateRequest requestProduct = new ProductUpdateRequest();
        requestProduct.setName("Some name");
        requestProduct.setBrand("Some brand");
        requestProduct.setPrice(new BigDecimal("123.00"));
        requestProduct.setInventory(20);
        requestProduct.setDescription("Some description");
        requestProduct.setCategory(category);

        // Mock the calls
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        // When
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(requestProduct, productId)
        );

        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).findById(productId);
        verify(categoryRepository, never()).findByName(requestProduct.getCategory().getName());
        verify(productRepository, never()).save(any(Product.class));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void updateProductById_WhenProductExistsAndCategoryDoesNotExists() {
        // Given
        Long productId = 1L;
        Category category = new Category("Some category");
        Product savedProduct = new Product(
                "Some name",
                "Some brand",
                new BigDecimal("123.00"),
                20,
                "Some description",
                category
        );

        ProductUpdateRequest requestProduct = new ProductUpdateRequest();
        requestProduct.setName("Some name");
        requestProduct.setBrand("Some brand");
        requestProduct.setPrice(new BigDecimal("123.00"));
        requestProduct.setInventory(20);
        requestProduct.setDescription("Some description");
        requestProduct.setCategory(category);

        // Mock the calls
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(savedProduct));
        when(categoryRepository.findByName(requestProduct.getCategory().getName()))
                .thenReturn(null);
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);
        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        // When
        Product getProduct = productService.updateProduct(requestProduct, productId);

        assertEquals(savedProduct.getBrand(), getProduct.getBrand());
        assertEquals(savedProduct.getPrice(), getProduct.getPrice());
        assertEquals(savedProduct.getInventory(), getProduct.getInventory());
        assertEquals(savedProduct.getDescription(), getProduct.getDescription());
        assertEquals(savedProduct.getCategory().getName(), getProduct.getCategory().getName());

        verify(productRepository, times(1)).findById(productId);
        verify(categoryRepository, times(1)).findByName(requestProduct.getCategory().getName());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void getAllProduct_WhenProductsExists() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void getAllProduct_WhenProductsDoesNotExists() {
        // Arrange
        when(productRepository.findAll())
                .thenReturn(new ArrayList<>());

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(0, result.size());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void getProductsByCategoryName_WhenProductsExists() {
        // Arrange
        String category = "category";
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        when(productRepository.findByCategoryName(category))
                .thenReturn(products);

        // Act
        List<Product> result = productService.getProductsByCategory(category);

        // Assert
        assertEquals(2, result.size());

        verify(productRepository).findByCategoryName(category);
    }

    @Test
    public void getProductsByCategoryName_WhenProductsDoesNotExists() {
        // Arrange
        String category = "category";

        when(productRepository.findByCategoryName(category))
                .thenReturn(null);

        // Act and Assert
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.getProductsByCategory(category));

        assertEquals("No products by this category", exception.getMessage());

        verify(productRepository).findByCategoryName(category);
    }

    @Test
    public void getProductsByBrandName_WhenProductsExists() {
        // Arrange
        String brand = "brand";
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        when(productRepository.findByBrand(brand))
                .thenReturn(products);

        // Act
        List<Product> result = productService.getProductsByBrand(brand);

        // Assert
        assertEquals(2, result.size());

        verify(productRepository).findByBrand(brand);
    }

    @Test
    public void getProductsByBrandName_WhenProductsDoesNotExists() {
        // Arrange
        String brand = "brand";

        when(productRepository.findByBrand(brand))
                .thenReturn(null);

        // Act and Assert
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.getProductsByBrand(brand));

        assertEquals("No products by this brand", exception.getMessage());

        verify(productRepository).findByBrand(brand);
    }

    @Test
    public void getProductsByCategoryAndBrandName_WhenProductsExists() {
        // Arrange
        String category = "category";
        String brand = "brand";
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        when(productRepository.findByCategoryNameAndBrand(category, brand))
                .thenReturn(products);

        // Act
        List<Product> result = productService.getProductsByCategoryAndBrand(category, brand);

        // Assert
        assertEquals(2, result.size());

        verify(productRepository).findByCategoryNameAndBrand(category, brand);
    }

    @Test
    public void getProductsByCategoryAndBrandName_WhenProductsDoesNotExists() {
        // Arrange
        String category = "category";
        String brand = "brand";

        when(productRepository.findByCategoryNameAndBrand(category, brand))
                .thenReturn(null);

        // Act and Assert
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.getProductsByCategoryAndBrand(category, brand));

        assertEquals("No products by this category and brand", exception.getMessage());

        verify(productRepository).findByCategoryNameAndBrand(category, brand);
    }

    @Test
    public void getProductsByProductName_WhenProductsExists() {
        // Arrange
        String name = "product_name";
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        when(productRepository.findByName(name))
                .thenReturn(products);

        // Act
        List<Product> result = productService.getProductsByName(name);

        // Assert
        assertEquals(2, result.size());

        verify(productRepository).findByName(name);
    }

    @Test
    public void getProductsByProductName_WhenProductsDoesNotExists() {
        // Arrange
        String name = "product_name";

        when(productRepository.findByName(name))
                .thenReturn(null);

        // Act and Assert
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.getProductsByName(name));

        assertEquals("No products by this name", exception.getMessage());

        verify(productRepository).findByName(name);
    }

    @Test
    public void getProductsByBrandAndProductName_WhenProductsExists() {
        // Arrange
        String brand = "brand";
        String name = "product_name";
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        when(productRepository.findByBrandAndName(brand, name))
                .thenReturn(products);

        // Act
        List<Product> result = productService.getProductsByBrandAndName(brand, name);

        // Assert
        assertEquals(2, result.size());

        verify(productRepository).findByBrandAndName(brand, name);
    }

    @Test
    public void getProductsByBrandAndProductName_WhenProductsDoesNotExists() {
        // Arrange
        String brand = "brand";
        String name = "product_name";

        when(productRepository.findByBrandAndName(brand, name))
                .thenReturn(null);

        // Act and Assert
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.getProductsByBrandAndName(brand, name));

        assertEquals("No products by this brand and name", exception.getMessage());

        verify(productRepository).findByBrandAndName(brand, name);
    }

    @Test
    public void getCountProductsByBrandAndName_WhenProductsExists() {
        // Arrange
        String brand = "brand";
        String name = "product_name";
        Long count = 2L;

        when(productRepository.countByBrandAndName(brand, name))
                .thenReturn(count);

        // Act
        Long result = productService.countProductsByBrandAndName(brand, name);

        // Assert
        assertEquals(count, result);

        verify(productRepository).countByBrandAndName(brand, name);
    }

    @Test
    public void getCountProductsByBrandAndName_WhenProductsDoesNotExists() {
        // Arrange
        String brand = "brand";
        String name = "product_name";
        Long count = 0L;

        when(productRepository.countByBrandAndName(brand, name))
                .thenReturn(count);

        // Act
        Long result = productService.countProductsByBrandAndName(brand, name);

        // Assert
        assertEquals(count, result);

        verify(productRepository).countByBrandAndName(brand, name);
    }


    @Test
    public void getConvertedToDTOProducts_WhenProductsExists() {
        // Arrange
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        products.add(product);

        when(modelMapper.map(product, ProductDto.class))
                .thenReturn(new ProductDto());
        when(imageRepository.findByProductId(product.getId()))
                .thenReturn(new ArrayList<>());

        // Act
        List<ProductDto> result = productService.getConvertedProducts(products);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    public void getConvertedToDTOProducts_WhenProductsDoesNotExists() {
        // Arrange
        List<Product> products = null;

        // Act
        List<ProductDto> result = productService.getConvertedProducts(products);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void getConvertedToDTOProduct_WhenProductExists() {
        // Arrange
        Category category = new Category("Some category");
        List<Image> images = new ArrayList<>();
        List<ImageDto> imagesDto = new ArrayList<>();

        Product savedProduct = new Product(
                "Some name",
                "Some brand",
                new BigDecimal("123.00"),
                20,
                "Some description",
                category
        );

        ProductDto productDto = new ProductDto();
        productDto.setName("Some name");
        productDto.setBrand("Some brand");
        productDto.setPrice(new BigDecimal("123.00"));
        productDto.setInventory(20);
        productDto.setDescription("Some description");
        productDto.setCategory(category);
        productDto.setImages(imagesDto);


        when(modelMapper.map(savedProduct, ProductDto.class))
                .thenReturn(productDto);
        when(imageRepository.findByProductId(savedProduct.getId()))
                .thenReturn(images);

        // Act
        ProductDto result = productService.convertToDto(savedProduct);

        // Assert
        assertEquals(productDto, result);
    }

    @Test
    public void getConvertedToDTOProduct_WhenProductDoesNotExists() {
        // Arrange
        Product product = null;

        // Act and Assert
        try {
            productService.convertToDto(product);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }


}