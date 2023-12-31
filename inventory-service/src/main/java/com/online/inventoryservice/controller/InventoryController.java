package com.online.inventoryservice.controller;

import com.online.inventoryservice.dto.InventoryResponse;
import com.online.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isnInStock(@RequestParam List<String> skuCodes) {
        return this.service.isInStock(skuCodes);
    }
}
