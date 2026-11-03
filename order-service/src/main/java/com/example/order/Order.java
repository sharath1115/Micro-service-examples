package com.example.order;

public record Order(long id, long userId, String item, double price) {}
