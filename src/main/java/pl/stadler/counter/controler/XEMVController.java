package pl.stadler.counter.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.services.XEMVService;

import java.util.Map;

@RestController
@RequestMapping(path = "/xemv")
public class XEMVController {
    private final XEMVService xemvService;

    public XEMVController(XEMVService xemvService) {
        this.xemvService = xemvService;
    }

    //////////////////
    // wyliczenie Xemv
    //////////////////

    @GetMapping(path = "/counter")
    public Map<String, Integer> countXEMV() {
        return xemvService.XEMVCount();
    }
}
