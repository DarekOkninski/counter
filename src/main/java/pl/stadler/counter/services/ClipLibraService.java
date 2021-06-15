package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.*;
import pl.stadler.counter.repositories.*;

import java.util.*;

@Service
public class ClipLibraService {

    private final ClipLibraRepository clipLibraRepository;
    private final KabelListRepository kabelListRepository;
    private final AparatListRepository aparatListRepository;
    private final GripRepository gripRepository;
    private final IsolationsCableRepository isolationsCableRepository;

    @Autowired
    public ClipLibraService(ClipLibraRepository clipLibraRepository, KabelListRepository kabelListRepository, AparatListRepository aparatListRepository, GripRepository gripRepository, IsolationsCableRepository isolationsCableRepository) {
        this.clipLibraRepository = clipLibraRepository;
        this.kabelListRepository = kabelListRepository;
        this.aparatListRepository = aparatListRepository;
        this.gripRepository = gripRepository;
        this.isolationsCableRepository = isolationsCableRepository;
    }

    public List<ClipLibra> findAll() {
        return clipLibraRepository.findAll();
    }

    public ClipLibra findByClipNumberStadlerID(String clipNumberStadlerID) {
        return clipLibraRepository.findByClipNumberStadlerID(clipNumberStadlerID).orElse(null);
    }
    public ClipLibra save(ClipLibra clipLibra){
        return clipLibraRepository.save(clipLibra);
    }



    public Map<String, Integer> clipLibraCounter(){
        List<KabelList> kabelLists= kabelListRepository.findAll();
        List<AparatList> aparatLists = new ArrayList<>();
        Map<String, String> aparatListsName = new HashMap<>();
        List<Grip> gripList= gripRepository.findAll();

        Map<String, Integer> clipCounter = new HashMap<>();


        List<ClipLibra> nameClip = findAll();

       nameClip.forEach(x -> clipCounter.put(x.getClipNumberStadlerID(), 0));

        for (Grip grip : gripList) {
//
            if(aparatListRepository.findAllByNumberProducer(grip.getNumberGrip()) != null){
               aparatLists.addAll(aparatListRepository.findAllByNumberProducer(grip.getNumberGrip()));
           }

        }
        for (AparatList aparatList : aparatLists) {
            aparatListsName.put(aparatList.getPosition(), aparatList.getNumberProducer());
        }

        for (KabelList kabelList : kabelLists) {
            for (Map.Entry<String, String> aparatListName : aparatListsName.entrySet()) {
                if(aparatListName.getKey().equals(kabelList.getPinTo())){
                    if(kabelList.getColor().toUpperCase().equals("SH")){
                       if(!findSizeClip(kabelList).contains("Brak przewodu o nazwie: ")){

                           int val = clipCounter.get(findByClipNumberStadlerID(findSizeClip(kabelList)).getClipNumberStadlerID());
                           clipCounter.put(findSizeClip(kabelList), val + 1);
                       }else{
                           System.out.println(findSizeClip(kabelList));
                       }
                    }
                }
                if(aparatListName.getKey().equals(kabelList.getPinFrom())){
                    if(kabelList.getColor().toUpperCase().equals("SH")){
                        if(!findSizeClip(kabelList).contains("Brak przewodu o nazwie: ")){
                            int val = clipCounter.get(findSizeClip(kabelList));
                            clipCounter.put(findSizeClip(kabelList), val + 1);
                        }else{
                            System.out.println(findSizeClip(kabelList));
                        }
                    }
                }
            }
        }
        return clipCounter;

    }

    public String findSizeClip(KabelList kabelList){
        IsolationsCable isolationsCable = isolationsCableRepository.findByTypeIsolations(kabelList.getType1());
        if(isolationsCable != null){
            //System.out.println(clipLibraRepository.findBySize(isolationsCable.getSrednicaWew()));
            ClipLibra clipLibra = clipLibraRepository.findBySize(isolationsCable.getSrednicaWew());
            return clipLibra.getClipNumberStadlerID();
        }else{
            return "Brak przewodu o nazwie: " + kabelList.getType1();
        }
    }
}

