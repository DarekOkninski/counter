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
    private final IsolationsCableService isolationsCableService;
    private static LinkedHashSet<String> error = new LinkedHashSet<String>();
    private static LinkedHashSet<String> informations = new LinkedHashSet<String>();

    @Autowired
    public ClipLibraService(ClipLibraRepository clipLibraRepository, KabelListRepository kabelListRepository, AparatListRepository aparatListRepository, GripRepository gripRepository, IsolationsCableService isolationsCableService) {
        this.clipLibraRepository = clipLibraRepository;
        this.kabelListRepository = kabelListRepository;
        this.aparatListRepository = aparatListRepository;
        this.gripRepository = gripRepository;
        this.isolationsCableService = isolationsCableService;

    }

    public List<ClipLibra> findAll() {
        return clipLibraRepository.findAll();
    }

    public ClipLibra findByClipNumberStadlerID(String clipNumberStadlerID) {
        return clipLibraRepository.findByClipNumberStadlerID(clipNumberStadlerID).orElse(null);
    }
    public ClipLibra findBySize(float size) {
        return clipLibraRepository.findBySize(size).orElse(null);
    }

    public ClipLibra save(ClipLibra clipLibra) {
        return clipLibraRepository.save(clipLibra);
    }


    public Wrapper clipLibraCounter() {
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
                if (aparatListName.getKey().contains(kabelList.getPositionTo())) {
                    if (kabelList.getColor().toUpperCase().contains("SH")) {
                        //Sprawdzenie czy mamy informacje i tym połaczeniu w bazie
                        if (findSizeClip(kabelList) != null) {
                            //Dobranie odpowiedniego rozmiaru zacisku do przewodu i dodanie go do mapy z zapotrzebawaniem

                            int val = clipCounter.get(findByClipNumberStadlerID(findSizeClip(kabelList)).getClipNumberStadlerID());
                            clipCounter.put(findSizeClip(kabelList), val + 1);
                            informations.add(" Do kabla: " + kabelList.getNameCable() + " na gripa do kości: " + kabelList.getPositionTo() + " dobrano zacisk o numerze producenta: " + findSizeClip(kabelList));
                        }
                    }
                }
                //Sprawdzamy czy połączenie jest typu SH i czy idzie na gripa
                if (aparatListName.getKey().contains(kabelList.getPositionFrom())) {
                    if (kabelList.getColor().toUpperCase().contains("SH")) {
                        //Sprawdzenie czy mamy informacje i tym połaczeniu w bazie
                        if (findSizeClip(kabelList) != null) {
                            //Dobranie odpowiedniego rozmiaru zacisku do przewodu i dodanie go do mapy z zapotrzebawaniem
                            int val = clipCounter.get(findSizeClip(kabelList));
                            clipCounter.put(findSizeClip(kabelList), val + 1);
                            informations.add(" Do kabla: " + kabelList.getNameCable() + " na gripa do kości: " + kabelList.getPositionFrom() + " dobrano zacisk o numerze producenta: " + findSizeClip(kabelList));
                        }
                    }
                }
            }
        }

        Wrapper wrapper = Wrapper.builder()
                .finalScore(clipCounter)
                .errors(error)
                .informations(informations)
                .build();
        return wrapper;

    }

    public String findSizeClip(KabelList kabelList) {
        IsolationsCable isolationsCable = isolationsCableService.findByTypeIsolations(kabelList.getType1());
        if (isolationsCable != null) {
            //System.out.println(clipLibraRepository.findBySize(isolationsCable.getSrednicaWew()));
            if(findBySize(isolationsCable.getSrednicaWew()) != null){
                ClipLibra clipLibra = findBySize(isolationsCable.getSrednicaWew());
                return clipLibra.getClipNumberStadlerID();
            }else if(isolationsCable.getSrednicaWew() > 0.0){
                error.add("2## Brak zacisku o srednicy: " + isolationsCable.getSrednicaWew());

            }
            return null;
        } else {
            error.add("1## Brak przewodu o nazwie: " + kabelList.getType1());
            return null;
        }
    }
}

