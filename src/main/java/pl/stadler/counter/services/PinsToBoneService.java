package pl.stadler.counter.services;

import org.springframework.stereotype.Service;
import pl.stadler.counter.models.BoneHarting;
import pl.stadler.counter.models.PinsToBone;
import pl.stadler.counter.repositories.BoneHartingRepository;
import pl.stadler.counter.repositories.PinsToBoneRepository;

import java.util.List;

@Service
public class PinsToBoneService {

    private final PinsToBoneRepository pinsToBoneRepository;

    public PinsToBoneService(PinsToBoneRepository pinsToBoneRepository) {
        this.pinsToBoneRepository = pinsToBoneRepository;
    }
    public List<PinsToBone> findAll() {
        return pinsToBoneRepository.findAll();
    }
    public List<PinsToBone> findAllByBoneHartingId(Long id){ return pinsToBoneRepository.findAllByBoneHartingId(id);}
    public PinsToBone save(PinsToBone pinsToBone){
        return pinsToBoneRepository.save(pinsToBone);
    }
}
