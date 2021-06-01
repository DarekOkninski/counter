package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.KabelListSettings;
import pl.stadler.counter.models.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface KabelListSettingsRepository   extends JpaRepository<KabelListSettings, Long> {
    public List<KabelListSettings> findAll();
    public Optional<KabelListSettings> findByProjectNumberProject(String numberProject);



}
