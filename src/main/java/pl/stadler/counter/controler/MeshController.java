package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.repositories.MeshRepository;

import java.util.List;

public class MeshController {
    private final MeshRepository meshRepository;

    @Autowired
    public MeshService(MeshRepository meshRepository) {
        this.meshRepository = meshRepository;
    }

    public List<Mesh> findAll() {
        return meshRepository.findAll();
    }

    public Mesh findByColorMinMax(String color, String min, String max ) {
        return meshRepository.findByColorMinMax(color, min, max).orElse(null);
    }
    public Mesh save(Mesh mesh){
        return meshRepository.save(mesh);
    }
}
