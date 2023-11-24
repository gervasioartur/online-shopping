package com.online.inventoryservice.service;

import com.online.inventoryservice.dto.InventoryResponse;
import com.online.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;

    @SneakyThrows
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        log.info("Wait started");
        Thread.sleep(10000);
        log.info("Wait ended");
        return this.repository.findBySkuCodeIn(skuCodes)
                .stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isnInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
