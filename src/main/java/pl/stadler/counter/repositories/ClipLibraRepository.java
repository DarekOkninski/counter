package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.stadler.counter.models.ClipLibra;
import pl.stadler.counter.models.Distances;

import java.util.List;
import java.util.Optional;

public interface ClipLibraRepository   extends JpaRepository<ClipLibra, Long> {
    public List<ClipLibra> findAll();
    public Optional<ClipLibra> findByNameClip(String nameClip);
}
