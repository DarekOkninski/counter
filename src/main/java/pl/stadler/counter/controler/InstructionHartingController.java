package pl.stadler.counter.controler;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;
import pl.stadler.counter.models.Project;
import pl.stadler.counter.models.ProjectSettings;
import pl.stadler.counter.models.Wrapper;
import pl.stadler.counter.services.InstructionHartingService;
import pl.stadler.counter.services.ProjectService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/instruction-harting")
public class InstructionHartingController {

    private final InstructionHartingService instructionHartingService;
    private final ProjectService projectService;
    Logger InstructionHartingController = LoggerFactory.getLogger(InstructionHartingService.class);

    public InstructionHartingController(InstructionHartingService instructionHartingService, ProjectService projectService) {
        this.instructionHartingService = instructionHartingService;

        this.projectService = projectService;
    }


    @PostMapping(path = "/save-instruction-harting-to-db")
    public Wrapper saveInstructionHartingToDB(@RequestBody Wrapper wrapper) {
        try {
            instructionHartingService.addInstructinHartingToDB(wrapper);
            InstructionHartingController.info("InstructionHartingController.saveInstructionHartingToDB() - Lista została dodana do DB");
        } catch (Exception ignored) {
            InstructionHartingController.error("InstructionHartingController.saveInstructionHartingToDB() - Błąd dodania listy do DB");
        }
        return wrapper;
    }

    @PostMapping(path = "/count-pins-harting")
    public Wrapper countPinsHarting(@RequestBody Project project) {
        Wrapper wrapper = new Wrapper();
        try {
            wrapper = instructionHartingService.countPinsHarting(project.getNumberProject());
            InstructionHartingController.info("InstructionHartingController.saveInstructionHartingToDB() - Lista została dodana do DB");
        } catch (Exception ignored) {
            InstructionHartingController.error("InstructionHartingController.saveInstructionHartingToDB() - Błąd dodania listy do DB");
        }
        return wrapper;
    }


    //@EventListener(ApplicationReadyEvent.class)
    public void asda() throws IOException, InvalidFormatException {
        instructionHartingService.deleteAll();
        Project project = projectService.findByNumberProject("L-4444");
        ProjectSettings projectSetting = new ProjectSettings();
        Wrapper wrapper = new Wrapper();
        wrapper.setProjectSettings(projectSetting);
        wrapper.getProjectSettings().setKabelListPath("L:\\05_KIEROWNICTWO\\07_Teamleader\\Okninski Dariusz\\L-4444 FNM Harting intern.xlsx");
        wrapper.getProjectSettings().setProject(project);

        saveInstructionHartingToDB(wrapper);
    }
    //@EventListener(ApplicationReadyEvent.class)
    public void testCount(){
        Project project = projectService.findByNumberProject("L-4444");
        countPinsHarting(project);
    }


}
