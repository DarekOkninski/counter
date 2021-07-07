package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Wrapper> countTermoTubeMultiWire(@PathVariable(value = "projectNumber") String projectNumber) throws IOException {

        Map<TermoTube, Integer> t = termoTubeService.countTermoTubeMultiWire(projectNumber);
        List<Wrapper> wrapperList = new ArrayList<>();

        t.forEach((k,v) -> {
            Wrapper wrapper = Wrapper.builder()
                    .termoTube(k)
                    .amountInteger(v)
                    .build();
            wrapperList.add(wrapper);
        });
        return wrapperList;
    }

    ////////////////////////////////////////
    // wyliczenie termokurczek - sh - 6 blok
    ////////////////////////////////////////

    @GetMapping(path = "/count-termotube-sh/{projectNumber}")
    public List<Wrapper> countTermoTubeSH(@PathVariable(value = "projectNumber") String projectNumber) throws IOException {
        Map<TermoTube, Integer> t = termoTubeService.countTermoTubeSH(projectNumber);
        List<Wrapper> wrapperList = new ArrayList<>();

        t.forEach((k,v) -> {
            Wrapper wrapper = Wrapper.builder()
                    .termoTube(k)
                    .amountInteger(v)
                    .build();
            wrapperList.add(wrapper);
        });
        return wrapperList;
    }

    //////////////////////////////////////////
    // wyliczenie termokurczek - blue - 6 blok
    //////////////////////////////////////////

    @GetMapping(path = "/count-termotube-blue/{projectNumber}")
    public List<Wrapper> countTermoTubeBlue(@PathVariable(value = "projectNumber") String projectNumber) throws IOException {
        Map<TermoTube, Integer> t = termoTubeService.countTermoTubeBlue(projectNumber);
        List<Wrapper> wrapperList = new ArrayList<>();

        t.forEach((k,v) -> {
            Wrapper wrapper = Wrapper.builder()
                    .termoTube(k)
                    .amountInteger(v)
                    .build();
            wrapperList.add(wrapper);
        });
        return wrapperList;
    }

}
