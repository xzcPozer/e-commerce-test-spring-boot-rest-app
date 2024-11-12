package com.dailycodework.dreamshops.data;

import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.model.Role;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    // вызывается при запуске
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultUserIfNotExists();
        createDefaultAdminIfNotExists();
        createDefaultCategoryIfNotExists();
        createDefaultProductIfNotExists();
        // createDefaultImagesForProductIfNotExists();
    }

    private void createDefaultCategoryIfNotExists() {

        Category category1 = new Category("Бытовая техника");
        if (!categoryRepository.existsByName(category1.getName()))
            categoryRepository.save(category1);

        Category category2 = new Category("Электроника");
        if (!categoryRepository.existsByName(category2.getName()))
            categoryRepository.save(category2);

        Category category3 = new Category("Компьютеры и ноутбуки");
        if (!categoryRepository.existsByName(category3.getName()))
            categoryRepository.save(category3);

        Category category4 = new Category("Мобильные телефоны");
        if (!categoryRepository.existsByName(category4.getName()))
            categoryRepository.save(category4);

        Category category5 = new Category("Телевизоры и аудио");
        if (!categoryRepository.existsByName(category5.getName()))
            categoryRepository.save(category5);

        Category category6 = new Category("Фото и видео");
        if (!categoryRepository.existsByName(category6.getName()))
            categoryRepository.save(category6);

        Category category7 = new Category("Игры и развлечения");
        if (!categoryRepository.existsByName(category7.getName()))
            categoryRepository.save(category7);

        Category category8 = new Category("Спортивные товары");
        if (!categoryRepository.existsByName(category8.getName()))
            categoryRepository.save(category8);

        Category category9 = new Category("Дом и сад");
        if (!categoryRepository.existsByName(category9.getName()))
            categoryRepository.save(category9);

        Category category10 = new Category("Автомобили и мотоциклы");
        if (!categoryRepository.existsByName(category10.getName()))
            categoryRepository.save(category10);
    }

    private void createDefaultProductIfNotExists() {
        Product product1 = new Product();
        product1.setInventory(25);
        product1.setPrice(new BigDecimal(35000));
        product1.setCategory(categoryRepository.findByName("Мобильные телефоны"));
        product1.setBrand("Samsung");
        product1.setName("Galaxy A54");
        product1.setDescription("default Samsung Galaxy A54 description");

        if (!productRepository.existsByNameAndBrand(product1.getName(), product1.getBrand())) {
            productRepository.save(product1);
        }

        Product product2 = new Product();
        product2.setInventory(30);
        product2.setPrice(new BigDecimal(25000));
        product2.setCategory(categoryRepository.findByName("Мобильные телефоны"));
        product2.setBrand("Apple");
        product2.setName("iPhone 13");
        product2.setDescription("default Apple iPhone 13 description");

        if (!productRepository.existsByNameAndBrand(product2.getName(), product2.getBrand())) {
            productRepository.save(product2);
        }

        Product product3 = new Product();
        product3.setInventory(20);
        product3.setPrice(new BigDecimal(40000));
        product3.setCategory(categoryRepository.findByName("Компьютеры и ноутбуки"));
        product3.setBrand("Dell");
        product3.setName("Inspiron 15 5000");
        product3.setDescription("default Dell Inspiron 15 5000 description");

        if (!productRepository.existsByNameAndBrand(product3.getName(), product3.getBrand())) {
            productRepository.save(product3);
        }

        Product product4 = new Product();
        product4.setInventory(15);
        product4.setPrice(new BigDecimal(30000));
        product4.setCategory(categoryRepository.findByName("Компьютеры и ноутбуки"));
        product4.setBrand("HP");
        product4.setName("Envy x360");
        product4.setDescription("default HP Envy x360 description");

        if (!productRepository.existsByNameAndBrand(product4.getName(), product4.getBrand())) {
            productRepository.save(product4);
        }

        Product product5 = new Product();
        product5.setInventory(25);
        product5.setPrice(new BigDecimal(20000));
        product5.setCategory(categoryRepository.findByName("Телевизоры и аудио"));
        product5.setBrand("LG");
        product5.setName("43UM7300");
        product5.setDescription("default LG 43UM7300 description");

        if (!productRepository.existsByNameAndBrand(product5.getName(), product5.getBrand())) {
            productRepository.save(product5);
        }

        Product product6 = new Product();
        product6.setInventory(30);
        product6.setPrice(new BigDecimal(25000));
        product6.setCategory(categoryRepository.findByName("Телевизоры и аудио"));
        product6.setBrand("Sony");
        product6.setName("KD43X720E");
        product6.setDescription("default Sony KD43X720E description");

        if (!productRepository.existsByNameAndBrand(product6.getName(), product6.getBrand())) {
            productRepository.save(product6);
        }

        Product product7 = new Product();
        product7.setInventory(20);
        product7.setPrice(new BigDecimal(40000));
        product7.setCategory(categoryRepository.findByName("Фото и видео"));
        product7.setBrand("Canon");
        product7.setName("EOS 80D");
        product7.setDescription("default Canon EOS 80D description");

        if (!productRepository.existsByNameAndBrand(product7.getName(), product7.getBrand())) {
            productRepository.save(product7);
        }

        Product product8 = new Product();
        product8.setInventory(15);
        product8.setPrice(new BigDecimal(30000));
        product8.setCategory(categoryRepository.findByName("Фото и видео"));
        product8.setBrand("Nikon");
        product8.setName("D5600");
        product8.setDescription("default Nikon D5600 description");

        if (!productRepository.existsByNameAndBrand(product8.getName(), product8.getBrand())) {
            productRepository.save(product8);
        }

        Product product9 = new Product();
        product9.setInventory(25);
        product9.setPrice(new BigDecimal(20000));
        product9.setCategory(categoryRepository.findByName("Игры и развлечения"));
        product9.setBrand("PlayStation");
        product9.setName("PS4 Slim");
        product9.setDescription("default PlayStation PS4 Slim description");

        if (!productRepository.existsByNameAndBrand(product9.getName(), product9.getBrand())) {
            productRepository.save(product9);
        }

        Product product10 = new Product();
        product10.setInventory(30);
        product10.setPrice(new BigDecimal(25000));
        product10.setCategory(categoryRepository.findByName("Игры и развлечения"));
        product10.setBrand("Xbox");
        product10.setName("Xbox One S");
        product10.setDescription("default Xbox Xbox One S description");

        if (!productRepository.existsByNameAndBrand(product10.getName(), product10.getBrand())) {
            productRepository.save(product10);
        }

        Product product11 = new Product();
        product11.setInventory(20);
        product11.setPrice(new BigDecimal(40000));
        product11.setCategory(categoryRepository.findByName("Спортивные товары"));
        product11.setBrand("Nike");
        product11.setName("Air Max 270");
        product11.setDescription("default Nike Air Max 270 description");

        if (!productRepository.existsByNameAndBrand(product11.getName(), product11.getBrand())) {
            productRepository.save(product11);
        }

        Product product12 = new Product();
        product12.setInventory(15);
        product12.setPrice(new BigDecimal(30000));
        product12.setCategory(categoryRepository.findByName("Спортивные товары"));
        product12.setBrand("Adidas");
        product12.setName("Superstar");
        product12.setDescription("default Adidas Superstar description");

        if (!productRepository.existsByNameAndBrand(product12.getName(), product12.getBrand())) {
            productRepository.save(product12);
        }

        Product product13 = new Product();
        product13.setInventory(25);
        product13.setPrice(new BigDecimal(20000));
        product13.setCategory(categoryRepository.findByName("Дом и сад"));
        product13.setBrand("IKEA");
        product13.setName("MALM");
        product13.setDescription("default IKEA MALM description");

        if (!productRepository.existsByNameAndBrand(product13.getName(), product13.getBrand())) {
            productRepository.save(product13);
        }

        Product product14 = new Product();
        product14.setInventory(30);
        product14.setPrice(new BigDecimal(25000));
        product14.setCategory(categoryRepository.findByName("Дом и сад"));
        product14.setBrand("Home Depot");
        product14.setName("Hampton Bay");
        product14.setDescription("default Home Depot Hampton Bay description");

        if (!productRepository.existsByNameAndBrand(product14.getName(), product14.getBrand())) {
            productRepository.save(product14);
        }

        Product product15 = new Product();
        product15.setInventory(20);
        product15.setPrice(new BigDecimal(40000));
        product15.setCategory(categoryRepository.findByName("Автомобили и мотоциклы"));
        product15.setBrand("Toyota");
        product15.setName("Corolla");
        product15.setDescription("default Toyota Corolla description");

        if (!productRepository.existsByNameAndBrand(product15.getName(), product15.getBrand())) {
            productRepository.save(product15);
        }

        Product product16 = new Product();
        product16.setInventory(15);
        product16.setPrice(new BigDecimal(30000));
        product16.setCategory(categoryRepository.findByName("Автомобили и мотоциклы"));
        product16.setBrand("Honda");
        product16.setName("Civic");
        product16.setDescription("default Honda Civic description");

        if (!productRepository.existsByNameAndBrand(product16.getName(), product16.getBrand())) {
            productRepository.save(product16);
        }

        Product product17 = new Product();
        product17.setInventory(25);
        product17.setPrice(new BigDecimal(20000));
        product17.setCategory(categoryRepository.findByName("Бытовая техника"));
        product17.setBrand("Whirlpool");
        product17.setName("WRF989SDAM");
        product17.setDescription("default Whirlpool WRF989SDAM description");

        if (!productRepository.existsByNameAndBrand(product17.getName(), product17.getBrand())) {
            productRepository.save(product17);
        }

        Product product18 = new Product();
        product18.setInventory(30);
        product18.setPrice(new BigDecimal(25000));
        product18.setCategory(categoryRepository.findByName("Бытовая техника"));
        product18.setBrand("LG");
        product18.setName("LFX28968S");
        product18.setDescription("default LG LFX28968S description");

        if (!productRepository.existsByNameAndBrand(product18.getName(), product18.getBrand())) {
            productRepository.save(product18);
        }

        Product product19 = new Product();
        product19.setInventory(20);
        product19.setPrice(new BigDecimal(40000));
        product19.setCategory(categoryRepository.findByName("Электроника"));
        product19.setBrand("Samsung");
        product19.setName("QLED 4K");
        product19.setDescription("default Samsung QLED 4K description");

        if (!productRepository.existsByNameAndBrand(product19.getName(), product19.getBrand())) {
            productRepository.save(product19);
        }

        Product product20 = new Product();
        product20.setInventory(15);
        product20.setPrice(new BigDecimal(30000));
        product20.setCategory(categoryRepository.findByName("Электроника"));
        product20.setBrand("Sony");
        product20.setName("Xperia 1");
        product20.setDescription("default Sony Xperia 1 description");

        if (!productRepository.existsByNameAndBrand(product20.getName(), product20.getBrand())) {
            productRepository.save(product20);
        }

        Product product21 = new Product();
        product21.setInventory(25);
        product21.setPrice(new BigDecimal(20000));
        product21.setCategory(categoryRepository.findByName("Мобильные телефоны"));
        product21.setBrand("Google");
        product21.setName("Pixel 4");
        product21.setDescription("default Google Pixel 4 description");

        if (!productRepository.existsByNameAndBrand(product21.getName(), product21.getBrand())) {
            productRepository.save(product21);
        }

        Product product22 = new Product();
        product22.setInventory(30);
        product22.setPrice(new BigDecimal(25000));
        product22.setCategory(categoryRepository.findByName("Мобильные телефоны"));
        product22.setBrand("OnePlus");
        product22.setName("7 Pro");
        product22.setDescription("default OnePlus 7 Pro description");

        if (!productRepository.existsByNameAndBrand(product22.getName(), product22.getBrand())) {
            productRepository.save(product22);
        }

        Product product23 = new Product();
        product23.setInventory(20);
        product23.setPrice(new BigDecimal(40000));
        product23.setCategory(categoryRepository.findByName("Компьютеры и ноутбуки"));
        product23.setBrand("Apple");
        product23.setName("MacBook Air");
        product23.setDescription("default Apple MacBook Air description");

        if (!productRepository.existsByNameAndBrand(product23.getName(), product23.getBrand())) {
            productRepository.save(product23);
        }

        Product product24 = new Product();
        product24.setInventory(15);
        product24.setPrice(new BigDecimal(30000));
        product24.setCategory(categoryRepository.findByName("Компьютеры и ноутбуки"));
        product24.setBrand("Lenovo");
        product24.setName("ThinkPad X1");
        product24.setDescription("default Lenovo ThinkPad X1 description");

        if (!productRepository.existsByNameAndBrand(product24.getName(), product24.getBrand())) {
            productRepository.save(product24);
        }

        Product product25 = new Product();
        product25.setInventory(25);
        product25.setPrice(new BigDecimal(20000));
        product25.setCategory(categoryRepository.findByName("Телевизоры и аудио"));
        product25.setBrand("Vizio");
        product25.setName("D40-D1");
        product25.setDescription("default Vizio D40-D1 description");

        if (!productRepository.existsByNameAndBrand(product25.getName(), product25.getBrand())) {
            productRepository.save(product25);
        }

        Product product26 = new Product();
        product26.setInventory(30);
        product26.setPrice(new BigDecimal(25000));
        product26.setCategory(categoryRepository.findByName("Телевизоры и аудио"));
        product26.setBrand("TCL");
        product26.setName("40S325");
        product26.setDescription("default TCL 40S325 description");

        if (!productRepository.existsByNameAndBrand(product26.getName(), product26.getBrand())) {
            productRepository.save(product26);
        }

        Product product27 = new Product();
        product27.setInventory(20);
        product27.setPrice(new BigDecimal(40000));
        product27.setCategory(categoryRepository.findByName("Фото и видео"));
        product27.setBrand("Canon");
        product27.setName("EOS 5D Mark IV");
        product27.setDescription("default Canon EOS 5D Mark IV description");

        if (!productRepository.existsByNameAndBrand(product27.getName(), product27.getBrand())) {
            productRepository.save(product27);
        }

        Product product28 = new Product();
        product28.setInventory(15);
        product28.setPrice(new BigDecimal(30000));
        product28.setCategory(categoryRepository.findByName("Фото и видео"));
        product28.setBrand("Nikon");
        product28.setName("D850");
        product28.setDescription("default Nikon D850 description");

        if (!productRepository.existsByNameAndBrand(product28.getName(), product28.getBrand())) {
            productRepository.save(product28);
        }

        Product product29 = new Product();
        product29.setInventory(25);
        product29.setPrice(new BigDecimal(20000));
        product29.setCategory(categoryRepository.findByName("Игры и развлечения"));
        product29.setBrand("PlayStation");
        product29.setName("PS4 Pro");
        product29.setDescription("default PlayStation PS4 Pro description");

        if (!productRepository.existsByNameAndBrand(product29.getName(), product29.getBrand())) {
            productRepository.save(product29);
        }

        Product product30 = new Product();
        product30.setInventory(30);
        product30.setPrice(new BigDecimal(25000));
        product30.setCategory(categoryRepository.findByName("Игры и развлечения"));
        product30.setBrand("Xbox");
        product30.setName("Xbox One X");
        product30.setDescription("default Xbox Xbox One X description");

        if (!productRepository.existsByNameAndBrand(product30.getName(), product30.getBrand())) {
            productRepository.save(product30);
        }
    }

    private void createDefaultUserIfNotExists() {
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail))
                continue;
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default vet user " + i + " created successfully");
        }
    }

    private void createDefaultAdminIfNotExists() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail))
                continue;
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user " + i + " created successfully");
        }
    }

    private void createDefaultRoleIfNotExists(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }
}
