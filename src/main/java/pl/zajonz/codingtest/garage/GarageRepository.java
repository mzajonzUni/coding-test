package pl.zajonz.codingtest.garage;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pl.zajonz.codingtest.garage.model.Garage;

import java.util.Optional;

public interface GarageRepository extends JpaRepository<Garage,Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Garage> findWithLockingById(int id);

}
