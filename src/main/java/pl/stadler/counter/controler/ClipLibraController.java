package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.ClipLibra;
import pl.stadler.counter.repositories.ClipLibraRepository;
import pl.stadler.counter.services.ClipLibraService;

import java.util.List;

@RestController
@RequestMapping(path = "/clip")
public class ClipLibraController {
    private final ClipLibraService clipLibraService;

    @Autowired
    public ClipLibraController(ClipLibraService clipLibraService) {
        this.clipLibraService = clipLibraService;
    }

    public List<ClipLibra> findAll() {
        return clipLibraService.findAll();
    }

    public ClipLibra findByNumberHarting(String nameClip) {
        return clipLibraService.findByNameClip(nameClip);
    }
    public ClipLibra save(ClipLibra clipLibra){
        return clipLibraService.save(clipLibra);
    }
}
