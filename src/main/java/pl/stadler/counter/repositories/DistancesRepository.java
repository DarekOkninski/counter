package pl.stadler.counter.repositories;

import org.springframework.data.geo.Distance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.Distances;
import pl.stadler.counter.models.IsolationsCable;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistancesRepository   extends JpaRepository<Distances, Long> {

    public List<Distances> findAll();
    public Optional<Distances> findByNumberHarting(String numberHarting);
}
