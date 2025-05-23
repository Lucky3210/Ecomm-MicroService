package com.ShoppingApp.ProductService;

import com.ShoppingApp.ProductService.DTO.ProductRequest;
import com.ShoppingApp.ProductService.DTO.ProductResponse;
import com.ShoppingApp.ProductService.Model.Product;
import com.ShoppingApp.ProductService.Repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource 		// add the method to our test context
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
				.name("Samsung")
				.description("A cool stuff")
				.price(BigDecimal.valueOf(1300))
				.build();
	}

	private ProductResponse getProductResponse(){
		return ProductResponse.builder()
				.name(getProduct().getName())
				.description(getProduct().getDescription())
				.price(getProduct().getPrice())
				.build();
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
        Assertions.assertEquals(2, productRepository.findAll().size());
	}

	@Test
	void shouldGetProduct() throws Exception {

		ProductResponse productResponse = getProductResponse();
		String productResponseString = objectMapper.writeValueAsString(List.of(productResponse));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) 	// verify HTTP 200

				// we won't have to use the productResponse string again
				.andExpect(jsonPath("$[0].name").value("Samsung"))
				.andExpect(jsonPath("$[0].description").value("A cool stuff"))
				.andExpect(jsonPath("$[0].price").value(1300)); 	// verify response body

	};

	// Create a product object and save it to db
	private Product getProduct(){

		// in order for us not to re-persist data into the db, first we check if the product of name samsung exist
		// at first it won't exist but when we are retrieving other details we will fall into the .orElseGet method

		return productRepository.findByName("Samsung")
				.orElseGet(() -> productRepository.save(
						Product.builder()
								.name("Samsung")
								.description("A cool stuff")
								.price(BigDecimal.valueOf(1300))
								.build()
				));
	}
}
