package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.IsolationsCable;
import pl.stadler.counter.models.KabelList;

import java.util.List;
import java.util.Optional;

@Repository
public interface KabelListRepository  extends JpaRepository<KabelList, Long> {
    public List<KabelList> findAll();
    public Optional<KabelList> findByPotential(String potential);

}
