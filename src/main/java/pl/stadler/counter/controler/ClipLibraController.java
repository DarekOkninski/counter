package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.ClipLibra;
import pl.stadler.counter.repositories.ClipLibraRepository;
import pl.stadler.counter.services.ClipLibraService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/clip")
public class ClipLibraController {
    private final ClipLibraService clipLibraService;

    @Autowired
    public ClipLibraController(ClipLibraService clipLibraService) {
        this.clipLibraService = clipLibraService;
    }

    @GetMapping(path = "/find-all")
    public List<ClipLibra> findAll() {
        return clipLibraService.findAll();
    }


    @PostMapping(path = "/save")
    public ClipLibra save(ClipLibra clipLibra){
        return clipLibraService.save(clipLibra);
    }

    @GetMapping(path= "/counter")
    public Map<String, Integer> clipLibraCounter(){ return clipLibraService.clipLibraCounter();}
}
