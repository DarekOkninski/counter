package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.repositories.MeshRepository;

import java.util.List;

@Service
public class MeshService {

    private MeshRepository meshRepository;

    @Autowired
    public MeshService(MeshRepository meshRepository) {
        this.meshRepository = meshRepository;
    }

    public List<Mesh> findAll() {
        return meshRepository.findAll();
    }

    public Mesh findByColorMinMax(String color, String min, String max ) {
        return meshRepository.findByColorAndMinSizeAndMaxSize(color, min, max).orElse(null);
    }
    public Mesh save(Mesh mesh){
        return meshRepository.save(mesh);
    }
}
