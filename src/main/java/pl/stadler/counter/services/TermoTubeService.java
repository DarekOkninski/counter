package pl.stadler.counter.services;

import org.springframework.stereotype.Service;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.models.TermoTube;
import pl.stadler.counter.repositories.KabelListRepository;
import pl.stadler.counter.repositories.TermoTubeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TermoTubeService {


    private final TermoTubeRepository termoTubeRepository;
    private final KabelListRepository kabelListRepository;
    private final ProjectService projectService;
    private final IsolationsCableService isolationsCableService;

    public TermoTubeService(TermoTubeRepository termoTubeRepository, KabelListRepository kabelListRepository, ProjectService projectService, IsolationsCableService isolationsCableService) {
        this.termoTubeRepository = termoTubeRepository;
        this.kabelListRepository = kabelListRepository;
        this.projectService = projectService;
        this.isolationsCableService = isolationsCableService;
    }

    public List<TermoTube> findAll() {
        return termoTubeRepository.findAll();
    }


    public TermoTube save(TermoTube termoTube){
        return termoTubeRepository.save(termoTube);
    }

    public TermoTube findBySize(Float size, String color) {
        return termoTubeRepository.findBySize(size, color).orElse(null);
    }
    List<TermoTube> findAllByColor(String color) {
        return termoTubeRepository.findAllByColor(color);
    }
    public Map<TermoTube, Integer> countTermoTubeMultiWire(String NumberProject){

        if(projectService.findByNumberProject(NumberProject).getTyp().equals("E3")){
            return groupTermoTubeMultiWireE3();
        }else{
            return groupTermoTubeMultiWireRuplan();
        }

    }

    public Map<TermoTube, Integer> groupTermoTubeMultiWireE3(){
        Map<TermoTube, Integer> finalScore = new HashMap<>();
        Map<String, Float> groupNrW = new HashMap<>();

        List<KabelList> kabelLists= kabelListRepository.findAll();
        System.out.println(kabelLists.size());
        for (KabelList kabelList : kabelLists) {
            if(!kabelList.getNameCable().equals("") && !kabelList.getPrzekrojZyly().toUpperCase().contains("BR")){
                if(!kabelList.getPotential().toUpperCase().contains("BR") && !kabelList.getPotential().toUpperCase().contains("CU")){
                    groupNrW.put(kabelList.getNameCable(), 0.0F);
                }

            }
        }
        return countTermoTubeMultiWire(groupNrW);
    }

    public Map<TermoTube, Integer> groupTermoTubeMultiWireRuplan(){
        Map<TermoTube, Integer> finalScore = new HashMap<>();
        Map<String, Float> groupNrW = new HashMap<>();

        List<KabelList> kabelLists= kabelListRepository.findAll();
        System.out.println(kabelLists.size());
        for (KabelList kabelList : kabelLists) {
            if(kabelList.getNameCable().contains("-W")){
                    groupNrW.put(kabelList.getNameCable(), 0.0F);
            }
        }
        return countTermoTubeMultiWire(groupNrW);
    }


    public Map<TermoTube, Integer> countTermoTubeMultiWire(Map<String, Float> groupNrW){
        Map<TermoTube, Integer> finalScore = new HashMap<>();
        for(var TermoTube : this.findAllByColor("Czarna")){
            finalScore.put(TermoTube, 0);
        }
        groupNrW.forEach((key, value) -> {
            if(isolationsCableService.findByTypeIsolationsAndPrzekrojWew(kabelListRepository.findAllByNameCable(key).get(0).getType1(), kabelListRepository.findAllByNameCable(key).get(0).getType2()) ==null){
                if(kabelListRepository.findAllByNameCable(key).get(0).getType2().toUpperCase().contains("SH") && kabelListRepository.findAllByNameCable(key).size() > 1){
                    if(isolationsCableService.findByTypeIsolationsAndPrzekrojWew(kabelListRepository.findAllByNameCable(key).get(1).getType1(), kabelListRepository.findAllByNameCable(key).get(1).getType2()) ==null){
                        System.out.println("Brak przewodu typu: "+ kabelListRepository.findAllByNameCable(key).get(1).getType1()  + " o przekroju żyły: "+  kabelListRepository.findAllByNameCable(key).get(1).getType2());
                    }else{
                        String x = isolationsCableService.findByTypeIsolationsAndPrzekrojWew(kabelListRepository.findAllByNameCable(key).get(1).getType1(), kabelListRepository.findAllByNameCable(key).get(1).getType2()).getPrzekrojZew();
                        if (findBySize(Float.parseFloat(x), "Czarna") != null) {
                            TermoTube k = findBySize(Float.parseFloat(x), "Czarna");
                            finalScore.put(k, finalScore.get(k) + 10);
                        }
                    }
                }else{
                    System.out.println("Brak przewodu typu: "+ kabelListRepository.findAllByNameCable(key).get(0).getType1()  + " o przekroju żyły: "+  kabelListRepository.findAllByNameCable(key).get(0).getType2());
                }
            }else{
                String x = isolationsCableService.findByTypeIsolationsAndPrzekrojWew(kabelListRepository.findAllByNameCable(key).get(0).getType1(), kabelListRepository.findAllByNameCable(key).get(0).getType2()).getPrzekrojZew();
                if (findBySize(Float.parseFloat(x), "Czarna") != null) {
                    TermoTube k = findBySize(Float.parseFloat(x), "Czarna");
                    finalScore.put(k, finalScore.get(k) + 10);
                }
            }
        });
        finalScore.forEach((key, value) -> System.out.println(key + " --- " + value));
        return finalScore;
    }

}
