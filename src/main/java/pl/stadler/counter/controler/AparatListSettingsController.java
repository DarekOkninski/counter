package pl.stadler.counter.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.AparatListSettings;
import pl.stadler.counter.services.AparatListSettingsService;

import java.util.List;

@RestController
@RequestMapping(path = "/aparat-list-settings")
public class AparatListSettingsController {

    private final AparatListSettingsService aparatListSettingsService;

    public AparatListSettingsController(AparatListSettingsService aparatListSettingsService) {
        this.aparatListSettingsService = aparatListSettingsService;
    }

    @GetMapping(path = "/find-all")
    public List<AparatListSettings> findAll() {
        return aparatListSettingsService.findAll();
    }

    @GetMapping(path = "/find-by-project")
    public AparatListSettings findByProjectNumberProject(String numberProject) {
        return aparatListSettingsService.findByProjectNumberProject(numberProject);
    }

    @PostMapping(path = "/save")
    public AparatListSettings save(AparatListSettings aparatListSettings) {
        return aparatListSettingsService.save(aparatListSettings);
    }
}
