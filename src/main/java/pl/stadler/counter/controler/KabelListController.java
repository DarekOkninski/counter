package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.ProjectSettings;
import pl.stadler.counter.services.KabelListService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/kabelList")
public class KabelListController {

    private final KabelListService kabelListService;

    @Autowired
    public KabelListController(KabelListService kabelListService) {
        this.kabelListService = kabelListService;

    }


    @GetMapping(path = "/find-all")
    public List<KabelList> findAll() {
        return kabelListService.findAll();
    }

    @GetMapping(path = "/find-by-strang")
    public List<KabelList> findAllByStrang(String strang) {
        return kabelListService.findAllByStrang(strang);
    }

    @PostMapping(path = "/save")
    public KabelList save(KabelList kabelList) {
        return kabelListService.save(kabelList);
    }
    @GetMapping(path = "/mesh")
    public List<Object[]> mesh() {
        return kabelListService.mesh();
    }


//działa e3 na xmlx i ruplan xmlx oraz csv
    @PostMapping(path = "/add-kabelList")
    public ProjectSettings addKabelListE3(@RequestBody ProjectSettings projectSettings) throws IOException {

        try {
            kabelListService.addKabelList(projectSettings);
        } catch (Exception e){
            return null;
        }
        return projectSettings;

    }
// Test
//    @EventListener(ApplicationReadyEvent.class)
    public void aaa() throws IOException {
        ProjectSettings projectSettings = new ProjectSettings("L-4444", "C:\\Users\\okndar\\Desktop\\L-4444_GESAMTKABELLISTE.csv");
        kabelListService.addKabelList(projectSettings);
    }


}
