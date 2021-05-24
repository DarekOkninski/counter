package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.stadler.counter.models.KabelList;

public interface KabelListRepository  extends JpaRepository<KabelList, Long> {
}
