package com.online.orderservice.service;

import brave.Span;
import brave.Tracer;
import com.online.orderservice.dto.InventoryResponse;
import com.online.orderservice.dto.OrderLineItemsDTO;
import com.online.orderservice.dto.OrderRequest;
import com.online.orderservice.event.OrderPlaceEvent;
import com.online.orderservice.model.OrderLineItems;
import com.online.orderservice.model.Order;
import com.online.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final Tracer tracer;
    private final ModelMapper mapper;
    private final OrderRepository repository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlaceEvent> kafkaTemplate;


    public String placeOrder(OrderRequest request) {
        String orderNumber = UUID.randomUUID().toString();
        List<OrderLineItems> orderLineItems = request.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToDto)
                .toList();


        Order order = Order.builder()
                .orderNumber(orderNumber)
                .orderLineItems(orderLineItems)
                .build();

        List<String> skuCodes = order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).toList();

        log.info("Calling inventory service");

       Span inventoryServiceLookUp = this.tracer.nextSpan().name("InventoryServiceLookUp");

       try(Tracer.SpanInScope spanInScope = this.tracer.withSpanInScope(inventoryServiceLookUp.start())){
           InventoryResponse inventoryResponseArray[] = webClientBuilder.build()
                   .get()
                   .uri("http://inventory-service/api/inventory",
                           uriBuilder -> uriBuilder
                                   .queryParam("skuCodes", skuCodes)
                                   .build()
                   )
                   .retrieve()
                   .bodyToMono(InventoryResponse[].class)
                   .block();

           boolean allProductInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsnInStock);

           if (allProductInStock) {
               this.repository.save(order);
               kafkaTemplate.send("notificationTopic",
                                OrderPlaceEvent.builder()
                                .orderNumber(order.getOrderNumber())
                                .build());
               return "Order placed successfully";
           } else
               throw new IllegalArgumentException("The product ins not in stock, please try again later");
       }finally {
           inventoryServiceLookUp.finish();
       }
    }

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDTO) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsDTO.getSkuCode())
                .price(orderLineItemsDTO.getPrice())
                .quantity(orderLineItemsDTO.getQuantity())
                .build();
    }
}
