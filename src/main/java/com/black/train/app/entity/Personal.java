package com.black.train.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "PERSONAL")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "HEIGHT", nullable = false)
    private double height;

    @Column(name = "WEIGHT", nullable = false)
    private double weight;

    @Column(name = "AGE")
    private int age;
}
