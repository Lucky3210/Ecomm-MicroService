package com.ShoppingApp.InventoryService.Service;

import com.ShoppingApp.InventoryService.DTO.InventoryResponseDto;
import com.ShoppingApp.InventoryService.Entity.Inventory;
import com.ShoppingApp.InventoryService.Repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<InventoryResponseDto> isInStock(List<String> skuCode) {

        // stream and map skucode in inventory to inventory response dto
        // (create a logic for checking if the product is in stock)
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponseDto.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
    }
}
