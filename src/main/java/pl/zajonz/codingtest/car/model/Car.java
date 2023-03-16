package pl.zajonz.codingtest.car.model;

import jakarta.persistence.*;
import lombok.*;
import pl.zajonz.codingtest.common.FuelType;
import pl.zajonz.codingtest.garage.model.Garage;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String brand;
    private String model;
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    @ManyToOne
    @JoinColumn(name="garage_id")
    private Garage garage;

}
