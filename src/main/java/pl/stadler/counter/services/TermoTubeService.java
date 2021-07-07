package pl.stadler.counter.services;

import lombok.var;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.IsolationsCable;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.models.TermoTube;
import pl.stadler.counter.repositories.KabelListRepository;
import pl.stadler.counter.repositories.TermoTubeRepository;

import java.util.*;

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

    public TermoTube save(TermoTube termoTube) {
        return termoTubeRepository.save(termoTube);
    }

    public TermoTube findBySize(Float size, String color) {
        return termoTubeRepository.findBySize(size, color).orElse(null);
    }


    public List<TermoTube> findAllByColor(String color) {
        return termoTubeRepository.findAllByColor(color);
    }

    //////////////////////////////////////////////////////
    // obliczenie termokurczek dla przewodów wielożyłowych
    //////////////////////////////////////////////////////

    public Map<TermoTube, Integer> countTermoTubeMultiWire(String NumberProject) {

        if (projectService.findByNumberProject(NumberProject).getTyp().equals("E3")) {
            return groupTermoTubeMultiWireE3();
        } else {
            return groupTermoTubeMultiWireRuplan();
        }

    }

    public Map<TermoTube, Integer> groupTermoTubeMultiWireE3() {
        Map<TermoTube, Integer> finalScore = new HashMap<>();
        Map<String, Float> groupNrW = new HashMap<>();

        List<KabelList> kabelLists = kabelListRepository.findAll();
        // znajdowanie unikalnych nr W w kabelliscie Projektów typu E3
        for (KabelList kabelList : kabelLists) {
            // jeżeli nazwa nie jest pusta ani nie jest to mostek czy cu schine to dodajemy do mapy
            if (!kabelList.getNameCable().equals("") && !kabelList.getPrzekrojZyly().toUpperCase().contains("BR")) {
                if (!kabelList.getPotential().toUpperCase().contains("BR") && !kabelList.getPotential().toUpperCase().contains("CU")) {
                    groupNrW.put(kabelList.getNameCable(), 0.0F);
                }

            }
        }
        return countTermoTubeMultiWire(groupNrW);
    }

    public Map<TermoTube, Integer> groupTermoTubeMultiWireRuplan() {

        Map<String, Float> groupNrW = new HashMap<>();

        List<KabelList> kabelLists = kabelListRepository.findAll();
        //Znajdowanie unikalnych nr W w kabelliscie Projektów typu Ruplan
        for (KabelList kabelList : kabelLists) {
            //Wyszykujemy nazw zawierajacych '-W'
            if (kabelList.getNameCable().contains("-W")) {
                groupNrW.put(kabelList.getNameCable(), 0.0F);
            }
        }
        return countTermoTubeMultiWire(groupNrW);
    }

    public Map<TermoTube, Integer> countTermoTubeMultiWire(Map<String, Float> groupNrW) {
        Map<TermoTube, Integer> finalScore = new HashMap<>();
        //Tworzymy liste termokurczek czarnych
        for (TermoTube termoTube : this.findAllByColor("Czarna")) {
            finalScore.put(termoTube, 0);
        }
        groupNrW.forEach((key, value) -> {
            //Sprawdzenie czy mamy ten typ przewodu w bazie
            if (isolationsCableService.findByTypeIsolationsAndPrzekrojWew(kabelListRepository.findAllByNameCable(key).get(0).getType1(), kabelListRepository.findAllByNameCable(key).get(0).getType2()) == null) {
                //Jezeli nie mamy tego typu w bazie to sprawdzamy inna nazwe tego samego przewodu jezeli sprawdzane jest SH i jezeli istnieje inne połaczenie
                if (kabelListRepository.findAllByNameCable(key).get(0).getType2().toUpperCase().contains("SH") && kabelListRepository.findAllByNameCable(key).size() > 1) {
                    //Sprawdzenie ponowne czy mamy ten typ przewodu w bazie
                    if (isolationsCableService.findByTypeIsolationsAndPrzekrojWew(kabelListRepository.findAllByNameCable(key).get(1).getType1(), kabelListRepository.findAllByNameCable(key).get(1).getType2()) == null) {
                        System.out.println("Brak przewodu typu: " + kabelListRepository.findAllByNameCable(key).get(1).getType1() + " o przekroju żyły: " + kabelListRepository.findAllByNameCable(key).get(1).getType2());
                    } else {
                        String x = isolationsCableService.findByTypeIsolationsAndPrzekrojWew(kabelListRepository.findAllByNameCable(key).get(1).getType1(), kabelListRepository.findAllByNameCable(key).get(1).getType2()).getPrzekrojZew();
                        //Sprawdzenie czy termokurczki w bazie pasuja i jezeli tak to dodajemy odpowiedni typ do zapotrzebowania
                        if (findBySize(Float.parseFloat(x), "Czarna") != null) {
                            TermoTube k = findBySize(Float.parseFloat(x), "Czarna");
                            finalScore.put(k, finalScore.get(k) + 10);
                        }
                    }
                } else {
                    System.out.println("Brak przewodu typu: " + kabelListRepository.findAllByNameCable(key).get(0).getType1() + " o przekroju żyły: " + kabelListRepository.findAllByNameCable(key).get(0).getType2());
                }
            } else {
                String x = isolationsCableService.findByTypeIsolationsAndPrzekrojWew(kabelListRepository.findAllByNameCable(key).get(0).getType1(), kabelListRepository.findAllByNameCable(key).get(0).getType2()).getPrzekrojZew();
                //Sprawdzenie czy termokurczki w bazie pasuja i jezeli tak to dodajemy odpowiedni typ do zapotrzebowania
                if (findBySize(Float.parseFloat(x), "Czarna") != null) {
                    TermoTube k = findBySize(Float.parseFloat(x), "Czarna");
                    finalScore.put(k, finalScore.get(k) + 10);
                }
            }
        });
        finalScore.forEach((key, value) -> System.out.println(key + " --- " + value));
        return finalScore;
    }


    public Map<TermoTube, Integer> countTermoTubeSH(String NumberProject) {
        List<KabelList> kabelLists = new ArrayList<>();
        //Sprawdzenie jaki to typ projektu i wybranie odpowiedniej metody do zwrócenia listy wyszukiwanych połączeń
        if (projectService.findByNumberProject(NumberProject).getTyp().equals("E3")) {
            kabelLists = kabelListRepository.findAllByPotencialZeroE3();
        } else {
            kabelLists = kabelListRepository.findAllByPotencialZeroRuplan();
        }
        Map<TermoTube, Integer> finalScore = new HashMap<>();

        //Tworzymy liste termokurczek Żółto-Zielonych
        for (var TermoTube : this.findAllByColor("Zolta/Zielona")) {
            finalScore.put(TermoTube, 0);
        }

        for (KabelList kabelList : kabelLists) {
            //Sprawdzenie czy ten przewód nie jest typu cu schine Sh czy brak danych
            if (!kabelList.getPrzekrojZyly().toUpperCase().contains("CU") && !kabelList.getPrzekrojZyly().equals("") && !kabelList.getPrzekrojZyly().toUpperCase().contains("SH")) {
                //Sprawdzenie czy mamy informacje o tym przewodzie w bazie
                IsolationsCable isolationsCable = isolationsCableService.findByTypeIsolationsAndPrzekrojWew(kabelList.getType1(), kabelList.getType2());
                if (isolationsCable != null) {
                    //Sprawdzenie czy termokurczki w bazie pasuja i jezeli tak to dodajemy odpowiedni typ do zapotrzebowania
                    TermoTube k = findBySize(Float.parseFloat(isolationsCable.getPrzekrojZew()), "Zolta/Zielona");
                    if (k != null) {
                        finalScore.put(k, finalScore.get(k) + 10);
                    } else {
                        System.out.println("Brak Termokurczki Żółto-zielonej o przekroju : " + isolationsCable.getPrzekrojZew() + " W bazie danych");
                    }
                } else {
                    System.out.println("brak przewodu: " + kabelList.getType1() + " , " + kabelList.getType2() + " W bazie danych");
                }


            }
        }


        return finalScore;


    }

    public Map<TermoTube, Integer> countTermoTubeBlue(String NumberProject) {
        List<KabelList> kabelLists = new ArrayList<>();
        Map<TermoTube, Integer> finalScore = new HashMap<>();
//Sprawdzenie jaki to typ projektu i wybranie odpowiedniej metody do zwrócenia listy wyszukiwanych połączeń
        if (projectService.findByNumberProject(NumberProject).getTyp().equals("E3")) {
            kabelLists = kabelListRepository.findAllByTermoTubeBlueE3();
        } else {
            kabelLists = kabelListRepository.findAllByTermoTubeBlueRuplan();
        }
        //Tworzymy liste termokurczek Niebieskich
        for (var TermoTube : this.findAllByColor("Niebieska")) {
            finalScore.put(TermoTube, 0);
        }

        for (KabelList kabelList : kabelLists) {
            //Sprawdzenie czy ten przewód nie jest typu cu schine Sh czy brak danych
            if (!kabelList.getPrzekrojZyly().toUpperCase().contains("CU") && !kabelList.getPrzekrojZyly().equals("") && !kabelList.getPrzekrojZyly().toUpperCase().contains("SH")) {
                //Sprawdzenie czy mamy informacje o tym przewodzie w bazie
                IsolationsCable isolationsCable = isolationsCableService.findByTypeIsolationsAndPrzekrojWew(kabelList.getType1(), kabelList.getType2());
                if (isolationsCable != null) {

                    TermoTube k = findBySize(Float.parseFloat(isolationsCable.getPrzekrojZew()), "Niebieska");
                    if (k != null) {
                        finalScore.put(k, finalScore.get(k) + 10);
                    } else {
                        System.out.println("Brak Termokurczki Niebieskiej o przekroju : " + isolationsCable.getPrzekrojZew() + " W bazie danych");
                    }

                } else {
                    System.out.println("brak przewodu: " + kabelList.getType1() + " , " + kabelList.getType2() + " W bazie danych");
                }
            }
        }


        return finalScore;


    }
}
