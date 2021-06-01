package pl.stadler.counter.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.KabelListSettings;
import pl.stadler.counter.services.KabelListSettingsService;

import java.util.List;

@RestController
@RequestMapping(path = "/kabel-list-settings")
public class KabelListSettingsController {

    private final KabelListSettingsService kabelListSettingsService;

    public KabelListSettingsController(KabelListSettingsService kabelListSettingsService) {
        this.kabelListSettingsService = kabelListSettingsService;
    }

    @GetMapping(path = "/find-all")
    public List<KabelListSettings> findAll(){
        return kabelListSettingsService.findAll();
    }

    @GetMapping(path = "/find-by-project")
    public KabelListSettings findByProjectNumberProject(String numberProject){
        return kabelListSettingsService.findByProjectNumberProject(numberProject);
    }

    @PostMapping(path = "/save")
    public KabelListSettings save(KabelListSettings kabelListSettings){
        return kabelListSettingsService.save(kabelListSettings);
    }
}
