package com.black.train.app.model;

import lombok.Data;

@Data
public class CreatePersonRequest {
    private Long id;
    private String name;
    private double height;
    private double weight;
    private int age;
    private String email;
}
