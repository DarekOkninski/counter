package pl.stadler.counter.services;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.*;
import pl.stadler.counter.repositories.InstructionHartingRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InstructionHartingService {
    private final InstructionHartingRepository instructionHartingRepository;
    private final ExcelMenager excelMenager;
    private final ProjectService projectService;
    private  final InstructionHartingSettingsService instructionHartingSettingsService;
    private final AparatListService aparatListService;
    private final KabelListService kabelListService;
    private final PinsToBoneService pinsToBoneService;
    private final BoneHartingService boneHartingService;
//    private final ProjectSettings projectSettings;

    public InstructionHartingService(InstructionHartingRepository instructionHartingRepository, ExcelMenager excelMenager, ProjectService projectService, InstructionHartingSettingsService instructionHartingSettingsService, AparatListService aparatListService, KabelListService kabelListService, PinsToBoneService pinsToBoneService, BoneHartingService boneHartingService) {
        this.instructionHartingRepository = instructionHartingRepository;
        this.excelMenager = excelMenager;
        this.projectService = projectService;
        this.instructionHartingSettingsService = instructionHartingSettingsService;
//        this.projectSettings = projectSettings;
        this.aparatListService = aparatListService;
        this.kabelListService = kabelListService;
        this.pinsToBoneService = pinsToBoneService;
        this.boneHartingService = boneHartingService;
    }

    public List<InstructionHarting> findAll() {
        return instructionHartingRepository.findAll();
    }

    public InstructionHarting findByNameBone (String nameBone){
        return instructionHartingRepository.findByNameBone(nameBone).orElse(null);
    }

    public InstructionHarting save(InstructionHarting instructionHarting){
        return instructionHartingRepository.save(instructionHarting);
    }
    public void deleteAll(){
        instructionHartingRepository.deleteAll();
    }

    public void addInstructinHartingToDB(Wrapper wrapper) throws IOException{
        InstructionHartingSettings instructionHartingSettings = instructionHartingSettingsService.findByProject(wrapper.getProjectSettings().getProject());
        Map<Integer, List<String>> map = excelMenager.readWorksheetExcelXLSX2(wrapper.getProjectSettings().getKabelListPath(), instructionHartingSettings.getModulAColumnNumber(), instructionHartingSettings.getModulFColumnNumber());;

        map.forEach((key, value) -> {

            if (!value.isEmpty() && value.size() > 12 && ( value.get(instructionHartingSettings.getGenderColumnNumber()).toUpperCase().equals("F") || value.get(instructionHartingSettings.getGenderColumnNumber()).toUpperCase().equals("M"))) {
                boolean flag= false;

                if(value.get(instructionHartingSettings.getOrOneBoneColumnNumber()).isEmpty() || value.get(instructionHartingSettings.getOrOneBoneColumnNumber()).toUpperCase().equals("NIE")){
                    flag = true;
                }

                InstructionHarting instructionHarting = InstructionHarting.builder()
                    .project(wrapper.getProjectSettings().getProject())
                    .areaBone(value.get(instructionHartingSettings.getAreaBoneColumnNumber()))
                    .nameBone(value.get(instructionHartingSettings.getNameBoneColumnNumber()))
                    .gender(value.get(instructionHartingSettings.getGenderColumnNumber()))
                    .orOneBone(flag)
                    .boneNumber(value.get(instructionHartingSettings.getBoneNumberColumnNumber()))
                    .modulA(value.get(instructionHartingSettings.getModulAColumnNumber()))
                    .modulB(value.get(instructionHartingSettings.getModulBColumnNumber()))
                    .modulC(value.get(instructionHartingSettings.getModulCColumnNumber()))
                    .modulD(value.get(instructionHartingSettings.getModulDColumnNumber()))
                    .modulE(value.get(instructionHartingSettings.getModulEColumnNumber()))
                    .modulF(value.get(instructionHartingSettings.getModulFColumnNumber()))
                    .contact(value.get(instructionHartingSettings.getContactColumnNumber()))
                    .additionalMaterial(value.get(instructionHartingSettings.getAdditionalMaterialColumnNumber()))
                    .build();

                save(instructionHarting);
            }

        });
    }


   // @EventListener(ApplicationReadyEvent.class)
    public Map<String, Integer> countPinsHartingE3(){
        Map<String, Integer> finalScore = new HashMap<>();

        List<InstructionHarting> instructionHartings = instructionHartingRepository.findAll();
        List<PinsToBone> pinsToBones = pinsToBoneService.findAll();

        for (PinsToBone pinsToBone : pinsToBones) {
            finalScore.put(pinsToBone.getNumberProducer(), 0);
        }

        //System.out.println("fcgjfgjfj");
        for (InstructionHarting instructionHarting : instructionHartings) {
            //System.out.println("------------------------------ " + instructionHarting.getAreaBone() + " -- " + instructionHarting.getNameBone());
            List<AparatList> aparatLists = aparatListService.findAllByAreaAndPosition(instructionHarting.getAreaBone(), instructionHarting.getNameBone());
            //System.out.println(instructionHarting);
            for (AparatList aparatList : aparatLists) {
                //System.out.println(aparatList);
                for(String score: finalScore.keySet()){
                    //System.out.println(aparatList.getNumberProducer() + " ======= " + score);
                    if(aparatList.getNumberProducer().equals(score)){
                        finalScore.put(score, finalScore.get(score) + Integer.valueOf(aparatList.getCount()));
                    }
                }



            }
        }
        finalScore.forEach((key, value) -> System.out.println(key + " -- " + value));
        return finalScore;
    }

    public PinsToBone findPin(InstructionHarting  bone, KabelList kabelList){
        List<BoneHarting> boneHartings = boneHartingService.findAll();
        float sizeWire = 0.0F;
        BoneHarting tmpHarting = null;
        for (BoneHarting boneHarting : boneHartings) {
            if(bone.getBoneNumber().contains(boneHarting.getNumberProducer())){
                tmpHarting = boneHarting;
            }
        }

        if(tmpHarting != null){
            List<PinsToBone> pinsToBoneList = pinsToBoneService.findAllByBoneHartingId(tmpHarting.getId());

            if(0.14 < Float.parseFloat(kabelList.getPrzekrojZyly()) && Float.parseFloat(kabelList.getPrzekrojZyly()) < 0.37){
                sizeWire = 0.37F;
            }else{
                sizeWire= Float.parseFloat(kabelList.getPrzekrojZyly());
            }

            for (PinsToBone pinsToBone : pinsToBoneList) {

                if(pinsToBone.getSize() == sizeWire){
                    if(bone.getContact().contains(pinsToBone.getNamePin() + pinsToBone.getColor())){
                        return pinsToBone;
                    }else if(!bone.getContact().toLowerCase().contains("srebrne") || !bone.getContact().toLowerCase().contains("złote")){
                        return pinsToBone;
                    }

                }
            }
            System.out.println("brak pinu przekroju: " + kabelList.getPrzekrojZyly() + " i typie: " + bone.getContact() + " w kości: " + bone.getBoneNumber());

        }else{
            System.out.println("brak w bazie danych kości o numerze: " + bone.getBoneNumber());
        }
        return null;
    }

  @EventListener(ApplicationReadyEvent.class)
    public Map<String, Integer> countPinsHartingRuplan(){
        Map<String, Integer> finalScore = new HashMap<>();

        List<KabelList> kabelLists = kabelListService.findAll();
        //List<InstructionHarting> instructionHartings = instructionHartingRepository.findAll();
        List<PinsToBone> pinsToBones = pinsToBoneService.findAll();
//tworzenie pustej mapy z rodzajami pinów która bedzie przechowywac wyniki zliczania
        for (PinsToBone pinsToBone : pinsToBones) {
            finalScore.put(pinsToBone.getNumberProducer(), 0);
        }
//Przechodzimy po kolei po kazdym połączeniu z kabellisty
        for (KabelList kabelList : kabelLists) {
            InstructionHarting  boneFrom = null;
            InstructionHarting  boneTo = null;

        //Sprawdzamy czy w każdym połączeniu i z jednej strony i z drugiej jest nazwa która też sie znajduje w instrukcji hartingowej
        //oraz czy nie trzeba usunac '-' na poczatku   i przypisujemy do zmiennej odpowiednia linijke z instrukcji

            if(kabelList.getPositionFrom().substring(0,1).equals("-")){
                boneFrom = findByNameBone(kabelList.getPositionFrom().substring(1));
            }else{
                boneFrom = findByNameBone(kabelList.getPositionFrom());
            }

            if(kabelList.getPositionTo().substring(0,1).equals("-")){
                boneTo = findByNameBone(kabelList.getPositionTo().substring(1));
            }else{
                boneTo = findByNameBone(kabelList.getPositionTo());
            }




//Warunek czy jest tam kosc której piny bedziemy zliczac strona pierwsza
            if(boneFrom != null){
// warunek czy wtyczka nie jest modułowa. Jezeli nie to wywołujemy metode findPin która zwraca mam juz odpowiedni pin i go dodajemy do mapy pod odpowiednik kluczem
//Jezeli wtyczka jest modułowa to przechodzimy do metody findModul która zwarac nam juz odpowiedni pin i go dodajemy do mapy pod odpowiednik kluczem
                if(boneFrom.isOrOneBone() && findPin(boneFrom, kabelList) != null){
                    String NumberProducer = findPin(boneFrom, kabelList).getNumberProducer();
                    finalScore.put(NumberProducer, finalScore.get(NumberProducer) + 1);
                }else if(findModul(boneFrom, kabelList) != null){
                    String NumberProducer = findModul(boneFrom, kabelList).getNumberProducer();
                    finalScore.put(NumberProducer, finalScore.get(NumberProducer) + 1);

                }
            }
//Warunek czy jest tam kosc której piny bedziemy zliczac strona druga
            if(boneTo != null){
// warunek czy wtyczka nie jest modułowa. Jezeli nie to wywołujemy metode findPin która zwraca mam juz odpowiedni pin i go dodajemy do mapy pod odpowiednik kluczem
//Jezeli wtyczka jest modułowa to przechodzimy do metody findModul która zwarac nam juz odpowiedni pin i go dodajemy do mapy pod odpowiednik kluczem
                if(boneTo.isOrOneBone() && findPin(boneTo, kabelList) != null){

                    String NumberProducer = findPin(boneTo, kabelList).getNumberProducer();
                    finalScore.put(NumberProducer, finalScore.get(NumberProducer) + 1);
                }else if(findModul(boneTo, kabelList) != null){
                    String NumberProducer = findModul(boneTo, kabelList).getNumberProducer();
                    finalScore.put(NumberProducer, finalScore.get(NumberProducer) + 1);
                }
            }

        }

        //Wyswietlenie mapy z wynikami
        finalScore.forEach((key, value) -> System.out.println(key + " --- " + value));


        return finalScore;
    }



    public PinsToBone findModul(InstructionHarting  bone, KabelList kabelList){
        List<BoneHarting> boneHartings = boneHartingService.findAll();

        float sizeWire = 0.0F;
        BoneHarting tmpA = null;
        BoneHarting tmpB = null;
        BoneHarting tmpC = null;
        BoneHarting tmpD = null;
        BoneHarting tmpE = null;
        BoneHarting tmpF = null;


        //Znajdowanie odpowiedniego rodzaju modulu lub zwraca null
        for (BoneHarting boneHarting : boneHartings) {
            if(findNumber( bone, boneHarting, bone.getModulA()) != null){
                tmpA = findNumber( bone, boneHarting, bone.getModulA());
            }
            if(findNumber( bone, boneHarting, bone.getModulB()) != null){
                tmpB = findNumber( bone, boneHarting, bone.getModulB());
            }
            if(findNumber( bone, boneHarting, bone.getModulC()) != null){
                tmpC = findNumber( bone, boneHarting, bone.getModulC());
            }
            if(findNumber( bone, boneHarting, bone.getModulD()) != null){
                tmpD = findNumber( bone, boneHarting, bone.getModulD());
            }
            if(findNumber( bone, boneHarting, bone.getModulE()) != null){
                tmpE = findNumber( bone, boneHarting, bone.getModulE());
            }
            if(findNumber( bone, boneHarting, bone.getModulF()) != null){
                tmpF = findNumber( bone, boneHarting, bone.getModulF());
            }
        }
        if(tmpA == null && !bone.getModulA().trim().equals("") && !bone.getModulA().contains("09140009950")){
            System.out.println("nie opisało kości: " + bone.getModulA());
        }
        if(tmpB == null && !bone.getModulB().trim().equals("") && !bone.getModulB().contains("09140009950")){
            System.out.println("nie opisało kości: " + bone.getModulB());
        }
        if(tmpC == null && !bone.getModulC().trim().equals("") && !bone.getModulC().contains("09140009950")){
            System.out.println("nie opisało kości: " + bone.getModulC());
        }
        if(tmpD == null && !bone.getModulD().trim().equals("") && !bone.getModulD().contains("09140009950")){
            System.out.println("nie opisało kości: " + bone.getModulD());
        }
        if(tmpE == null && !bone.getModulE().trim().equals("") && !bone.getModulE().contains("09140009950")){
            System.out.println("nie opisało kości: " + bone.getModulE());
        }
        if(tmpF == null && !bone.getModulF().trim().equals("") && !bone.getModulF().contains("09140009950")){
            System.out.println("nie opisało kości: " + bone.getModulF());
        }

        if(findPin(bone, kabelList, tmpA, "A") != null){
            return  findPin(bone, kabelList, tmpA, "A");
        }
        if(findPin(bone, kabelList, tmpB, "B") != null){
            return  findPin(bone, kabelList, tmpB, "B");
        }
        if(findPin(bone, kabelList, tmpC, "C") != null){
            return  findPin(bone, kabelList, tmpC, "C");
        }
        if(findPin(bone, kabelList, tmpD, "D") != null){
            return  findPin(bone, kabelList, tmpD, "D");
        }
        if(findPin(bone, kabelList, tmpE, "E") != null){
            return  findPin(bone, kabelList, tmpE, "E");
        }
        if(findPin(bone, kabelList, tmpF, "F") != null){
            return  findPin(bone, kabelList, tmpF, "F");
        }
        return null;
    }

    public BoneHarting findNumber(InstructionHarting  bone, BoneHarting boneHarting, String modul){
        if(modul.contains(boneHarting.getNumberProducer()) || modul.contains("09140013111") || modul.contains("09140013011")  || modul.contains("09140023101") || modul.contains("09140023001") ){
            if(modul.contains(boneHarting.getNumberProducer())){
                return boneHarting;
            }else if(modul.contains("09140013111")){
                if(bone.getAdditionalMaterial().contains("09140083116") || bone.getAdditionalMaterial().contains("09140083117")){
                    return boneHartingService.findByNumberProducer("09140083116");
                }
            }else if(modul.contains("09140013011")){
                if(bone.getAdditionalMaterial().contains("09140083016") || bone.getAdditionalMaterial().contains("09140083017")){
                    return boneHartingService.findByNumberProducer("09140083016");
                }
            }else if(modul.contains("09140023101")){
                if(bone.getAdditionalMaterial().contains("09150043113")){
                    return boneHartingService.findByNumberProducer("09150043113");
                }
            }else if(modul.contains("09140023001")){
                if(bone.getAdditionalMaterial().contains("09150043013")){
                    return boneHartingService.findByNumberProducer("09150043013");
                }
            }
        }

        return null;
    }

    public PinsToBone findPin(InstructionHarting  bone, KabelList kabelList, BoneHarting tmp , String x){
        if(tmp != null){
            if(kabelList.getPositionTo().contains(bone.getNameBone()) && kabelList.getPinTo().toUpperCase().startsWith(x)){
               float sizeWire = 0.0F;

                List<PinsToBone> pinsToBoneList = pinsToBoneService.findAllByBoneHartingId(tmp.getId());

                if(0.14 <= Float.parseFloat(kabelList.getPrzekrojZyly()) && Float.parseFloat(kabelList.getPrzekrojZyly()) <= 0.37){
                    sizeWire = 0.37F;
                }else{
                    sizeWire= Float.parseFloat(kabelList.getPrzekrojZyly());
                }

                for (PinsToBone pinsToBone : pinsToBoneList) {
                    if(pinsToBone.getSize() == sizeWire){
                        if(bone.getContact().contains(pinsToBone.getNamePin() + pinsToBone.getColor()) || pinsToBone.getNamePin().toUpperCase().contains("TC")){
                            //System.out.println("ok: " + kabelList.getPositionTo()+ " - " + kabelList.getPinTo() + " ---|--- " + pinsToBone.getNamePin() + " -- " + pinsToBone.getSize() + " -- " + pinsToBone.getColor() + " --- " + pinsToBone.getNumberProducer());
                            return pinsToBone;
                        }else if(!bone.getContact().contains("srebrne") && !bone.getContact().contains("złote")){
                            //System.out.println("nie: " + kabelList.getPositionTo()+ " - " + kabelList.getPinTo() + " ---|--- " + pinsToBone.getNamePin() + " -- " + pinsToBone.getSize() + " -- " + pinsToBone.getColor() + " --- " + pinsToBone.getNumberProducer());
                            return pinsToBone;
                        }
                    }
                }
                System.out.println("brak pinu przekroju: " + kabelList.getPrzekrojZyly() + " i typie: " + bone.getContact() + " w kości: " + tmp.getNumberProducer() + " i pinie: " + kabelList.getPositionTo()+ ":" + kabelList.getPinTo());
            }
            if(kabelList.getPositionFrom().contains(bone.getNameBone()) && kabelList.getPinFrom().toUpperCase().startsWith(x)){
                float sizeWire = 0.0F;

                List<PinsToBone> pinsToBoneList = pinsToBoneService.findAllByBoneHartingId(tmp.getId());

                if(0.14 <= Float.parseFloat(kabelList.getPrzekrojZyly()) && Float.parseFloat(kabelList.getPrzekrojZyly()) <= 0.37){
                    sizeWire = 0.37F;
                }else{
                    sizeWire= Float.parseFloat(kabelList.getPrzekrojZyly());
                }

                for (PinsToBone pinsToBone : pinsToBoneList) {

                    if(pinsToBone.getSize() == sizeWire) {

                        if (bone.getContact().contains(pinsToBone.getNamePin() + pinsToBone.getColor()) || pinsToBone.getNamePin().toUpperCase().contains("TC")) {
                            //System.out.println("ok: " + kabelList.getPositionFrom()+ " - " + kabelList.getPinFrom() + " ---|--- " + pinsToBone.getNamePin() + " -- " + pinsToBone.getSize() + " -- " + pinsToBone.getColor() + " --- " + pinsToBone.getNumberProducer());
                            return pinsToBone;
                        } else if (!bone.getContact().contains("srebrne") && !bone.getContact().contains("złote")) {
                            //System.out.println("nie: "+ kabelList.getPositionFrom()+ " - " + kabelList.getPinFrom() + " ---|--- " + pinsToBone.getNamePin() + " -- " + pinsToBone.getSize() + " -- " + pinsToBone.getColor() + " --- " + pinsToBone.getNumberProducer());
                            return pinsToBone;
                        }

                    }

                }
                System.out.println("brak pinu przekroju: " + kabelList.getPrzekrojZyly() + " i typie: " + bone.getContact() + " w kości: " + tmp.getNumberProducer() + " i pinie: " + kabelList.getPositionFrom()+ ":" + kabelList.getPinFrom());
            }
        }

        return null;
    }




}