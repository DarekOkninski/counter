package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.services.KabelListService;

import java.io.IOException;
import java.util.List;

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

    @GetMapping(path = "/find-by-strang")
    public List<KabelList> findAllByStrang(String strang) {
        return kabelListService.findAllByStrang(strang);
    }

    @PostMapping(path = "/save")
    public KabelList save(KabelList kabelList) {
        return kabelListService.save(kabelList);
    }
    @GetMapping(path = "/mesh")
    public List<Object[]> mesh() {
        return kabelListService.mesh();
    }

    @GetMapping(path = "/groupE3")
    public List<Object[]> groupE3() {
        return kabelListService.groupE3();
    }

//działa e3 na xmlx i ruplan xmlx oraz csv
    @GetMapping(path = "/add-kabelList")
    public void addKabelListE3() throws IOException {
        String address = "C://Users//okndar//Desktop//L-4400_Kabelliste.xlsx";
        //String address = "C://Users//okndar//Desktop//L-4444_GESAMTKABELLISTE.csv";
        kabelListService.addKabelList(address, "L-4400");
    }
//
//    //@PostMapping(path = "/find-mesh")
//    public List<KabelList> findAllByStrangAndPositionFromAndPositionTo(String strang, String positionFrom, String positionTo) {
//        return kabelListService.findAllByStrangAndPositionFromAndPositionTo(strang, positionFrom, positionTo);
//    }
//
//   // @PostMapping(path = "/find-mesh-E3")
//    public List<KabelList> findAllByPositionFromAndPinFromAndPositionToAndPinTo(String positionFrom, String pinFrom, String positionTo, String pinTo) {
//        return kabelListService.findAllByPositionFromAndPinFromAndPositionToAndPinTo(positionFrom, pinFrom, positionTo, pinTo);
//    }



}
