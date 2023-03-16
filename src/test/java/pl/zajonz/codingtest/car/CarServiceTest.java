package pl.zajonz.codingtest.car;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajonz.codingtest.car.model.Car;
import pl.zajonz.codingtest.common.FuelType;
import pl.zajonz.codingtest.garage.model.Garage;
import pl.zajonz.codingtest.garage.GarageRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @InjectMocks
    private CarService carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private GarageRepository garageRepository;

    @Test
    void testFindCarsByGarage_Id_CorrectValues_ResultsInListCarBeingReturned() {
        //given
        Garage garage = Garage.builder()
                .id(1)
                .address("Test")
                .build();
        Car car = Car.builder()
                .id(1)
                .brand("Test")
                .garage(garage)
                .build();
        List<Car> carList = List.of(car);

        when(carRepository.findCarsByGarage_Id(anyInt())).thenReturn(carList);

        //when
        List<Car> returned = carService.findCarsByGarage_Id(1);

        //then
        assertEquals(carList, returned);
    }

    @Test
    void testFindCarsByGarageIsNull_CorrectValues_ResultsInListCarBeingReturned() {
        //given
        Garage garage = Garage.builder()
                .id(1)
                .address("Test")
                .acceptLpg(true)
                .build();
        Car car = Car.builder()
                .id(1)
                .brand("Test")
                .build();
        List<Car> carList = List.of(car);

        when(carRepository.findCarsByGarageIsNull()).thenReturn(carList);
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage));

        //when
        List<Car> returned = carService.findCarsByGarageIsNull(1);

        //then
        assertEquals(carList, returned);
    }

    @Test
    void testFindCarsByGarageIsNull_IncorrectGarageId_ResultsInEntityNotFoundException() {
        //given
        String exceptionMsg = "Not found garage with id= 1";

        when(garageRepository.findById(anyInt())).thenReturn(Optional.empty());

        //when //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> carService.findCarsByGarageIsNull(1));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testFindCarsByGarageIsNullAndFuelTypeIsNot_CorrectValues_ResultsInListCarBeingReturned() {
        //given
        Garage garage = Garage.builder()
                .id(1)
                .address("Test")
                .acceptLpg(false)
                .build();
        Car car = Car.builder()
                .id(1)
                .brand("Test")
                .fuelType(FuelType.LPG)
                .build();
        List<Car> carList = List.of(car);

        when(carRepository.findCarsByGarageIsNullAndFuelTypeIsNot(any(FuelType.class))).thenReturn(carList);
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage));

        //when
        List<Car> returned = carService.findCarsByGarageIsNull(1);

        //then
        assertEquals(carList, returned);
    }

    @Test
    void testAddToGarage_CorrectValues_ResultsInCarBeingSaved() {
        //given
        Garage garage = Garage.builder()
                .id(1)
                .capacity(10)
                .address("Test")
                .carList(new HashSet<>())
                .acceptLpg(false)
                .build();
        Car car = Car.builder()
                .id(1)
                .brand("Test")
                .fuelType(FuelType.GASOLINE)
                .build();

        when(carRepository.findWithLockingById(anyInt())).thenReturn(Optional.of(car));
        when(garageRepository.findWithLockingById(anyInt())).thenReturn(Optional.of(garage));

        //when
        carService.addToGarage(car.getId(), garage.getId());

        //then
        verify(carRepository).save(car);
    }

    @Test
    void testAddToGarage_IncorrectGarageId_ResultsInEntityNotFoundException() {
        //given
        String exceptionMsg = "Not found garage with id= 1";

        when(garageRepository.findWithLockingById(anyInt())).thenReturn(Optional.empty());

        //when //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> carService.addToGarage(1, 1));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testAddToGarage_IncorrectCarId_ResultsInEntityNotFoundException() {
        //given
        String exceptionMsg = "Not found car with id= 1";
        Garage garage = Garage.builder()
                .id(1)
                .capacity(10)
                .address("Test")
                .carList(new HashSet<>())
                .acceptLpg(false)
                .build();
        when(garageRepository.findWithLockingById(anyInt())).thenReturn(Optional.of(garage));
        when(carRepository.findWithLockingById(anyInt())).thenReturn(Optional.empty());

        //when //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> carService.addToGarage(1, 1));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testAddToGarage_CannotAdd_ResultsInIllegalArgumentException() {
        //given
        String exceptionMsg = "Garage is full";
        Garage garage = Garage.builder()
                .id(1)
                .capacity(0)
                .address("Test")
                .carList(new HashSet<>())
                .acceptLpg(false)
                .build();
        Car car = Car.builder()
                .id(1)
                .brand("Test")
                .fuelType(FuelType.GASOLINE)
                .build();

        when(carRepository.findWithLockingById(anyInt())).thenReturn(Optional.of(car));
        when(garageRepository.findWithLockingById(anyInt())).thenReturn(Optional.of(garage));


        //when //then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> carService.addToGarage(1, 1));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testAddToGarage_NotAcceptLpg_ResultsInIllegalArgumentException() {
        //given
        String exceptionMsg = "Car with LPG is not accepted";
        Garage garage = Garage.builder()
                .id(1)
                .capacity(1)
                .address("Test")
                .carList(new HashSet<>())
                .acceptLpg(false)
                .build();
        Car car = Car.builder()
                .id(1)
                .brand("Test")
                .fuelType(FuelType.LPG)
                .build();

        when(carRepository.findWithLockingById(anyInt())).thenReturn(Optional.of(car));
        when(garageRepository.findWithLockingById(anyInt())).thenReturn(Optional.of(garage));

        //when //then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> carService.addToGarage(1, 1));
        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testDeleteFromGarage_CorrectValues_ResultsInCarBeingDeleted() {
        //given
        Car car = Car.builder()
                .id(1)
                .brand("Test")
                .build();

        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));

        //when
        carService.deleteFromGarage(1);

        //then
        verify(carRepository).save(car);
    }

    @Test
    void testDeleteFromGarage_IncorrectCarId_ResultsInCarBeingDeleted() {
        //given
        String exceptionMsg = "Not found car with id= 1";

        when(carRepository.findById(anyInt())).thenReturn(Optional.empty());

        //when //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> carService.deleteFromGarage(1));
        assertEquals(exceptionMsg, exception.getMessage());
    }
}