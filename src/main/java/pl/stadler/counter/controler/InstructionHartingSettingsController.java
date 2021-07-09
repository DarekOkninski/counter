package pl.stadler.counter.controler;

import pl.stadler.counter.models.InstructionHartingSettings;
import pl.stadler.counter.models.Project;
import pl.stadler.counter.repositories.InstructionHartingSettingsRepository;
import pl.stadler.counter.services.InstructionHartingSettingsService;

import java.util.List;

public class InstructionHartingSettingsController {

    private final InstructionHartingSettingsService instructionHartingSettingsService;

    public InstructionHartingSettingsController(InstructionHartingSettingsService instructionHartingSettingsService) {
        this.instructionHartingSettingsService = instructionHartingSettingsService;
    }

    public List<InstructionHartingSettings> findAll(){
        return instructionHartingSettingsService.findAll();
    }

    public InstructionHartingSettings findByProject(Project project){
        return instructionHartingSettingsService.findByProject(project);
    }

    public InstructionHartingSettings save(InstructionHartingSettings instructionHartingSettings){
        return instructionHartingSettingsService.save(instructionHartingSettings);
    }
    public void deleteAll(){
        instructionHartingSettingsService.deleteAll();
    }

}
