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

    public ClipLibra save(ClipLibra clipLibra) {
        return clipLibraRepository.save(clipLibra);
    }


    public Map<String, Integer> clipLibraCounter() {
        List<KabelList> kabelLists = kabelListRepository.findAll();
        List<AparatList> aparatLists = new ArrayList<>();
        Map<String, String> aparatListsName = new HashMap<>();
        List<Grip> gripList = gripRepository.findAll();

        Map<String, Integer> clipCounter = new HashMap<>();

        List<ClipLibra> nameClip = findAll();
        // tworzenie mapy z rodzajami  Clipów i liczka ich zapotrzebowan
        nameClip.forEach(x -> clipCounter.put(x.getClipNumberStadlerID(), 0));

        for (Grip grip : gripList) {
            // wyszukiwanie listy kości w których znajduja sie też gripy
            if (aparatListRepository.findAllByNumberProducer(grip.getNumberGrip()) != null) {
                aparatLists.addAll(aparatListRepository.findAllByNumberProducer(grip.getNumberGrip()));
            }
        }

        // tworzenie mapy (pozycja - numer producenta)
        for (AparatList aparatList : aparatLists) {
            aparatListsName.put(aparatList.getPosition(), aparatList.getNumberProducer());
        }

        // przechodzimy po całej kabelliscie
        for (KabelList kabelList : kabelLists) {
            for (Map.Entry<String, String> aparatListName : aparatListsName.entrySet()) {
                //Sprawdzamy czy połączenie jest typu SH i czy idzie na gripa
                if (aparatListName.getKey().equals(kabelList.getPinTo())) {
                    if (kabelList.getColor().toUpperCase().equals("SH")) {
                        //Sprawdzenie czy mamy informacje i tym połaczeniu w bazie
                        if (!findSizeClip(kabelList).contains("Brak przewodu o nazwie: ")) {
                            //Dobranie odpowiedniego rozmiaru zacisku do przewodu i dodanie go do mapy z zapotrzebawaniem
                            int val = clipCounter.get(findByClipNumberStadlerID(findSizeClip(kabelList)).getClipNumberStadlerID());
                            clipCounter.put(findSizeClip(kabelList), val + 1);
                        } else {
                            System.out.println(findSizeClip(kabelList));
                        }
                    }
                }
                //Sprawdzamy czy połączenie jest typu SH i czy idzie na gripa
                if (aparatListName.getKey().equals(kabelList.getPinFrom())) {
                    if (kabelList.getColor().toUpperCase().equals("SH")) {
                        //Sprawdzenie czy mamy informacje i tym połaczeniu w bazie
                        if (!findSizeClip(kabelList).contains("Brak przewodu o nazwie: ")) {
                            //Dobranie odpowiedniego rozmiaru zacisku do przewodu i dodanie go do mapy z zapotrzebawaniem
                            int val = clipCounter.get(findSizeClip(kabelList));
                            clipCounter.put(findSizeClip(kabelList), val + 1);
                        } else {
                            System.out.println(findSizeClip(kabelList));
                        }
                    }
                }
            }
        }
        return clipCounter;

    }

    public String findSizeClip(KabelList kabelList) {
        IsolationsCable isolationsCable = isolationsCableRepository.findByTypeIsolations(kabelList.getType1());
        if (isolationsCable != null) {
            //System.out.println(clipLibraRepository.findBySize(isolationsCable.getSrednicaWew()));
            ClipLibra clipLibra = clipLibraRepository.findBySize(isolationsCable.getSrednicaWew());
            return clipLibra.getClipNumberStadlerID();
        } else {
            return "Brak przewodu o nazwie: " + kabelList.getType1();
        }
    }
}

