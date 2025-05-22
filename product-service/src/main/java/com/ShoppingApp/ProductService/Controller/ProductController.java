package com.ShoppingApp.ProductService.Controller;

import com.ShoppingApp.ProductService.DTO.ProductRequest;
import com.ShoppingApp.ProductService.DTO.ProductResponse;
import com.ShoppingApp.ProductService.Service.ProductService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Observed(
            name = "user.name",
            lowCardinalityKeyValues = {
                    "userType", "userType2"
            }
    )
    public void createProduct(@RequestBody ProductRequest productRequest){

        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Observed(
            name = "user.name",
            lowCardinalityKeyValues = {
                    "userType", "userType2"
            }
    )
    public List<ProductResponse> getAllProducts(){

        return productService.getAllProducts();
    }
}
