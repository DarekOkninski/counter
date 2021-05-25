package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.Project;
import pl.stadler.counter.services.KabelListService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/kabelList")
public class KabelListController {

    private final KabelListService kabelListService;

    @Autowired
    public KabelListController(KabelListService kabelListService) {
        this.kabelListService = kabelListService;

    }


    @GetMapping(path = "/find-all")
    public List<KabelList> findAll() {
        return kabelListService.findAll();
    }

    @GetMapping(path = "/find-by-potential")
    public KabelList findByPotential(String potential) {
        return kabelListService.findByPotential(potential);
    }
    @PostMapping(path = "/save")
    public KabelList save(KabelList kabelList){
        return kabelListService.save(kabelList);
    }


    @GetMapping(path = "/add-kabelList")
    public void addKabelList() throws IOException {
        //String address = "C://Users//okndar//Desktop//L-4423.xlsx";
        String address = "C://Users//okndar//Desktop//LLL.csv";
        kabelListService.addKabelList(address);
    }
}
