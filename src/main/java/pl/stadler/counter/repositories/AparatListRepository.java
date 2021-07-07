package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.AparatList;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.KabelListSettings;

import java.util.List;
import java.util.Optional;

@Repository
public interface AparatListRepository  extends JpaRepository<AparatList, Long> {
    public List<AparatList> findAll();
    public List<AparatList> findAllByPosition(String position);
    @Query("SELECT a FROM AparatList a where a.area like concat('%', ?1, '%') and a.position like concat('%', ?2, '%')")
    public List<AparatList> findAllByAreaAndPosition(String area, String position);
    public List<AparatList> findAllByNumberProducer(String numberProducer);
    public List<AparatList> findAllByNameProducer(String nameProducer);
}
