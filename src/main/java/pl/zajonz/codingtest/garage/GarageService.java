package pl.zajonz.codingtest.garage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajonz.codingtest.garage.model.Garage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GarageService {

    private final GarageRepository garageRepository;

    public List<Garage> findAll() {
        return garageRepository.findAll();
    }
}
