package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.stadler.counter.models.Mesh;

public interface MeshRepository  extends JpaRepository<Mesh, Long> {
}
