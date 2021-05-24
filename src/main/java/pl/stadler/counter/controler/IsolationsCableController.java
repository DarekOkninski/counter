package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.stadler.counter.models.IsolationsCable;
import pl.stadler.counter.services.IsolationsCableService;

import java.util.List;

@RestController
@RequestMapping(path = "/isolation")
public class IsolationsCableController {


    private final IsolationsCableService isolationsCableService;

    @Autowired
    public IsolationsCableController(IsolationsCableService isolationsCableService) {
        this.isolationsCableService = isolationsCableService;
    }
    @GetMapping(path = "/find-all")
    public List<IsolationsCable> findAll() {
        return isolationsCableService.findAll();
    }
    @GetMapping(path = "/find-by-type/{typeIsolations}/{przekrojWew}")
    public IsolationsCable findByTypeIsolationsAndPrzekrojWew(@PathVariable( value = "typeIsolations") String typeIsolations, @PathVariable( value = "przekrojWew") String przekrojWew) {
        return isolationsCableService.findByTypeIsolationsAndPrzekrojWew(typeIsolations, przekrojWew);
    }
    @PostMapping(path = "/save")
    public IsolationsCable save(@RequestBody IsolationsCable isolationsCable){
        return isolationsCableService.save(isolationsCable);
    }


}
