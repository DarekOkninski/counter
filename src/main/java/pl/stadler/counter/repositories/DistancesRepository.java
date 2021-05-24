package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.stadler.counter.models.Distances;

public interface DistancesRepository   extends JpaRepository<Distances, Long> {
}
