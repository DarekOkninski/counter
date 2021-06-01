package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.KabelListSettings;
import pl.stadler.counter.repositories.KabelListRepository;

import java.io.IOException;
import java.util.*;

@Service
public class KabelListService {

    private KabelListRepository kabelListRepository;
    private final ExcelMenager excelMenager;
    private final ClipLibraService clipLibraService;
    private final ProjectService projectService;
    private final KabelListSettingsService kabelListSettingsService;

    @Autowired
    public KabelListService(KabelListRepository kabelListRepository, ExcelMenager excelMenager, ClipLibraService clipLibraService, ProjectService projectService, KabelListSettingsService kabelListSettingsService) {
        this.kabelListRepository = kabelListRepository;
        this.excelMenager = excelMenager;
        this.clipLibraService = clipLibraService;
        this.projectService = projectService;
        this.kabelListSettingsService = kabelListSettingsService;
    }
    public List<KabelList> findAll(){
        return kabelListRepository.findAll();
    }
    public List<KabelList> findAllByStrang(String strang){
        return kabelListRepository.findAllByStrang(strang);
    }
    public List<KabelList> findAllByStrangAndPositionFromAndPositionTo(String strang, String positionFrom, String positionTo){
        return kabelListRepository.findAllByStrangAndPositionFromAndPositionTo(strang, positionFrom, positionTo);
    }
    public List<Object[]> mesh(){
        return kabelListRepository.mesh();
    }

    public KabelList save(KabelList kabelList){
        return kabelListRepository.save(kabelList);
    }

    public void addKabelList(String address, String projectNumber) throws IOException {

        kabelListRepository.deleteAll();
        Map<Integer, List<String>> map;
        KabelListSettings kabelListSettings = kabelListSettingsService.findByProjectNumberProject(projectNumber);


        if(address.contains("csv")){
            map = excelMenager.getMapFromCSV(address);
        }else{
            map = excelMenager.readWorksheet(address, "KABELLISTE");
        }
        map.forEach((key, value) -> {
            KabelList kabelList = KabelList.builder()
                    .project(projectService.findByNumberProject(projectNumber))
                    .description(value.get(kabelListSettings.getDescriptionColumnNumber()))
                    .nameCable(value.get(kabelListSettings.getNameCableColumnNumber()))
                    .potential(value.get(kabelListSettings.getPotentialColumnNumber()))
                    .strang(value.get(kabelListSettings.getStrangColumnNumber()))
                    .positionFrom(value.get(kabelListSettings.getPositionFromColumnNumber()))
                    .pinFrom(value.get(kabelListSettings.getPinFromColumnNumber()))
                    .positionTo(value.get(kabelListSettings.getPositionToColumnNumber()))
                    .pinTo(value.get(kabelListSettings.getPinToColumnNumber()))
                    .mesh(value.get(kabelListSettings.getMeshColumnNumber()))
                    .gelifert(value.get(kabelListSettings.getGelifertColumnNumber()))
                    .color(value.get(kabelListSettings.getColorColumnNumber()))
                    .przekrojZyly(value.get(kabelListSettings.getPrzekrojZylyColumnNumber()))
                    .type1(value.get(kabelListSettings.getType1ColumnNumber()))
                    .type2(value.get(kabelListSettings.getType2ColumnNumber()))
                    .lengthKable(value.get(kabelListSettings.getLengthKableColumnNumber()))
                    .build();
            save(kabelList);
        });
    }





}
