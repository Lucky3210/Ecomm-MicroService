package com.ShoppingApp.InventoryService;

import com.ShoppingApp.InventoryService.Entity.Inventory;
import com.ShoppingApp.InventoryService.Repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory samsung = new Inventory();
			samsung.setSkuCode("Samsung");
			samsung.setQuantity(100);

			Inventory iphone = new Inventory();
			iphone.setSkuCode("Iphone");
			iphone.setQuantity(0);

			inventoryRepository.save(samsung);
			inventoryRepository.save(iphone);
		};
	}

}
