package com.ShoppingApp.OrderService.Service;

import com.ShoppingApp.OrderService.DTO.InventoryResponseDto;
import com.ShoppingApp.OrderService.DTO.OrderLineItemsDto;
import com.ShoppingApp.OrderService.DTO.OrderRequest;
import com.ShoppingApp.OrderService.Entity.Order;
import com.ShoppingApp.OrderService.Entity.OrderLineItems;
import com.ShoppingApp.OrderService.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest){

        Order order = new Order();
        // set order number
        order.setOrderNumber(UUID.randomUUID().toString());

        // from orderRequest, return orderline items by mapping orderline items dto to orderline items
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(this::mapToDto)
                .toList();

        // set order line items in order obj
        order.setOrderLineItemsList(orderLineItems);

        // get all skuCodes from the order or orderRequest to be used by web client in the inventory service
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(orderLineItemsList -> orderLineItemsList.getSkuCode()).toList();

        // call the inventory service
        InventoryResponseDto[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://InventoryService/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponseDto[].class)      // we are retrieving the response as a single object(mono)
                .block();       // for synchronous communication(until we get a response then we are good to go)

        // ensure that inventoryResponseArray has all isInStock  field for each item as true
        boolean allProductsIsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponseDto::isInStock);

        // logic for saving order based on inventory response
        if (Boolean.TRUE.equals(allProductsIsInStock)) {
            orderRepository.save(order);
            return "Order placed Successfully";
        }
        else throw new IllegalArgumentException("Product not available");

    }

    // method maps orderLineItemsDto to Orderline items
    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

        // map, build and return the OrderLineItems
       return OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .skuCode(orderLineItemsDto.getSkuCode())
                .build();
    }
}
