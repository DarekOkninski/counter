package pl.stadler.counter.controler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.models.Wrapper;
import pl.stadler.counter.models.Project;
import pl.stadler.counter.services.InstructionHartingService;
import pl.stadler.counter.services.MeshService;
import pl.stadler.counter.services.ProjectService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/mesh")
public class MeshController {

    private final MeshService meshService;
    private final ProjectService projectService;
    Logger MeshController = LoggerFactory.getLogger(InstructionHartingService.class);
    @Autowired
    public MeshController(MeshService meshServices, ProjectService projectService) {
        this.meshService = meshServices;
        this.projectService = projectService;
    }

    @GetMapping(path = "/find-all")
    public List<Mesh> findAll() {
        return meshService.findAll();
    }

    @GetMapping(path = "/find-by-color-min-max")
    public Mesh findByColorMinMax(String color, String min, String max ) {
        return meshService.findByColorMinMax(color, min, max);
    }
    @PostMapping(path = "/save")
    public Mesh save(Mesh mesh){
        return meshService.save(mesh);
    }

//    @PostMapping(path = "/group-map")
//    public List<Wrapper> groupMap(@RequestBody Project project) throws IOException {
//        String userName = System.getProperty("user.name");
//        Map<Mesh, Float> m = meshService.groupMap(project.getNumberProject());
//
//        List<Wrapper> wrapperList = new ArrayList<>();
//
//        m.forEach((k,v) -> {
//            Wrapper wrapper = Wrapper.builder()
//                    .mesh(k)
//                    .amountFloat(v)
//                    .build();
//            wrapperList.add(wrapper);
//        });
//
//        return wrapperList;
//    }

    @PostMapping(path = "/group-map")
    public Wrapper groupMap(@RequestBody Project project){
        Wrapper wrapper = new Wrapper();
        try {
            wrapper = meshService.groupMap(project.getNumberProject());
            MeshController.info("MeshController.groupMap() - Siatka obliczona poprawnie");
        } catch (Exception ignored) {
            MeshController.error("MeshController.groupMap() - Błąd podczas obliczania siatki");
        }
        return wrapper;

    }

    //@EventListener(ApplicationReadyEvent.class)
    public void testGroupMap(){
        Project project = projectService.findByNumberProject("L-4444");
        System.out.println(groupMap(project));
    }
}
