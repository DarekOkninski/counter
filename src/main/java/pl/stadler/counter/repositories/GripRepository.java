package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.Grip;

import java.util.List;
import java.util.Optional;

@Repository
public interface GripRepository extends JpaRepository<Grip, Long> {

    public List<Grip> findAll();
    public Optional<Grip> findByNumberGrip(String numberGrip);
}
