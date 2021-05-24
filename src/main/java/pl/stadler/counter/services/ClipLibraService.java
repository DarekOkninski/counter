package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.ClipLibra;
import pl.stadler.counter.models.Distances;
import pl.stadler.counter.repositories.ClipLibraRepository;
import pl.stadler.counter.repositories.DistancesRepository;

import java.util.List;

@Service
public class ClipLibraService {

    private final ClipLibraRepository clipLibraRepository;

    @Autowired
    public ClipLibraService(ClipLibraRepository clipLibraRepository) {
        this.clipLibraRepository = clipLibraRepository;
    }

    public List<ClipLibra> findAll() {
        return clipLibraRepository.findAll();
    }

    public ClipLibra findByNameClip(String nameClip) {
        return clipLibraRepository.findByNameClip(nameClip).orElse(null);
    }
    public ClipLibra save(ClipLibra clipLibra){
        return clipLibraRepository.save(clipLibra);
    }
}
