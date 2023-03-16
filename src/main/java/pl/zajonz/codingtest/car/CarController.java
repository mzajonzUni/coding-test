package pl.zajonz.codingtest.car;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.zajonz.codingtest.car.model.CarDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @GetMapping("/find/{id}")
    @ResponseBody
    public List<CarDto> findCarsByGarage_Id(@PathVariable int id) {
        return carService.findCarsByGarage_Id(id)
                .stream()
                .map(CarDto::fromEntity)
                .toList();
    }

    @GetMapping
    @ResponseBody
    public List<CarDto> findCarsByGarageIsNull(@RequestParam int garageId) {
        return carService.findCarsByGarageIsNull(garageId)
                .stream()
                .map(CarDto::fromEntity)
                .toList();
    }

    @PatchMapping("add/{id}")
    @ResponseBody
    public void addToGarage(@PathVariable int id, @RequestParam int garageId){
        carService.addToGarage(id, garageId);
    }

    @PatchMapping("delete/{id}")
    @ResponseBody
    public void deleteFromGarage(@PathVariable int id) {
        carService.deleteFromGarage(id);
    }

}
