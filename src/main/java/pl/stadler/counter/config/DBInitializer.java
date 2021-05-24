package pl.stadler.counter.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.*;
import pl.stadler.counter.repositories.*;
import pl.stadler.counter.services.DistancesService;
import pl.stadler.counter.services.IsolationsCableService;

import java.util.List;
import java.util.Map;


@Configuration
public class DBInitializer {
    private String clipLibraPath;
    private String userName = System.getProperty("user.name");
    @Autowired
    private ExcelMenager excelMenager;

    @Autowired
    private ClipLibraRepository clipLibraRepository;

    @Autowired
    private DistancesService distancesService;

    @Autowired
    private IsolationsCableService isolationsCableService;

    @Autowired
    private KabelListRepository kabelListRepository;

    @Autowired
    private MeshRepository meshRepository;

    @Autowired
    private ProjectRepository projectRepository;


    @Bean
    InitializingBean init() {
        return () -> {

            if (clipLibraRepository.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet("C://Users//"+userName+"//Desktop//counter//databaseExcel//libra.xlsx", "Sheet1");
                map.forEach((key, value) -> {
                    clipLibraRepository.save(new ClipLibra(value.get(0), value.get(1), value.get(2), value.get(3), value.get(4)));
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
            if (meshRepository.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet("C://Users//"+userName+"//Desktop//counter//databaseExcel//mesh.xlsx", "Sheet1");
                map.forEach((key, value) -> {
                    meshRepository.save(new Mesh(value.get(0), value.get(1), value.get(2), value.get(3), value.get(3)));
                });
            }


        };
    }
}
