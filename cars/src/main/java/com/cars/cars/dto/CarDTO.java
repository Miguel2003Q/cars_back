package com.cars.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {
    
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String licensePlate;
    private String color;
    private String photoUrl;
    private Long userId;
}
