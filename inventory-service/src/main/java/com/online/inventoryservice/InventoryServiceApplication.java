package com.online.inventoryservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

   /* @Bean
    public CommandLineRunner loadData(InventoryRepository repository){
        return args -> {
            Inventory inventoryOne =  Inventory.builder()
                    .skuCode("Iphone_14")
                    .quantity(100)
                    .build();

            Inventory inventoryTwo =  Inventory.builder()
                    .skuCode("Iphone_13")
                    .quantity(0)
                    .build();

            repository.save(inventoryOne);
            repository.save(inventoryTwo);
        };
    }*/
}
