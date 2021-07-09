package pl.stadler.counter.controler;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.ProjectSettings;
import pl.stadler.counter.models.Wrapper;
import pl.stadler.counter.services.KabelListService;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/kabel-list")
public class KabelListController {

    private final KabelListService kabelListService;
    private final ExcelMenager excelMenager;
    Logger loggerKabelListController = LoggerFactory.getLogger(KabelListController.class);

    // pobranie ścieżki do zapisu kabelListy
    @Value("${kabel-list.kabelListPath}")
    private String kabelListPath;

    @Autowired
    public KabelListController(KabelListService kabelListService, ExcelMenager excelMenager) {
        this.kabelListService = kabelListService;
        this.excelMenager = excelMenager;
    }

    ///////////////////////////////////////////
    // pobranie wyszystkich obiektów kabelListy
    ///////////////////////////////////////////

    @GetMapping(path = "/find-all")
    public List<KabelList> findAll() {
        return kabelListService.findAll();
    }

    //////////////////////////////////////////////////
    // pobranie obiektów kabelListy na podstawie strung
    //////////////////////////////////////////////////

    @GetMapping(path = "/find-by-strang")
    public List<KabelList> findAllByStrang(String strang) {
        return kabelListService.findAllByStrang(strang);
    }

    /////////////////////////////////////
    // zapisanie obiektu kabelListy do DB
    /////////////////////////////////////

    @PostMapping(path = "/save")
    public KabelList save(KabelList kabelList) {
        return kabelListService.save(kabelList);
    }

    ////////////////////////
    // pobranie listy siatek
    ////////////////////////

    @GetMapping(path = "/mesh")
    public List<Object[]> mesh() {
        return kabelListService.mesh();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // przechwycenie pliku i zapisanie go na dysk, metoda zwraca obiekt ze ścieżką do pliku
    ///////////////////////////////////////////////////////////////////////////////////////

    @PostMapping(path = "/save-kabel-list-on-disc")
    public Wrapper saveKabeListOnDisc(@RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException {
        // zapisanie pliku
        ProjectSettings projectSettings = new ProjectSettings();
        projectSettings.setKabelListPath(kabelListPath + file.getOriginalFilename());

        Wrapper wrapper = new Wrapper();
        wrapper.setProjectSettings(projectSettings);
        excelMenager.saveFile(file, projectSettings.getKabelListPath());

        return wrapper;
    }

    /////////////////////////////
    // zapisanie kabelListy do DB
    /////////////////////////////

    @PostMapping(path = "/save-kabel-list-to-db")
    public Wrapper saveKabelListToDB(@RequestBody Wrapper wrapper) {
        try {
            kabelListService.addKabelListToDB(wrapper);
            loggerKabelListController.info("KabelListController.addKabelList() - Lista została dodana do DB");
        } catch (Exception ignored) {
            loggerKabelListController.error("KabelListController.addKabelList() - Błąd dodania listy do DB");
        }
        return wrapper;
    }

}
