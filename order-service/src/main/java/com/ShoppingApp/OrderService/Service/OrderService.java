package com.ShoppingApp.OrderService.Service;

import com.ShoppingApp.OrderService.DTO.OrderLineItemsDto;
import com.ShoppingApp.OrderService.DTO.OrderRequest;
import com.ShoppingApp.OrderService.Entity.Order;
import com.ShoppingApp.OrderService.Entity.OrderLineItems;
import com.ShoppingApp.OrderService.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

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
        orderRepository.save(order);

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
