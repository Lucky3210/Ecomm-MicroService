package com.ShoppingApp.OrderService.Service;

import com.ShoppingApp.OrderService.Config.WebClientConfig;
import com.ShoppingApp.OrderService.DTO.OrderLineItemsDto;
import com.ShoppingApp.OrderService.DTO.OrderRequest;
import com.ShoppingApp.OrderService.Entity.Order;
import com.ShoppingApp.OrderService.Entity.OrderLineItems;
import com.ShoppingApp.OrderService.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){

        Order order = new Order();
        // set order number
        order.setOrderNumber(UUID.randomUUID().toString());

        // from orderRequest, return orderline items by mapping orderline items dto to orderline items
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(this::mapToDto)
                .toList();

        // set order line items in order obj
        order.setOrderLineItemsList(orderLineItems);

        // call the inventory service
        Boolean inventoryResponse = webClient.get()
                .uri("https://localhost:5052/api/inventory")
                .retrieve()
                .bodyToMono(Boolean.class)      // we are retrieving the response as a single object(mono)
                .block();       // for synchronous communication(until we get a response then we are good to go)

        // logic for saving order based on inventory response
        if (Boolean.TRUE.equals(inventoryResponse)) orderRepository.save(order);
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
