package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.stadler.counter.models.IsolationsCable;

public interface IsolationsCableRepository extends JpaRepository<IsolationsCable, Long> {
}
