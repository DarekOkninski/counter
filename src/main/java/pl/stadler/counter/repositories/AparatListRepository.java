package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.AparatList;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.KabelListSettings;

import java.util.List;

@Repository
public interface AparatListRepository  extends JpaRepository<AparatList, Long> {
    public List<AparatList> findAll();
    public List<AparatList> findAllByPosition(String position);
    public List<AparatList> findAllByNumberProducer(String numberProducer);
    public List<AparatList> findAllByNameProducer(String nameProducer);
}
