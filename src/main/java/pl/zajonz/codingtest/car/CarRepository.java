package pl.zajonz.codingtest.car;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pl.zajonz.codingtest.car.model.Car;
import pl.zajonz.codingtest.common.FuelType;

import java.util.List;
import java.util.Optional;
public interface CarRepository extends JpaRepository<Car,Integer> {

    List<Car> findCarsByGarage_Id(int id);
    List<Car> findCarsByGarageIsNullAndFuelTypeIsNot(FuelType fuelType);
    List<Car> findCarsByGarageIsNull();
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Car> findWithLockingById(int id);

}
