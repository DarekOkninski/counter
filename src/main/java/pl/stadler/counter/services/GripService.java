package pl.stadler.counter.services;

import org.springframework.stereotype.Service;
import pl.stadler.counter.models.Distances;
import pl.stadler.counter.models.Grip;
import pl.stadler.counter.repositories.GripRepository;

import java.util.List;

@Service
public class GripService {

    private final GripRepository gripRepository;

    public GripService(GripRepository gripRepository) {
        this.gripRepository = gripRepository;
    }

    public List<Grip> findAll() {
        return gripRepository.findAll();
    }

    public Grip findByNumberGrip(String numberGrip) {
        return gripRepository.findByNumberGrip(numberGrip).orElse(null);
    }

    public Grip save(Grip grip) {
        return gripRepository.save(grip);
    }
}
