package com.online.service;

import com.online.dto.OrderLineItemsDTO;
import com.online.dto.OrderRequest;
import com.online.model.Order;
import com.online.model.OrderLineItems;
import com.online.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ModelMapper mapper;
    private final OrderRepository repository;

    public void placeOrder(OrderRequest request) {
        String orderNumber = UUID.randomUUID().toString();
        List<OrderLineItems> orderLineItems = request.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToDto)
                .toList();


        Order order = Order.builder()
                .orderNumber(orderNumber)
                .orderLineItems(orderLineItems)
                .build();

        this.repository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems = OrderLineItems.builder()
                .skuCode(orderLineItemsDTO.getSkuCode())
                .price(orderLineItemsDTO.getPrice())
                .quantity(orderLineItemsDTO.getQuantity())
                .build();
        return orderLineItems;
    }
}
