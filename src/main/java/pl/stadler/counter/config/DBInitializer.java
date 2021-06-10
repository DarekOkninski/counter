package pl.stadler.counter.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.*;
import pl.stadler.counter.services.*;

import java.util.List;
import java.util.Map;


@Configuration
public class DBInitializer {
    private String userName = System.getProperty("user.name");
    @Autowired
    private ExcelMenager excelMenager;

    @Autowired
    private ClipLibraService clipLibraService;

    @Autowired
    private DistancesService distancesService;

    @Autowired
    private IsolationsCableService isolationsCableService;

    @Autowired
    private MeshService meshService;
    @Autowired
    private KabelListSettingsService kabelListSettingsService;
    @Autowired
    private ProjectService projectService;


    @Bean
    InitializingBean init() {
        return () -> {
            if (kabelListSettingsService.findAll().isEmpty()) {
                KabelListSettings kabelListSettings = KabelListSettings.builder()
                        .project(projectService.findByNumberProject("L-4444"))
                        .descriptionColumnNumber(1)
                        .nameCableColumnNumber(2)
                        .potentialColumnNumber(3)
                        .strangColumnNumber(4)
                        .positionFromColumnNumber(7)
                        .pinFromColumnNumber(9)
                        .positionToColumnNumber(12)
                        .pinToColumnNumber(14)
                        .meshColumnNumber(15)
                        .gelifertColumnNumber(19)
                        .colorColumnNumber(21)
                        .przekrojZylyColumnNumber(22)
                        .type1ColumnNumber(23)
                        .type2ColumnNumber(24)
                        .lengthKableColumnNumber(25)
                        .build();

                kabelListSettingsService.save(kabelListSettings);
            }

            if (clipLibraService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet("C://Users//"+userName+"//Desktop//counter//databaseExcel//libra.xlsx" ,"KABELLISTE");
                map.forEach((key, value) -> {
                    ClipLibra clipLibra = ClipLibra.builder()
                            .nameClip(value.get(0))
                            .typCable(value.get(1))
                            .diameterClip(value.get(2))
                            .clipNumber(value.get(3))
                            .diameterCable(value.get(4))
                            .build();
                    clipLibraService.save(clipLibra);
                });

            }
            if (distancesService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet("C://Users//"+userName+"//Desktop//counter//databaseExcel//distances.xlsx", "KABELLISTE");
                map.forEach((key, value) -> {
                    Distances distances =Distances.builder()
                            .numberHarting(value.get(0))
                            .build();
                    distancesService.save(distances);
                });
            }
            if (isolationsCableService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet("C://Users//"+userName+"//Desktop//counter//databaseExcel//isolationsCable.xlsx" ,"KABELLISTE" );
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
                Map<Integer, List<String>> map = excelMenager.readWorksheet("C://Users//"+userName+"//Desktop//counter//databaseExcel//mesh.xlsx","KABELLISTE" );
                map.forEach((key, value) -> {
                    Mesh mesh = Mesh.builder()
                            .color(value.get(0))
                            .name(value.get(1))
                            .minSize(Integer.parseInt(value.get(2)))
                            .maxSize(Integer.parseInt(value.get(3)))
                            .numberProducer(value.get(4))
                            .build();
                    meshService.save(mesh);
                });
            }


        };
    }
}
