package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.services.MeshService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/mesh")
public class MeshController {

    private final MeshService meshService;

    @Autowired
    public MeshController(MeshService meshServices) {
        this.meshService = meshServices;
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

//  Działa i dla ruplana i dla E3
    @GetMapping(path = "/group-map/{projectNumber}")
    public Map<Mesh, Float> groupMap(@PathVariable(value = "projectNumber") String projectNumber) throws IOException {
        return meshService.groupMap(projectNumber);
    }

}
