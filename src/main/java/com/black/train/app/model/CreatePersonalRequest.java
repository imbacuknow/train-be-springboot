package com.black.train.app.model;

import lombok.Data;

@Data
public class CreatePersonalRequest {
    private Long id;
    private String name;
    private double height;
    private double weight;
    private int age;
}
