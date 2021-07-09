package pl.stadler.counter.services;

import org.springframework.stereotype.Service;
import pl.stadler.counter.models.AparatList;
import pl.stadler.counter.models.BoneHarting;
import pl.stadler.counter.repositories.BoneHartingRepository;

import java.util.List;

@Service
public class BoneHartingService {

    private final BoneHartingRepository boneHartingRepository;

    public BoneHartingService(BoneHartingRepository boneHartingRepository) {
        this.boneHartingRepository = boneHartingRepository;
    }
    public List<BoneHarting> findAll() {
        return boneHartingRepository.findAll();
    }

    public BoneHarting findByNumberProducer(String numberProducer){
        return boneHartingRepository.findByNumberProducer(numberProducer).orElse(null);
    }

    public BoneHarting save(BoneHarting boneHarting){
        return boneHartingRepository.save(boneHarting);
    }

}
