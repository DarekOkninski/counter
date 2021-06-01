package pl.stadler.counter.services;

import org.springframework.stereotype.Service;
import pl.stadler.counter.models.AparatListSettings;
import pl.stadler.counter.repositories.AparatListSettingsRepository;

import java.util.List;

@Service
public class AparatListSettingsService {
    private final AparatListSettingsRepository aparatListSettingsRepository;

    public AparatListSettingsService(AparatListSettingsRepository aparatListSettingsRepository) {
        this.aparatListSettingsRepository = aparatListSettingsRepository;
    }


    public List<AparatListSettings> findAll() {
        return aparatListSettingsRepository.findAll();
    }
    public AparatListSettings findByProjectNumberProject(String numberProject){
        return aparatListSettingsRepository.findByProjectNumberProject(numberProject).orElse(null);
    }
    public AparatListSettings save(AparatListSettings aparatListSettings){
        return aparatListSettingsRepository.save(aparatListSettings);
    }

}
