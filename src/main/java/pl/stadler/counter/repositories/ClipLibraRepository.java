package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.stadler.counter.models.ClipLibra;

public interface ClipLibraRepository   extends JpaRepository<ClipLibra, Long> {
}
