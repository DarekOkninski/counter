package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.stadler.counter.models.IsolationsCable;

import java.util.List;
import java.util.Optional;

public interface IsolationsCableRepository extends JpaRepository<IsolationsCable, Long> {

    public List<IsolationsCable> findAll();
    public Optional<IsolationsCable> findByTypeIsolationsAndPrzekrojWew(String typeIsolations, String przekrojWew);


}
