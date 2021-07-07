package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.models.Wrapper;
import pl.stadler.counter.models.Project;
import pl.stadler.counter.services.MeshService;

import java.io.IOException;
import java.util.ArrayList;
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

    @PostMapping(path = "/group-map")
    public List<Wrapper> groupMap(@RequestBody Project project) throws IOException {
        String userName = System.getProperty("user.name");
        Map<Mesh, Float> m = meshService.groupMap(project.getNumberProject());

        List<Wrapper> wrapperList = new ArrayList<>();

        m.forEach((k,v) -> {
            Wrapper wrapper = Wrapper.builder()
                    .mesh(k)
                    .amountFloat(v)
                    .build();
            wrapperList.add(wrapper);
        });

        return wrapperList;
    }
}
