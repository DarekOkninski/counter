package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.InstructionHarting;
import pl.stadler.counter.models.InstructionHartingSettings;
import pl.stadler.counter.models.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructionHartingSettingsRepository extends JpaRepository<InstructionHartingSettings, Long> {

    public List<InstructionHartingSettings> findAll();
    public void deleteAll();
    public Optional<InstructionHartingSettings> findByProject(Project proect);
}
