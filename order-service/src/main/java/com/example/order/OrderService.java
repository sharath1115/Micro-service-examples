package com.example.order;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final UserClient userClient;

    private final List<Order> orders = List.of(
            new Order(101, 1, "Laptop", 55000),
            new Order(102, 2, "Headphones", 2500),
            new Order(103, 1, "Mouse", 800));

    public OrderService(UserClient userClient) {
        this.userClient = userClient;
    }

    public List<Order> findAll() {
        return orders;
    }

    public OrderDetails findById(long id) {
        Order order = orders.stream()
                .filter(o -> o.id() == id)
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException(id));

        return userClient.findUser(order.userId())
                .map(user -> new OrderDetails(order.id(), order.item(), order.price(), user, null))
                .orElseGet(() -> new OrderDetails(order.id(), order.item(), order.price(), null,
                        "User service unavailable"));
    }
}
