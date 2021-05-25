package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.services.MeshService;

import java.util.List;

@RestController
@RequestMapping(path = "/mesh")
public class MeshController {

    private final MeshService meshServices;

    @Autowired
    public MeshController(MeshService meshServices) {
        this.meshServices = meshServices;
    }

    @GetMapping(path = "/find-all")
    public List<Mesh> findAll() {
        return meshServices.findAll();
    }

    @GetMapping(path = "/find-by-color-min-max")
    public Mesh findByColorMinMax(String color, String min, String max ) {
        return meshServices.findByColorMinMax(color, min, max);
    }
    @GetMapping(path = "/save")
    public Mesh save(Mesh mesh){
        return meshServices.save(mesh);
    }
}
