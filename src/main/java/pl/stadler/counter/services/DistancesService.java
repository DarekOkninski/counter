package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.AparatList;
import pl.stadler.counter.models.Distances;
import pl.stadler.counter.models.Wrapper;
import pl.stadler.counter.repositories.DistancesRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistancesService {

    private final DistancesRepository distancesRepository;
    private final AparatListService aparatListService;

    @Autowired
    public DistancesService(DistancesRepository distancesRepository, AparatListService aparatListService) {
        this.distancesRepository = distancesRepository;
        this.aparatListService = aparatListService;
    }

    public List<Distances> findAll() {
        return distancesRepository.findAll();
    }

    public Distances findByNumberHarting(String numberHarting) {
        return distancesRepository.findByNumberHarting(numberHarting).orElse(null);
    }
    public Distances save(Distances distances){
        return distancesRepository.save(distances);
    }


    public List<String> countDistance(){
        List<String> finalSorce = new ArrayList<>();
        //Wczytanie listy wtyczek na której daje sie dystansy
        List<Distances> distances = distancesRepository.findAll();
        Integer countSmallDistance = 0;
        Integer countBigDistance = 0;

        for (Distances distance : distances) {
            //Sprawdzanie czy jest to kość męska
            if(distance.getGender().equals("M")){
                //zwiekszenie liczników zaleznie od typu
                //ramka
                if(distance.getOrFrame()){
                    countSmallDistance += aparatListService.findAllByNumberProducer(distance.getNumberHarting()).size();
                }
                //Zwykła
                else {
                    countBigDistance += aparatListService.findAllByNumberProducer(distance.getNumberHarting()).size();
                }
            }
        }
        //Dodanie liczników do listy z wynikami
        finalSorce.add("Dystans do ramki(komplet):  " + countSmallDistance);
        finalSorce.add("Dystans do zwykłej kości Harting (komplet):  " + countBigDistance);




        return finalSorce;
    }
}
