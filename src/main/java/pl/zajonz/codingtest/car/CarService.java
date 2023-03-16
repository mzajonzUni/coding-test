package pl.zajonz.codingtest.car;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.codingtest.car.model.Car;
import pl.zajonz.codingtest.common.FuelType;
import pl.zajonz.codingtest.garage.GarageRepository;
import pl.zajonz.codingtest.garage.model.Garage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    private final GarageRepository garageRepository;

    public List<Car> findCarsByGarage_Id(int garageId) {
        return carRepository.findCarsByGarage_Id(garageId);
    }

    public List<Car> findCarsByGarageIsNull(int garageId) {
        Boolean acceptLpg = garageRepository.findById(garageId)
                .orElseThrow(() -> new EntityNotFoundException("Not found garage with id= " + garageId))
                .getAcceptLpg();
        if (!acceptLpg) {
            return carRepository.findCarsByGarageIsNullAndFuelTypeIsNot(FuelType.LPG);
        }
        return carRepository.findCarsByGarageIsNull();
    }

    @Transactional
    public void addToGarage(int carId, int garageId) {
        Garage garage = garageRepository.findWithLockingById(garageId)
                .orElseThrow(() -> new EntityNotFoundException("Not found garage with id= " + garageId));
        Car car = carRepository.findWithLockingById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Not found car with id= " + carId));
        if (garage.getCarList().size() >= garage.getCapacity()) {
            throw new IllegalArgumentException("Garage is full");
        }
        if (!garage.getAcceptLpg() && car.getFuelType() == FuelType.LPG) {
            throw new IllegalArgumentException("Car with LPG is not accepted");
        }
        car.setGarage(garage);
        carRepository.save(car);
    }

    public void deleteFromGarage(int garageId) {
        Car car = carRepository.findById(garageId)
                .orElseThrow(() -> new EntityNotFoundException("Not found car with id= " + garageId));
        car.setGarage(null);
        carRepository.save(car);
    }
}
