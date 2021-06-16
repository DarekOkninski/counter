package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.TermoTube;

import java.util.List;
import java.util.Optional;

@Repository
public interface TermoTubeRepository extends JpaRepository<TermoTube, Long>{
    List<TermoTube> findAll();
    List<TermoTube> findAllByColor(String color);


    @Query("SELECT m FROM TermoTube m where m.sizeMin <= ?1 and m.sizeMax > ?1 and color like ?2")
    Optional<TermoTube> findBySize(Float size, String color);
}
