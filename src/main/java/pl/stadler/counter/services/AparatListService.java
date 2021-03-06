package pl.stadler.counter.services;

import org.springframework.stereotype.Service;
import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.AparatList;
import pl.stadler.counter.models.AparatListSettings;
import pl.stadler.counter.repositories.AparatListRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AparatListService {

    private ExcelMenager excelMenager;
    private final AparatListRepository aparatListRepository;
    private final AparatListSettingsService aparatListSettingsService;
    private final ProjectService projectService;

    public AparatListService(ExcelMenager excelMenager, AparatListRepository aparatListRepository, AparatListSettingsService aparatListSettingsService, ProjectService projectService) {
        this.excelMenager = excelMenager;
        this.aparatListRepository = aparatListRepository;
        this.aparatListSettingsService = aparatListSettingsService;
        this.projectService = projectService;
    }

    public List<AparatList> findAll() {
        return aparatListRepository.findAll();
    }
    public List<AparatList> findAllByPosition(String position){
        return aparatListRepository.findAllByPosition(position);
    }
    public List<AparatList> findAllByNumberProducer(String numberProducer) {
        return aparatListRepository.findAllByNumberProducer(numberProducer);
    }
    public List<AparatList> findAllByNameProducer(String nameProducer) {
        return aparatListRepository.findAllByNameProducer(nameProducer);
    }
    public List<AparatList> findAllByAreaAndPosition(String area, String position) {
        return aparatListRepository.findAllByAreaAndPosition(area, position);
    }
    public AparatList save(AparatList aparatList){
        return aparatListRepository.save(aparatList);
    }

    public void addAparatList(String address, String projectNumber) throws IOException {

        aparatListRepository.deleteAll();
        Map<Integer, List<String>> map = new HashMap<>();
        AparatListSettings aparatListSettings = aparatListSettingsService.findByProjectNumberProject(projectNumber);


        if(address.contains("csv")){
            map = excelMenager.getMapFromCSV(address);
        }else if(address.contains("xlsx")){
            map = excelMenager.readWorksheet(address, "APPARATE");



        }


        map.forEach((key, value) -> {
            AparatList aparatList = AparatList.builder()
                    .project(projectService.findByNumberProject(projectNumber))
                    .position(value.get(aparatListSettings.getPositionColumnNumber()))
                    .area(value.get(aparatListSettings.getAreaColumnNumber()))
                    .count(value.get(aparatListSettings.getCountColumnNumber()))
                    .function(value.get(aparatListSettings.getFunctionColumnNumber()))
                    .typ(value.get(aparatListSettings.getTypColumnNumber()))
                    .description(value.get(aparatListSettings.getDescriptionColumnNumber()))
                    .numberProducer(value.get(aparatListSettings.getNumberProducerColumnNumber()))
                    .nameProducer(value.get(aparatListSettings.getNameProducerColumnNumber()))
                    .schema(value.get(aparatListSettings.getSchemaColumnNumber()))
                    .stadlerNr(value.get(aparatListSettings.getStadlerNrColumnNumber()))
                    .build();
            save(aparatList);
        });
    }
}
