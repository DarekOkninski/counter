package pl.stadler.counter.controler;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.stadler.counter.models.ClipLibra;
import pl.stadler.counter.repositories.*;

import java.util.List;

@Configuration
@Controller
public class AppControler {
    private ClipLibraRepository clipLibraRepository;
    private DistancesRepository distancesRepository;
    private IsolationsCableRepository isolationsCableRepository;
    private KabelListRepository kabelListRepository;
    private MeshRepository meshRepository;
    private ProjectRepository projectRepository;

//sdfdsfdsfgdsugfjdshgfjdshgfjdshgf
//sdfdsfdsfgdsugfjdshgfjdshgfjdshgf
//sdfdsfdsfgdsugfjdshgfjdshgfjdshgf
//sdfdsfdsfgdsugfjdshgfjdshgfjdshgf
    public AppControler(ClipLibraRepository clipLibraRepository, DistancesRepository distancesRepository, IsolationsCableRepository isolationsCableRepository, KabelListRepository kabelListRepository, MeshRepository meshRepository, ProjectRepository projectRepository) {
        this.clipLibraRepository = clipLibraRepository;
        this.distancesRepository = distancesRepository;
        this.isolationsCableRepository = isolationsCableRepository;
        this.kabelListRepository = kabelListRepository;
        this.meshRepository = meshRepository;
        this.projectRepository = projectRepository;
    }

    @ModelAttribute("clipLibra")
    public List<ClipLibra> loadClipLibra() {

        return clipLibraRepository.findAll();
    }

}
