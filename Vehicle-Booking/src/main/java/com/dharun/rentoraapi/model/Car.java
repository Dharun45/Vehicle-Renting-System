package com.dharun.rentoraapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "cars")
@Data  // Lombok auto-generates getters/setters/toString
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)



    private Long id;
    private String brand;
    private String model;
    private double pricePerDay;
    private boolean available;


}