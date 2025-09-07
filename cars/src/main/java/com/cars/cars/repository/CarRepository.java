package com.cars.cars.repository;

import com.cars.cars.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    
    List<Car> findByUserId(Long userId);
    
    Optional<Car> findByIdAndUserId(Long id, Long userId);
    
    boolean existsByLicensePlate(String licensePlate);
    
    // Búsqueda por placa
    List<Car> findByUserIdAndLicensePlateContainingIgnoreCase(Long userId, String licensePlate);
    
    // Filtrado por año
    List<Car> findByUserIdAndYear(Long userId, Integer year);
    
    // Filtrado por marca
    List<Car> findByUserIdAndBrand(Long userId,String brand);
}
