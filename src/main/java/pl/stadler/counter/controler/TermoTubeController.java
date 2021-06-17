package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.models.TermoTube;
import pl.stadler.counter.repositories.TermoTubeRepository;
import pl.stadler.counter.services.TermoTubeService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(path = "/termotube")
public class TermoTubeController {

    private final TermoTubeService termoTubeService;

    @Autowired
    public TermoTubeController(TermoTubeService termoTubeService) {
        this.termoTubeService = termoTubeService;
    }
    @GetMapping(path = "/save")
    public TermoTube save(TermoTube termoTube){
        return termoTubeService.save(termoTube);
    }

    @GetMapping(path = "/counter/{projectNumber}")
    public Map<TermoTube, Integer> countTermoTubeMultiWire(@PathVariable(value = "projectNumber") String projectNumber) throws IOException {
        return termoTubeService.countTermoTubeMultiWire(projectNumber);
    }
    @GetMapping(path = "/countTermoTubeSH/{projectNumber}")
    public Map<TermoTube, Integer> countTermoTubeSH(@PathVariable(value = "projectNumber") String projectNumber) throws IOException {
        return termoTubeService.countTermoTubeSH(projectNumber);
    }
    @GetMapping(path = "/countTermoTubeBlue/{projectNumber}")
    public Map<TermoTube, Integer> countTermoTubeBlue(@PathVariable(value = "projectNumber") String projectNumber) throws IOException {
        return termoTubeService.countTermoTubeBlue(projectNumber);
    }

}
