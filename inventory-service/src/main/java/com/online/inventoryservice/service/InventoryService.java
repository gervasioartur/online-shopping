package com.online.inventoryservice.service;

import com.online.inventoryservice.repository.InventoryRepository;
import com.online.inventoryservice.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
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
