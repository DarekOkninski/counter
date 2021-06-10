package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.ClipLibra;
import pl.stadler.counter.models.Mesh;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MeshRepository  extends JpaRepository<Mesh, Long> {
    List<Mesh> findAll();
    Optional<Mesh> findByNumberProducer(String numberProducer);
    Optional<Mesh> findByColorAndMinSizeAndMaxSize(String color, String min, String max);
    @Query("SELECT m FROM Mesh m where m.minSize <= ?1 and m.maxSize > ?1 and m.color like ?2 ")
    Optional<Mesh> findMesh(Integer size, String color);
}
