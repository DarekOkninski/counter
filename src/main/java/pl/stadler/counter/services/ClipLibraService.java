package pl.stadler.counter.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.ClipLibra;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.repositories.ClipLibraRepository;
import pl.stadler.counter.repositories.KabelListRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClipLibraService {

    private final ClipLibraRepository clipLibraRepository;
    private final KabelListRepository kabelListRepository;

    @Autowired
    public ClipLibraService(ClipLibraRepository clipLibraRepository, KabelListRepository kabelListRepository) {
        this.clipLibraRepository = clipLibraRepository;
        this.kabelListRepository = kabelListRepository;
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


   // @EventListener(ApplicationReadyEvent.class)
    public void clipLibraCounter(){
        List<KabelList> kabelLists= kabelListRepository.findAll();
        Map<String, Integer> clipCounter = new HashMap<>();
        List<ClipLibra> nameClip = findAll();

        nameClip.forEach(x -> clipCounter.put(x.getNameClip(), 0));
        kabelLists.forEach(x -> {
            //System.out.println(clipCounter.containsKey(x.getType1()));

            if(clipCounter.containsKey(x.getType1())){
                clipCounter.put(x.getType1(),clipCounter.get(x.getType1())+1);
            }
        });

        clipCounter.forEach((key, value)-> System.out.println(key +" -- " + value));

    }

}
