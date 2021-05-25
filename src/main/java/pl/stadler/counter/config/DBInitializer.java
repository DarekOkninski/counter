package pl.stadler.counter.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.*;
import pl.stadler.counter.services.ClipLibraService;
import pl.stadler.counter.services.DistancesService;
import pl.stadler.counter.services.IsolationsCableService;
import pl.stadler.counter.services.MeshService;

import java.util.List;
import java.util.Map;


@Configuration
public class DBInitializer {
    private String clipLibraPath = "";
    private String userName = System.getProperty("user.name");
//    @Autowired
    private ExcelMenager excelMenager;

//    @Autowired
    private ClipLibraService clipLibraService;

//    @Autowired
    private DistancesService distancesService;

//    @Autowired
    private IsolationsCableService isolationsCableService;

    private MeshService meshService;

    @Autowired
    public DBInitializer(ExcelMenager excelMenager, ClipLibraService clipLibraService, DistancesService distancesService, IsolationsCableService isolationsCableService, MeshService meshService) {
        this.excelMenager = excelMenager;
        this.clipLibraService = clipLibraService;
        this.distancesService = distancesService;
        this.isolationsCableService = isolationsCableService;
        this.meshService = meshService;
    }


    @Bean
    InitializingBean init() {
        return () -> {

            if (clipLibraService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet("C://Users//"+userName+"//Desktop//counter//databaseExcel//libra.xlsx", "Sheet1");
                map.forEach((key, value) -> {
                    IsolationsCable isolationsCable = IsolationsCable.builder()
                            .typeIsolations(value.get(0))
                            .przekrojWew(value.get(1))
                            .przekrojZew(value.get(2))
                            .build();
                    isolationsCableService.save(isolationsCable);
                });

            }
            if (distancesService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet("C://Users//"+userName+"//Desktop//counter//databaseExcel//distances.xlsx", "Sheet1");
                map.forEach((key, value) -> {
                    Distances distances =Distances.builder()
                            .numberHarting(value.get(0))
                            .build();
                    distancesService.save(distances);
                });
            }
            if (isolationsCableService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet("C://Users//"+userName+"//Desktop//counter//databaseExcel//isolationsCable.xlsx", "Sheet1");
                map.forEach((key, value) -> {

                    IsolationsCable cable = IsolationsCable.builder()
                            .typeIsolations(value.get(0))
                            .przekrojWew(value.get(1))
                            .przekrojZew(value.get(2))
                            .build();

//
                    isolationsCableService.save(cable);
                });
            }
            if (meshService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet("C://Users//"+userName+"//Desktop//counter//databaseExcel//mesh.xlsx", "Sheet1");
                map.forEach((key, value) -> {
                    Mesh mesh = Mesh.builder()
                            .color(value.get(0))
                            .name(value.get(1))
                            .minSize(value.get(2))
                            .maxSize(value.get(3))
                            .numberProducer(value.get(4))
                            .build();
                    meshService.save(mesh);
                });
            }


        };
    }
}
