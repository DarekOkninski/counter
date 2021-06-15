package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.IsolationsCable;

import java.util.List;
import java.util.Optional;

@Repository
public interface IsolationsCableRepository extends JpaRepository<IsolationsCable, Long> {

    public List<IsolationsCable> findAll();
    public Optional<IsolationsCable> findByTypeIsolationsAndPrzekrojWew(String typeIsolations, String przekrojWew);
    public IsolationsCable findByTypeIsolations(String typeIsolations);

}
