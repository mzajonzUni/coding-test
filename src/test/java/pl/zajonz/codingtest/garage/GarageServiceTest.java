package pl.zajonz.codingtest.garage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajonz.codingtest.garage.model.Garage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GarageServiceTest {

    @InjectMocks
    private GarageService garageService;
    @Mock
    private GarageRepository garageRepository;

    @Test
    void findAll() {
        //given
        Garage garage = Garage.builder()
                .id(1)
                .address("Test")
                .build();
        List<Garage> garageList = List.of(garage);

        when(garageRepository.findAll()).thenReturn(garageList);
        //when
        List<Garage> returned = garageService.findAll();

        //then
        assertEquals(garageList, returned);
    }
}