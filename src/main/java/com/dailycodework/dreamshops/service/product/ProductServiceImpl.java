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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        if(productRepository.existsByNameAndBrand(request.getName(), request.getBrand())){
            throw new AlreadyExistsException(request.getBrand() + " " + request.getName() + " already exists");
        }

        Category category = createCategoryIfNotExists(request.getCategory());
        request.setCategory(category);

        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    private Category createCategoryIfNotExists(Category categoryFromRequest){
        return Optional.ofNullable(categoryRepository.findByName(categoryFromRequest.getName()))
                .orElseGet(()->{
                    Category newCategory = new Category(categoryFromRequest.getName());
                    return categoryRepository.save(newCategory);
                });
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        ()->{throw new ProductNotFoundException("Product not found");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = createCategoryIfNotExists(request.getCategory());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return Optional.ofNullable(productRepository.findByCategoryName(category))
                .orElseThrow(() -> new ProductNotFoundException("No products by this category"));
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return Optional.ofNullable(productRepository.findByBrand(brand))
                .orElseThrow(() -> new ProductNotFoundException("No products by this brand"));
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return Optional.ofNullable(productRepository.findByCategoryNameAndBrand(category, brand))
                .orElseThrow(() -> new ProductNotFoundException("No products by this category and brand"));
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return Optional.ofNullable(productRepository.findByName(name))
                .orElseThrow(() -> new ProductNotFoundException("No products by this name"));
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return Optional.ofNullable(productRepository.findByBrandAndName(brand, name))
                .orElseThrow(() -> new ProductNotFoundException("No products by this brand and name"));
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return Optional.ofNullable(products)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        List<Image> images = imageRepository.findByProductId(product.getId());

        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);

        return productDto;
    }
}
