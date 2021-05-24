package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.repositories.MeshRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/distance")
public class MeshService {
    private final MeshService meshServices;

    @Autowired
    public MeshService(MeshService meshServices) {
        this.meshServices = meshServices;
    }

    public List<Mesh> findAll() {
        return meshServices.findAll();
    }

    public Mesh findByColorMinMax(String color, String min, String max ) {
        return meshServices.findByColorMinMax(color, min, max);
    }
    public Mesh save(Mesh mesh){
        return meshServices.save(mesh);
    }
}
