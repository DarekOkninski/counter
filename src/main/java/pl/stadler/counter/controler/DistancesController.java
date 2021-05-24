package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.Distances;
import pl.stadler.counter.services.DistancesService;

import java.util.List;

@RestController
@RequestMapping(path = "/distance")
public class DistancesController {

    private final DistancesService distancesService;

    @Autowired
    public DistancesController(DistancesService distancesService) {
        this.distancesService = distancesService;
    }

    @GetMapping(path = "/find-all")
    public List<Distances> findAll() {
        return distancesService.findAll();
    }

    @GetMapping(path = "/find-by-number")
    public Distances findByNumberHarting(String numberHarting) {
        return distancesService.findByNumberHarting(numberHarting);
    }
    @PostMapping(path = "/save")
    public Distances save(Distances distances){
        return distancesService.save(distances);
    }
}
