package com.ShoppingApp.ProductService.Repository;

import com.ShoppingApp.ProductService.Model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByName(String name);
}
