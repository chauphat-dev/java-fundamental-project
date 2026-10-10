package com.learnjavawithcrocsa.store.services;

import com.learnjavawithcrocsa.store.entities.Address;
import com.learnjavawithcrocsa.store.entities.Category;
import com.learnjavawithcrocsa.store.entities.Product;
import com.learnjavawithcrocsa.store.entities.User;
import com.learnjavawithcrocsa.store.repositories.*;
import com.learnjavawithcrocsa.store.repositories.specifications.ProductSpec;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class UserServices {
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void showEntityStates(){
        var user= User.builder()
                .name("John")
                .password("password")
                .email("daf@gmail.com")
                .build();
        if (entityManager.contains(user)) {
            System.out.println("Persistent");
        }
        else System.out.println("Transient/Detached");

        userRepository.save(user);

        if (entityManager.contains(user)) {
            System.out.println("Persistent");
        }
        else System.out.println("Transient/Detached");
    }
    @Transactional
    public void showRelatedEntities(){
        var profile=profileRepository.findById(2L).orElseThrow();
        System.out.println(profile.getUser().getEmail());
    }


    public void fetchAddress(){
        var address=addressRepository.findById(1L).orElseThrow();
    }


    public void persistRelated(){
        var user=User.builder()
                .name("John")
                .password("password")
                .email("djjj@outlook.com")
                .build();
        var address= Address.builder()
                .street("123 Main St")
                .city("Main St")
                .zip("12345")
                .build();
        user.addAddress(address);

        userRepository.save(user);
    }
    @Transactional
    public void deleteRelated(){
        var user=userRepository.findById(3L).orElseThrow();
        var address=user.getAddresses().getFirst();
        user.removerAddress(address);
        userRepository.save(user);
    }


    @Transactional
    public void manageProducts(){
        productRepository.deleteById(4L);
    }


    @Transactional
    public void updateProductPrices(){
        productRepository.updatePriceByCategory(BigDecimal.valueOf(10), (byte)1);
    }
    @Transactional
    public void fetchProducts(){
        var product = new Product();
        product.setName("mouse");

        var matcher=ExampleMatcher.matching()
                .withIncludeNullValues()
                .withIgnorePaths("id","description")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        var example=Example.of(product,matcher);
        var products=productRepository.findAll(example);
        products.forEach(System.out::println);
    }


    public void fetchProductsByCriteria(){
        var products=productRepository.findProductByCriteria("Clothing");
        products.forEach(p->System.out.println(p.getName()));
    }


    public void fetchProductsBySpecification(String category) {
        Specification<Product> spec =Specification.allOf();
        if (category != null) {
            spec=spec.and(ProductSpec.isCategory(category));
        }

        productRepository.findAll(spec).forEach(p->System.out.println(p.getName()));
    }




    public void fetchPaginatedProducts(int pageNumber, int size) {
        PageRequest pageRequest = PageRequest.of(pageNumber, size);
        Page<Product> page= productRepository.findAll(pageRequest);
        var products = page.getContent();
        products.forEach(System.out::println);

        var totalPages=page.getTotalPages();
        var totalElements=page.getTotalElements();
        System.out.println("Total elements: "+totalElements);
        System.out.println("Total Pages: "+totalPages);
    }

    public void fetchSortedProducts(){
        var sort=Sort.by("name").and(
                Sort.by("price").descending()
        );

        productRepository.findAll(sort).forEach(System.out::println);
    }

    @Transactional
    public void fetchUsers(){
        var user=userRepository.findAllWithAddresses();
        user.forEach(u ->{
                System.out.println(u);
                u.getAddresses().forEach(System.out::println);

    });
    }
    @Transactional
    public void printLoyaltyProfiles(){
        var users=userRepository.findByLoyalUsers(2);
        users.forEach(u-> System.out.println(u.getId() + ": "+u.getEmail()));
    }
}
