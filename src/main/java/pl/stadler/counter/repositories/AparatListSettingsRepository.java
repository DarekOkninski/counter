package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.AparatList;
import pl.stadler.counter.models.AparatListSettings;
import pl.stadler.counter.models.KabelListSettings;

import java.util.List;
import java.util.Optional;

@Repository
public interface AparatListSettingsRepository  extends JpaRepository<AparatListSettings, Long>{
    public List<AparatListSettings> findAll();
    public Optional<AparatListSettings> findByProjectNumberProject(String numberProject);
}
