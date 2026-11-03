package com.example.order;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDetails(long id, String item, double price, UserDto user, String warning) {}
