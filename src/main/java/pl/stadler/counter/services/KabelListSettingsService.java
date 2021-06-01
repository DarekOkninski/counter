package pl.stadler.counter.services;

import org.springframework.stereotype.Service;
import pl.stadler.counter.models.KabelListSettings;
import pl.stadler.counter.repositories.KabelListSettingsRepository;

import java.util.List;

@Service
public class KabelListSettingsService {

    private final KabelListSettingsRepository kabelListSettingsRepository;

    public KabelListSettingsService(KabelListSettingsRepository kabelListSettingsRepository) {
        this.kabelListSettingsRepository = kabelListSettingsRepository;
    }

    public List<KabelListSettings> findAll(){
        return kabelListSettingsRepository.findAll();
    }

    public KabelListSettings findByProjectNumberProject(String numberProject){
        return kabelListSettingsRepository.findByProjectNumberProject(numberProject).orElse(null);
    }

    public KabelListSettings save(KabelListSettings kabelListSettings){
        return kabelListSettingsRepository.save(kabelListSettings);
    }
}
