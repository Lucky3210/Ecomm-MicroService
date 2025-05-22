package com.ShoppingApp.InventoryService.Controller;

import com.ShoppingApp.InventoryService.DTO.InventoryResponseDto;
import com.ShoppingApp.InventoryService.Service.InventoryService;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @Observed(
            name = "user.name",
            lowCardinalityKeyValues = {
                    "userType", "userType2"
            }
    )
    public List<InventoryResponseDto> isInStock(@RequestParam List<String> skuCode) throws InterruptedException {
        return inventoryService.isInStock(skuCode);
    }
}
