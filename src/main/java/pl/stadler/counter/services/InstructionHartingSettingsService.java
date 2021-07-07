package pl.stadler.counter.services;

import org.springframework.stereotype.Service;
import pl.stadler.counter.models.InstructionHarting;
import pl.stadler.counter.models.InstructionHartingSettings;
import pl.stadler.counter.models.Project;
import pl.stadler.counter.repositories.InstructionHartingSettingsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InstructionHartingSettingsService {

    private final InstructionHartingSettingsRepository instructionHartingSettingsRepository;

    public InstructionHartingSettingsService(InstructionHartingSettingsRepository instructionHartingSettingsRepository) {
        this.instructionHartingSettingsRepository = instructionHartingSettingsRepository;
    }

    public List<InstructionHartingSettings> findAll(){
        return instructionHartingSettingsRepository.findAll();
    }

    public InstructionHartingSettings findByProject(Project project){
        return instructionHartingSettingsRepository.findByProject(project).orElse(null);
    }

    public InstructionHartingSettings save(InstructionHartingSettings instructionHartingSettings){
        return instructionHartingSettingsRepository.save(instructionHartingSettings);
    }
    public void deleteAll(){
        instructionHartingSettingsRepository.deleteAll();
    }


}
