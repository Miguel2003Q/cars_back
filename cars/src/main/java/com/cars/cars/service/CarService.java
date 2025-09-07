package com.cars.cars.service;

import com.cars.cars.dto.CarCreateDTO;
import com.cars.cars.dto.CarDTO;
import com.cars.cars.dto.CarUpdateDTO;
import com.cars.cars.entity.Car;
import com.cars.cars.entity.User;
import com.cars.cars.exception.NotFoundException;
import com.cars.cars.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    
    private final CarRepository carRepository;
    
    public List<CarDTO> getAllCarsByUser(User user) {
        List<Car> cars = carRepository.findByUserId(user.getId());
        return cars.stream()
                .map(this::convertToCarDTO)
                .collect(Collectors.toList());
    }
    
    public CarDTO getCarByIdAndUser(Long carId, User user) {
        Car car = carRepository.findByIdAndUserId(carId, user.getId())
                .orElseThrow(() -> new NotFoundException("Auto no encontrado"));
        return convertToCarDTO(car);
    }
    
    public CarDTO createCar(CarCreateDTO carCreateDTO, User user) {
        if (carRepository.existsByLicensePlate(carCreateDTO.getLicensePlate())) {
            throw new RuntimeException("La placa ya existe");
        }
        
        Car car = new Car();
        car.setBrand(carCreateDTO.getBrand());
        car.setModel(carCreateDTO.getModel());
        car.setYear(carCreateDTO.getYear());
        car.setLicensePlate(carCreateDTO.getLicensePlate());
        car.setColor(carCreateDTO.getColor());
        car.setPhotoUrl(carCreateDTO.getPhotoUrl());
        car.setUser(user);
        
        Car savedCar = carRepository.save(car);
        return convertToCarDTO(savedCar);
    }
    
    public CarDTO updateCar(Long carId, CarUpdateDTO carUpdateDTO, User user) {
        Car car = carRepository.findByIdAndUserId(carId, user.getId())
                .orElseThrow(() -> new NotFoundException("Auto no encontrado"));
        
        car.setBrand(carUpdateDTO.getBrand());
        car.setModel(carUpdateDTO.getModel());
        car.setYear(carUpdateDTO.getYear());
        car.setLicensePlate(carUpdateDTO.getLicensePlate());
        car.setColor(carUpdateDTO.getColor());
        car.setPhotoUrl(carUpdateDTO.getPhotoUrl());
        
        Car savedCar = carRepository.save(car);
        return convertToCarDTO(savedCar);
    }
    
    public void deleteCar(Long carId, User user) {
        Car car = carRepository.findByIdAndUserId(carId, user.getId())
                .orElseThrow(() -> new NotFoundException("Auto no encontrado"));
        carRepository.delete(car);
    }
    
    // Búsqueda por placa
    public List<CarDTO> searchCars(User user, String search) {
        List<Car> cars = carRepository.findByUserIdAndLicensePlateContainingIgnoreCase(user.getId(), search);
        return cars.stream()
                .map(this::convertToCarDTO)
                .collect(Collectors.toList());
    }
    
    // Filtrado por año
    public List<CarDTO> filterCarsByYear(User user, Integer year) {
        List<Car> cars = carRepository.findByUserIdAndYear(user.getId(), year);
        return cars.stream()
                .map(this::convertToCarDTO)
                .collect(Collectors.toList());
    }
    
    // Filtrado por marca
    public List<CarDTO> filterCarsByBrand(User user, String brand) {
        List<Car> cars = carRepository.findByUserIdAndBrand(user.getId(), brand);
        return cars.stream()
                .map(this::convertToCarDTO)
                .collect(Collectors.toList());
    }
    
    // Método auxiliar para convertir Car a CarDTO
    private CarDTO convertToCarDTO(Car car) {
        return new CarDTO(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getLicensePlate(),
                car.getColor(),
                car.getPhotoUrl(),
                car.getUser().getId()
        );
    }
}
