package pl.stadler.counter.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private  GripService gripService;
    @Autowired
    private  TermoTubeService termoTubeService;
    @Autowired
    private  BoneHartingService boneHartingService;
    @Autowired
    private  PinsToBoneService pinsToBoneService;
    @Autowired
    private InstructionHartingSettingsService instructionHartingSettingsService;

    // pobranie ścieżek
    @Value("${settings-path.termotubePath}")
    private String termotubePath;
    @Value("${settings-path.gripPath}")
    private String gripPath;
    @Value("${settings-path.libraPath}")
    private String libraPath;
    @Value("${settings-path.distancesPath}")
    private String distancesPath;
    @Value("${settings-path.isolationPath}")
    private String isolationPath;
    @Value("${settings-path.meshPath}")
    private String meshPath;
    @Value("${settings-path.boneHartingPath}")
    private String boneHartingPath;

    @Value("${settings-path.pinsToBonePath}")
    private String pinsToBonePath;
    @Bean
    InitializingBean init() {
        return () -> {

            if (boneHartingService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet(boneHartingPath ,"Sheet1");
                map.forEach((key, value) -> {
                    BoneHarting boneHarting = BoneHarting.builder()
                            .name(value.get(0))
                            .numberProducer(value.get(1))
                            .size(Integer.valueOf(value.get(2)))
                            .build();
                    boneHartingService.save(boneHarting);
                });
            }
            if (pinsToBoneService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet(pinsToBonePath ,"Sheet1");
                map.forEach((key, value) -> {
                    PinsToBone pinsToBone = PinsToBone.builder()
                            .boneHarting(boneHartingService.findByNumberProducer(value.get(0)))
                            .namePin(value.get(1))
                            .color(value.get(2))
                            .size(Float.parseFloat(value.get(3)))
                            .numberProducer(value.get(4))
                            .stadlerId(value.get(5))
                            .build();
                    pinsToBoneService.save(pinsToBone);
                });
            }



            if (gripService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet(gripPath ,"Sheet1");
                map.forEach((key, value) -> {
                    Grip grip = Grip.builder()
                            .numberGrip(value.get(0))
                            .build();
                    gripService.save(grip);
                });
            }
            if (termoTubeService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet(termotubePath,"Sheet1");
                map.forEach((key, value) -> {
                    TermoTube termoTube = TermoTube.builder()
                            .name(value.get(0))
                            .numberStadlerID(value.get(1))
                            .numberProducer(value.get(2))
                            .sizeMax(Float.parseFloat(value.get(3)))
                            .sizeMin(Float.parseFloat(value.get(4)))
                            .color(value.get(5))
                            .type(value.get(6))
                            .build();
                    termoTubeService.save(termoTube);
                });
            }

            if (instructionHartingSettingsService.findAll().isEmpty()) {

                InstructionHartingSettings instructionHartingSettings = InstructionHartingSettings.builder()
                        .project(projectService.findByNumberProject("L-4444"))
                        .nameBoneColumnNumber(2)
                        .areaBoneColumnNumber(0)
                        .orOneBoneColumnNumber(7)
                        .genderColumnNumber(3)
                        .boneNumberColumnNumber(8)
                        .modulAColumnNumber(8)
                        .modulBColumnNumber(9)
                        .modulCColumnNumber(10)
                        .modulDColumnNumber(11)
                        .modulEColumnNumber(12)
                        .modulFColumnNumber(13)
                        .contactColumnNumber(14)
                        .additionalMaterialColumnNumber(15)
                        .build();
                instructionHartingSettingsService.save(instructionHartingSettings);


                InstructionHartingSettings instructionHartingSettings2 = InstructionHartingSettings.builder()
                        .project(projectService.findByNumberProject("L-4400"))
                        .nameBoneColumnNumber(0)
                        .areaBoneColumnNumber(1)
                        .orOneBoneColumnNumber(6)
                        .genderColumnNumber(2)
                        .boneNumberColumnNumber(7)
                        .modulAColumnNumber(7)
                        .modulBColumnNumber(8)
                        .modulCColumnNumber(9)
                        .modulDColumnNumber(10)
                        .modulEColumnNumber(11)
                        .modulFColumnNumber(12)
                        .contactColumnNumber(13)
                        .additionalMaterialColumnNumber(14)
                        .build();
                instructionHartingSettingsService.save(instructionHartingSettings2);

            }

            if (kabelListSettingsService.findAll().isEmpty()) {
                KabelListSettings kabelListSettings = KabelListSettings.builder()
                        .project(projectService.findByNumberProject("L-4444"))
                        .descriptionColumnNumber(1)
                        .nameCableColumnNumber(2)
                        .potentialColumnNumber(3)
                        .strangColumnNumber(4)
                        .areaFromColumnNumber(5)
                        .positionFromColumnNumber(7)
                        .pinFromColumnNumber(9)
                        .areaToColumnNumber(10)
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

                KabelListSettings kabelListSettings2 = KabelListSettings.builder()
                        .project(projectService.findByNumberProject("L-4400"))
                        .descriptionColumnNumber(29)
                        .nameCableColumnNumber(0)
                        .potentialColumnNumber(1)
                        .strangColumnNumber(2)
                        .areaFromColumnNumber(5)
                        .positionFromColumnNumber(6)
                        .pinFromColumnNumber(7)
                        .areaToColumnNumber(12)
                        .positionToColumnNumber(13)
                        .pinToColumnNumber(14)
                        .meshColumnNumber(18)
                        .gelifertColumnNumber(21)
                        .colorColumnNumber(19)
                        .przekrojZylyColumnNumber(20)
                        .type1ColumnNumber(21)
                        .type2ColumnNumber(20)
                        .lengthKableColumnNumber(22)
                        .build();

                kabelListSettingsService.save(kabelListSettings2);
            }

            if (clipLibraService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet(libraPath ,"Sheet1");
                map.forEach((key, value) -> {
                    ClipLibra clipLibra = ClipLibra.builder()
                            .clipNumberStadlerID(value.get(0))
                            .clipNumberProducer(value.get(1))
                            .diameterClip(value.get(2))
                            .diameterClipMin(Float.parseFloat(value.get(3)))
                            .diameterClipMax(Float.parseFloat(value.get(4)))
                            .build();
                    clipLibraService.save(clipLibra);
                });

            }
            if (distancesService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet(distancesPath, "KABELLISTE");
                map.forEach((key, value) -> {
                    Distances distances =Distances.builder()
                            .numberHarting(value.get(0))
                            .orFrame(Boolean.valueOf(value.get(1)))
                            .gender(value.get(2))
                            .build();
                    distancesService.save(distances);
                });
            }
            if (isolationsCableService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet(isolationPath ,"KABELLISTE" );
                map.forEach((key, value) -> {

                    IsolationsCable cable = IsolationsCable.builder()
                            .typeIsolations(value.get(0))
                            .przekrojWew(value.get(1))
                            .srednicaWew(Float.valueOf(value.get(2)))
                            .przekrojZew(value.get(3))
                            .multiWire(Boolean.parseBoolean(value.get(4)))
                            .build();

//
                    isolationsCableService.save(cable);
                });
            }
            if (meshService.findAll().isEmpty()) {
                Map<Integer, List<String>> map = excelMenager.readWorksheet(meshPath,"KABELLISTE" );
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
