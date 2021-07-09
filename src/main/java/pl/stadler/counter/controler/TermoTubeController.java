package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.models.TermoTube;
import pl.stadler.counter.models.Wrapper;
import pl.stadler.counter.repositories.TermoTubeRepository;
import pl.stadler.counter.services.TermoTubeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/termotube")
public class TermoTubeController {

    private final TermoTubeService termoTubeService;

    @Autowired
    public TermoTubeController(TermoTubeService termoTubeService) {
        this.termoTubeService = termoTubeService;
    }

    ////////////////////////////////////
    // zapisanie obiektu do bazdy danych
    ////////////////////////////////////

    @GetMapping(path = "/save")
    public TermoTube save(TermoTube termoTube){
        return termoTubeService.save(termoTube);
    }

    ////////////////////////////////////////////
    // wyliczenie termokurczek - czarne - 5 blok
    ////////////////////////////////////////////

    @GetMapping(path = "/counter/{projectNumber}")
    public Wrapper countTermoTubeMultiWire(@PathVariable(value = "projectNumber") String projectNumber) throws IOException {
        return termoTubeService.countTermoTubeMultiWire(projectNumber);
    }



    ////////////////////////////////////////
    // wyliczenie termokurczek - sh - 6 blok
    ////////////////////////////////////////

    @GetMapping(path = "/count-termotube-sh/{projectNumber}")
    public Wrapper countTermoTubeSH(@PathVariable(value = "projectNumber") String projectNumber) throws IOException {
        return termoTubeService.countTermoTubeSH(projectNumber);
    }

    //////////////////////////////////////////
    // wyliczenie termokurczek - blue - 6 blok
    //////////////////////////////////////////

    @GetMapping(path = "/count-termotube-blue/{projectNumber}")
    public Wrapper countTermoTubeBlue(@PathVariable(value = "projectNumber") String projectNumber) throws IOException {
        return termoTubeService.countTermoTubeBlue(projectNumber);
    }




}
