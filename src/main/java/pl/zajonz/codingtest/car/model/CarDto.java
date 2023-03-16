package pl.zajonz.codingtest.car.model;

import lombok.Builder;
import lombok.Getter;
import pl.zajonz.codingtest.common.FuelType;

@Getter
@Builder
public class CarDto {

    private int id;
    private String brand;
    private String model;
    private FuelType fuelType;

    public static CarDto fromEntity(Car car){
        return CarDto.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .fuelType(car.getFuelType())
                .build();
    }
}
