package com.ShoppingApp.ProductService.Service;

import com.ShoppingApp.ProductService.DTO.ProductRequest;
import com.ShoppingApp.ProductService.DTO.ProductResponse;
import com.ShoppingApp.ProductService.Model.Product;
import com.ShoppingApp.ProductService.Repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(ProductRequest productRequest){

        // map the productRequest dto to the product model
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        // save the product into the db
        productRepository.save(product);
        log.info("Product with id {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts(){

        // findAll return list of Product, but we want to return ProductResponse
        List<Product> products = productRepository.findAll();

        // map Product to ProductResponse and return it
        return products.stream()
                .map(product -> ProductResponse.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .build()
                        ).toList();
    }


    // reusable method to map product to product response
    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
