package com.cars.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarCreateDTO {
    
    private String brand;
    private String model;
    private Integer year;
    private String licensePlate;
    private String color;
    private String photoUrl;
}

