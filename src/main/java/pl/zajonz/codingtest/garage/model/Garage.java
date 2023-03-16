package pl.zajonz.codingtest.garage.model;

import jakarta.persistence.*;
import lombok.*;
import pl.zajonz.codingtest.car.model.Car;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String address;
    private int capacity;
    private Boolean acceptLpg= false;
    @OneToMany(mappedBy = "garage")
    private Set<Car> carList;

    @Override
    public String toString() {
        return id + " " + address;
    }
}
