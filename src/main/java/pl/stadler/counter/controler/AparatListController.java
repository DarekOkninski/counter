package pl.stadler.counter.controler;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.AparatList;
import pl.stadler.counter.services.AparatListService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/aparat-list")
public class AparatListController {
    private final AparatListService aparatListService;


    public AparatListController(AparatListService aparatListService) {
        this.aparatListService = aparatListService;
    }
    @GetMapping(path = "/find-all")
    public List<AparatList> findAll() {
        return aparatListService.findAll();
    }
    @GetMapping(path = "/find-all-by-position")
    public List<AparatList> findAllByPosition(String position){
        return aparatListService.findAllByPosition(position);
    }
    @GetMapping(path = "/find-all-by-number-producer")
    public List<AparatList> findAllByNumberProducer(String numberProducer) {
        return aparatListService.findAllByNumberProducer(numberProducer);
    }
    @GetMapping(path = "/find-all-by-name-producer")
    public List<AparatList> findAllByNameProducer(String nameProducer) {
        return aparatListService.findAllByNameProducer(nameProducer);
    }

    @GetMapping(path = "/add")
    public void addKabelList() throws IOException {
        //String address = "C://Users//okndar//Desktop//L-4423.xlsx";
        String address = "C://Users//okndar//Desktop//L-4444_Apparateliste.xlsx";
        aparatListService.addAparatList(address, "L-4444");
    }

}
