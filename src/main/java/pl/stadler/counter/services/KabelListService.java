package pl.stadler.counter.services;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.*;
import pl.stadler.counter.repositories.KabelListRepository;

import java.io.IOException;
import java.util.*;

@Service
public class KabelListService {

    private KabelListRepository kabelListRepository;
    private final ExcelMenager excelMenager;

    private final ProjectService projectService;
    private final KabelListSettingsService kabelListSettingsService;

    @Autowired
    public KabelListService(KabelListRepository kabelListRepository, ExcelMenager excelMenager, ProjectService projectService, KabelListSettingsService kabelListSettingsService) {
        this.kabelListRepository = kabelListRepository;
        this.excelMenager = excelMenager;
        this.projectService = projectService;
        this.kabelListSettingsService = kabelListSettingsService;
    }

    public List<KabelList> findAll() {
        return kabelListRepository.findAll();
    }

    public List<KabelList> findAllByPotencialZeroE3() {
        return kabelListRepository.findAllByPotencialZeroE3();
    }

    public List<KabelList> findAllByPotencialZeroRuplan() {
        return kabelListRepository.findAllByPotencialZeroRuplan();
    }

    public List<KabelList> findAllByStrang(String strang) {
        return kabelListRepository.findAllByStrang(strang);
    }

    public List<KabelList> findAllByNameCable(String nameCable) {
        return kabelListRepository.findAllByNameCable(nameCable);
    }

    public List<KabelList> findAllByStrangAndPositionFromAndPositionTo(String strang, String positionFrom, String positionTo) {
        return kabelListRepository.findAllByStrangAndPositionFromAndPositionTo(strang, positionFrom, positionTo);
    }

    public List<KabelList> findAllByPositionFromAndPinFromAndPositionToAndPinTo(String positionFrom, String pinFrom, String positionTo, String pinTo) {
        return kabelListRepository.findAllByPositionFromAndPinFromAndPositionToAndPinTo(positionFrom, pinFrom, positionTo, pinTo);
    }

    public List<KabelList> findAllByAreaFromAndPositionFromAndAreaToAndPositionTo(String areaFrom, String positionFrom, String areaTo, String positionTo) {
        return kabelListRepository.findAllByPositionFromAndPinFromAndPositionToAndPinTo(areaFrom, positionFrom, areaTo, positionTo);
    }

    public List<Object[]> mesh() {
        return kabelListRepository.mesh();
    }

    public List<Object[]> groupE3() {
        return kabelListRepository.groupE3();
    }

    /////////////////////////////
    // zapisanie kabelListy do DB
    /////////////////////////////

    public KabelList save(KabelList kabelList) {
        return kabelListRepository.save(kabelList);
    }

    /////////////////////////////////////////////////////////////////////////////////
    // wybór odpowiedniej metody do wczytania plików oraz zapisanie kabelListy do DB
    ////////////////////////////////////////////////////////////////////////////////

    public void addKabelListToDB(Wrapper wrapper) throws IOException, InvalidFormatException {

        kabelListRepository.deleteAll();
        Map<Integer, List<String>> map = null;
        KabelListSettings kabelListSettings = kabelListSettingsService.findByProjectNumberProject(wrapper.getProjectSettings().getProject().getNumberProject());

        if (wrapper.getProjectSettings().getKabelListPath().toUpperCase().contains("CSV")) {
                map = excelMenager.getMapFromCSV(wrapper.getProjectSettings().getKabelListPath());
        } else {
                map = excelMenager.readWorksheetExcelXLSX(wrapper.getProjectSettings().getKabelListPath(),"KABELLISTE");
        }

        map.forEach((key, value) -> {
            if (!value.isEmpty() && value.size() > 17) {

                KabelList kabelList = KabelList.builder()
                        .project(wrapper.getProjectSettings().getProject())
                        .description(value.get(kabelListSettings.getDescriptionColumnNumber()))
                        .nameCable(value.get(kabelListSettings.getNameCableColumnNumber()))
                        .potential(value.get(kabelListSettings.getPotentialColumnNumber()))
                        .strang(value.get(kabelListSettings.getStrangColumnNumber()))
                        .areaFrom(value.get(kabelListSettings.getAreaFromColumnNumber()))
                        .positionFrom(value.get(kabelListSettings.getPositionFromColumnNumber()))
                        .pinFrom(value.get(kabelListSettings.getPinFromColumnNumber()))
                        .areaTo(value.get(kabelListSettings.getAreaToColumnNumber()))
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
            }

        });
    }

}
