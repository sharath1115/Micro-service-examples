package com.example.order;

// The order-service's own view of a user (it only keeps what it needs)
public record UserDto(long id, String name, String email) {}
