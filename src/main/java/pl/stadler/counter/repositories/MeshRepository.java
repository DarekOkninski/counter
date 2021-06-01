package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.ClipLibra;
import pl.stadler.counter.models.Mesh;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MeshRepository  extends JpaRepository<Mesh, Long> {
    List<Mesh> findAll();
   Optional<Mesh> findByColorAndMinSizeAndMaxSize(String color, String min, String max);

}
