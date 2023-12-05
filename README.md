## OLINE SHOPPING MICROSERVICES EXAMPLE

# Services

    * prouct service -  Create and view products, acts as product categories.
    * Order service - Cn order porducts.
    * Inventory service - Can check if product is in stock or not.
    * Notification service - Can send notifications, after product or is placed.
    
    ## Note: 
        # Order service, inventory service  and  notification service are going to interact with each product other.

# Running the Services

    ## First must ensure that the no running containers with our services names, that we cann do running the following commands:
         docker stop product-service order-service notification-service inventory-service api-gateway discovery-server
         docker rm product-service order-service notification-service inventory-service api-gateway discovery-server
         docker compose up -d

# Removing old containers and images

    docker stop product-service order-service notification-service inventory-service api-gateway discovery-server
    docker rm product-service order-service notification-service inventory-service api-gateway discovery-server
    docker rmi gervasioartur/product-service gervasioartur/order-service gervasioartur/notification-service gervasioartur/inventory-service gervasioartur/api-gateway gervasioartur/discovery-server
