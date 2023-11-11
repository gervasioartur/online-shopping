package com.online.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;
import com.online.productservice.dto.ProductRequest;
import com.online.productservice.repository.ProductRepository;
import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ProductServiceApplicationTests {

  private final String URL = "/api/product";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  ProductRepository repository;

  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer(
    "mongo:7.0.2"
  );

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add(
      "spring.data.mongodb.uri",
      mongoDBContainer::getReplicaSetUrl
    );
  }

  private ProductRequest getProductRequest() {
    return ProductRequest
      .builder()
      .name("any_name")
      .description("any_description")
      .price(BigDecimal.valueOf(1200))
      .build();
  }

  @Test
  @DisplayName("It should create product")
  void shouldCreateProduct() throws Exception {
    ProductRequest productRequest = this.getProductRequest();
    String request = objectMapper.writeValueAsString(productRequest);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(this.URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(request)
      )
      .andExpect(MockMvcResultMatchers.status().isCreated());
    Assertions.assertTrue(repository.findAll().size() == 1);
  }
}