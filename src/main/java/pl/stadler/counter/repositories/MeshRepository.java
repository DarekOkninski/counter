package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.stadler.counter.models.ClipLibra;
import pl.stadler.counter.models.Mesh;

import java.util.List;
import java.util.Optional;

public interface MeshRepository  extends JpaRepository<Mesh, Long> {
    public List<Mesh> findAll();
    public Optional<Mesh> findByColorMinMax(String color, String min, String max);
}
