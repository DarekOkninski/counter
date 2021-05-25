package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.Project;
import pl.stadler.counter.repositories.KabelListRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class KabelListService {

    private KabelListRepository kabelListRepository;
    private final ExcelMenager excelMenager;
    private final ClipLibraService clipLibraService;
    private final ProjectService projectService;

    @Autowired
    public KabelListService(KabelListRepository kabelListRepository, ExcelMenager excelMenager, ClipLibraService clipLibraService, ProjectService projectService) {
        this.kabelListRepository = kabelListRepository;
        this.excelMenager = excelMenager;
        this.clipLibraService = clipLibraService;
        this.projectService = projectService;
    }
    public List<KabelList> findAll(){
        return kabelListRepository.findAll();
    }
    public KabelList findByPotential(String potential){
        return kabelListRepository.findByPotential(potential).orElse(null);
    }

    public KabelList save(KabelList kabelList){
        return kabelListRepository.save(kabelList);
    }

    public void addKabelList(String address) throws IOException {
        Project project = new Project();
        project.setDescriptionColumnNumber(1);
        project.setNameCableColumnNumber(2);
        project.setPotentialColumnNumber(3);
        project.setStrangColumnNumber(4);
        project.setPositionFromColumnNumber(7);
        project.setPinFromColumnNumber(9);
        project.setPositionToColumnNumber(12);
        project.setPinToColumnNumber(14);
        project.setMeshColumnNumber(15);
        project.setGelifertColumnNumber(19);
        project.setColorColumnNumber(21);
        project.setPrzekrojZylyColumnNumber(22);
        project.setType1ColumnNumber(23);
        project.setType2ColumnNumber(24);
        project.setLengthKableColumnNumber(25);
        projectService.save(project);

        //Map<Integer, List<String>> map = excelMenager.readWorksheet(address);
        Map<Integer, List<String>> map = excelMenager.getMapFromCSV(address);

        map.forEach((key, value) -> {
            KabelList kabelList = KabelList.builder()
                    .project(project)
                    .description(value.get(project.getDescriptionColumnNumber()))
                    .nameCable(value.get(project.getNameCableColumnNumber()))
                    .potential(value.get(project.getPotentialColumnNumber()))
                    .strang(value.get(project.getStrangColumnNumber()))
                    .positionFrom(value.get(project.getPositionFromColumnNumber()))
                    .pinFrom(value.get(project.getPinFromColumnNumber()))
                    .positionTo(value.get(project.getPositionToColumnNumber()))
                    .pinTo(value.get(project.getPinToColumnNumber()))
                    .mesh(value.get(project.getMeshColumnNumber()))
                    .gelifert(value.get(project.getGelifertColumnNumber()))
                    .color(value.get(project.getColorColumnNumber()))
                    .przekrojZyly(value.get(project.getPrzekrojZylyColumnNumber()))
                    .type1(value.get(project.getType1ColumnNumber()))
                    .type2(value.get(project.getType2ColumnNumber()))
                    .lengthKable(value.get(project.getLengthKableColumnNumber()))
                    .build();
            save(kabelList);
        });



    }
    public void addKabelList2(String address) throws IOException {
        excelMenager.getMapFromCSV(address);
    }




}
