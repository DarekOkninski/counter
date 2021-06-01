package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


//    @GetMapping(path = "/strang-map")
//    public Map<String, List<KabelList>> strangMap(){
//
//        return meshService.strangMap();
//    }
    @GetMapping(path = "/group-map")
    public List<Object[]> groupMap() throws IOException {
        String userName = System.getProperty("user.name");
        return meshService.groupMap("C://Users//" +userName+ "//Desktop//strangGroup.csv");
    }
}
