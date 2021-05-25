package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.KabelList;

@Repository
public interface KabelListRepository  extends JpaRepository<KabelList, Long> {
}
