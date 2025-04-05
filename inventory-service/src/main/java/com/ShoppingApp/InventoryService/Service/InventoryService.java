package com.ShoppingApp.InventoryService.Service;

import com.ShoppingApp.InventoryService.Entity.Inventory;
import com.ShoppingApp.InventoryService.Repository.InventoryRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode) {

        // find inventory by skuCode
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode).
                orElseThrow(() -> new NoSuchElementException("SkuCode not found"));

        // check if quantity is null and return false else true
        return inventory.getQuantity() != 0;
    }
}
