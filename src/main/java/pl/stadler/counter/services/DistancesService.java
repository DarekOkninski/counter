package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.Distances;
import pl.stadler.counter.models.IsolationsCable;
import pl.stadler.counter.repositories.DistancesRepository;

import java.util.List;

@Service
public class DistancesService {

    private final DistancesRepository distancesRepository;

    @Autowired
    public DistancesService(DistancesRepository distancesRepository) {
        this.distancesRepository = distancesRepository;
    }

    public List<Distances> findAll() {
        return distancesRepository.findAll();
    }

    public Distances findByNumberHarting(String numberHarting) {
        return distancesRepository.findByNumberHarting(numberHarting).orElse(null);
    }
    public Distances save(Distances distances){
        return distancesRepository.save(distances);
    }
}
