package pl.stadler.counter.services;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.*;
import pl.stadler.counter.repositories.InstructionHartingRepository;

import java.io.IOException;
import java.util.*;

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
    private static LinkedHashSet<String> error = new LinkedHashSet<String>();
    private static LinkedHashSet<String> informations = new LinkedHashSet<String>();

    public InstructionHartingService(InstructionHartingRepository instructionHartingRepository, ExcelMenager excelMenager, ProjectService projectService, InstructionHartingSettingsService instructionHartingSettingsService, AparatListService aparatListService, KabelListService kabelListService, PinsToBoneService pinsToBoneService, BoneHartingService boneHartingService) {
        this.instructionHartingRepository = instructionHartingRepository;
        this.excelMenager = excelMenager;
        this.projectService = projectService;
        this.instructionHartingSettingsService = instructionHartingSettingsService;
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

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Metoda do zliczania pinów
    public Wrapper countPinsHarting(String projectNumber) throws IOException {
        Map<String, Integer> x =new HashMap<>();

        if(projectService.findByNumberProject(projectNumber).getTyp().equals("E3")){
            x = countPinsHartingE3();
        }else{
            x = countPinsHartingRuplan();
        }
        Wrapper wrapper = Wrapper.builder()
                .finalScore(x)
                .errors(error)
                .informations(informations)
                .build();
        return wrapper;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Metoda zliczajaca piny dla projektów typu E3
    public Map<String, Integer> countPinsHartingE3(){
        Map<String, Integer> finalScore = new HashMap<>();
        List<InstructionHarting> instructionHartings = instructionHartingRepository.findAll();
        List<PinsToBone> pinsToBones = pinsToBoneService.findAll();
//tworzenie pustej mapy z rodzajami pinów która bedzie przechowywac wyniki zliczania
        for (PinsToBone pinsToBone : pinsToBones) {
            finalScore.put(pinsToBone.getNumberProducer(), 0);
        }
//Przechodzimy po kazdym wierszu instrukcji hartingowej
        for (InstructionHarting instructionHarting : instructionHartings) {
//Wyszukujemy w aparatliście informacji na temat danej kości z instrukcji hartingowej po obsarze i nazwie wtyczki.
            List<AparatList> aparatLists = aparatListService.findAllByAreaAndPosition(instructionHarting.getAreaBone(), instructionHarting.getNameBone());
//Przechodzimy przez wszystkie wiersze aparatlisty spelniajacych warunek powyżej
            for (AparatList aparatList : aparatLists) {
//Przechodzimy po liście pinów i sprawdzamy który wiersz w aparatliście zawiera informacje o pinie. Jezeli znajdziemy taki wiersz to doliczamy ilość do odpowiedniego klucza w mapie.
                for(String score: finalScore.keySet()){
                    if(aparatList.getNumberProducer().equals(score)){
                        finalScore.put(score, finalScore.get(score) + Integer.valueOf(aparatList.getCount()));
                        informations.add("Dla wtyczki: " + instructionHarting.getNameBone() + " zliczono: " + aparatList.getCount() + " pinów o nr: " + score);
                    }
                }
            }
        }
        return finalScore;
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Metoda rozpoczynajaca zliczanie pinów dla projektów typu ruplan
    public Map<String, Integer> countPinsHartingRuplan(){
        Map<String, Integer> finalScore = new HashMap<>();
        List<KabelList> kabelLists = kabelListService.findAll();
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
            if(boneFrom != null && !kabelList.getPinFrom().toUpperCase().contains("SH") && !kabelList.getPinFrom().toUpperCase().contains("N.A") && !kabelList.getPinFrom().equals("")){
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
            if(boneTo != null && !kabelList.getPinTo().toUpperCase().contains("SH") && !kabelList.getPinTo().toUpperCase().contains("N.A") && !kabelList.getPinTo().equals("")){
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
        //finalScore.forEach((key, value) -> System.out.println(key + " --- " + value));
        return finalScore;
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Metoda wykonujaca zliczanie pinów dla wtyczki zwykłej.
    // Jako paramet przyjmuje obiekt z wartosciami wiersza pilku instrukcja harting o nazwe wtyczki (z jednej strony przewodu) oraz jedno połaczenie z kabellisty które rozpatrujemy
    // a zwracamy odszukany pin lub wartośc null jezeli go naie znajdziemy
    public PinsToBone findPin(InstructionHarting  bone, KabelList kabelList){
        List<BoneHarting> boneHartings = boneHartingService.findAll();
        float sizeWire = 0.0F;
        BoneHarting tmpHarting = null;
  //Przypisanie odpiowiedzniego obiektu do zmiannej typu BoneHarting po numerze przesłanym jako parametr z instrukcji hartingowej
        for (BoneHarting boneHarting : boneHartings) {
            if(bone.getBoneNumber().contains(boneHarting.getNumberProducer())){
                tmpHarting = boneHarting;
            }
        }
  //Jezeli wtyczka zostanie znaleziona to wchodzimy do bloku if a jezeli nie to mamy informacje o braku definicji numeru producenta w bazie i przypisujemy to do listy błedąw
        if(tmpHarting != null){
            List<PinsToBone> pinsToBoneList = pinsToBoneService.findAllByBoneHartingId(tmpHarting.getId());
//Jezeli przewód z kabellist który został przekazany jako parametr jest w rozmiarze 0.14-0.37 definiujemy zmiennna o rozmiarze górnym. W innym wypadku przypisujemy mu rozmiar jaki ma podany.
            if(0.14 < Float.parseFloat(kabelList.getPrzekrojZyly()) && Float.parseFloat(kabelList.getPrzekrojZyly()) < 0.37){
                sizeWire = 0.37F;
            }else{
                sizeWire= Float.parseFloat(kabelList.getPrzekrojZyly());
            }
//Przechodzimy po wszystkich pinach które sa przypisane w bazie do tej wtyczki.
            for (PinsToBone pinsToBone : pinsToBoneList) {
//Jezeli znajdziemy pin odpowiadajacy rozmiarowi przewodu to sprawdzamy informacje czy ma to byc pin koloru srebrnego czy złotego. JEzeli nie posiadamy takiej informacji przypisujemy mu pierwszy napotkany.
                if(pinsToBone.getSize() == sizeWire){
                    if(bone.getContact().contains(pinsToBone.getNamePin() + pinsToBone.getColor())){
                        if(kabelList.getPositionTo().contains(bone.getNameBone())){
                            informations.add("Dla pinu: " + kabelList.getPositionTo() + ":" + kabelList.getPinTo() + " - "+ kabelList.getNameCable() + " --- " + kabelList.getDescription() + " dobrano pin o numerze producenta: " + pinsToBone.getNumberProducer());
                        }else{
                            informations.add("Dla pinu: " + kabelList.getPositionFrom() + ":" + kabelList.getPinFrom() + " - "+ kabelList.getNameCable() + " --- " + kabelList.getDescription() +  " dobrano pin o numerze producenta: " + pinsToBone.getNumberProducer());
                        }

                        return pinsToBone;
                    }else if(!bone.getContact().toLowerCase().contains("srebrne") || !bone.getContact().toLowerCase().contains("złote")){
                        if(kabelList.getPositionTo().contains(bone.getNameBone())){
                            informations.add("Dla pinu: " + kabelList.getPositionTo() + ":" + kabelList.getPinTo() + " - "+ kabelList.getNameCable() + " --- " + kabelList.getDescription() +  " dobrano pin o numerze producenta: " + pinsToBone.getNumberProducer());
                        }else{
                            informations.add("Dla pinu: " + kabelList.getPositionFrom() + ":" + kabelList.getPinFrom() + " - "+ kabelList.getNameCable() + " --- " + kabelList.getDescription() +  " dobrano pin o numerze producenta: " + pinsToBone.getNumberProducer());
                        }
                        return pinsToBone;
                    }

                }
            }
            //Jezeli nie znajdziemy pinu i nie nastapi wyskoczenie z metody zwracamy bład o braku pinu a wskazanym typie;
            error.add("2## Brak informacji o pinie o przekroju: " + kabelList.getPrzekrojZyly() + ", typie: " + bone.getContact() + " przypisanego do kości o numerze producenta: " + bone.getBoneNumber() + " i nazwie pina: " + kabelList.getPositionTo()+ ":" + kabelList.getPinTo());

        }else{
            error.add("1## Brak informacji o wtyczce o numerze producenta: " + bone.getBoneNumber());
        }
        return null;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Metoda wyszukujaca jaka kośc jest w którym module.
    // Jako paramet przyjmuje obiekt z wartosciami wiersza pilku instrukcja harting o nazwe wtyczki (z jednej strony przewodu) oraz jedno połaczenie z kabellisty które rozpatrujemy
    //a zwracamy odszukany pin lub wartośc null jezeli go naie znajdziemy
    public PinsToBone findModul(InstructionHarting  bone, KabelList kabelList){
        List<BoneHarting> boneHartings = boneHartingService.findAll();

        float sizeWire = 0.0F;
        BoneHarting tmpA = null;
        BoneHarting tmpB = null;
        BoneHarting tmpC = null;
        BoneHarting tmpD = null;
        BoneHarting tmpE = null;
        BoneHarting tmpF = null;

        //Znajdowanie odpowiedniego rodzaju modulu po numerze  lub zwraca null
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

  //Sprawdzenie czy odnaleziono informacje o wszystkich numerach producenta w bazie z uwzglednieniem pustych miejsc oraz numeru zaślepki.
  //Jezeli nie rozpoznano jakiejś kości, zostaje dodana to informacja do listy błedów.
        if(tmpA == null && !bone.getModulA().trim().equals("") && !bone.getModulA().contains("09140009950")){
            error.add("1## Brak informacji o wtyczce o numerze producenta: " + bone.getModulA());
        }
        if(tmpB == null && !bone.getModulB().trim().equals("") && !bone.getModulB().contains("09140009950")){
            error.add("1## Brak informacji o wtyczce o numerze producenta: " + bone.getModulB());
        }
        if(tmpC == null && !bone.getModulC().trim().equals("") && !bone.getModulC().contains("09140009950")){
            error.add("1## Brak informacji o wtyczce o numerze producenta: " + bone.getModulC());
        }
        if(tmpD == null && !bone.getModulD().trim().equals("") && !bone.getModulD().contains("09140009950")){
            error.add("1## Brak informacji o wtyczce o numerze producenta: " + bone.getModulD());
        }
        if(tmpE == null && !bone.getModulE().trim().equals("") && !bone.getModulE().contains("09140009950")){
            error.add("1## Brak informacji o wtyczce o numerze producenta: " + bone.getModulE());
        }
        if(tmpF == null && !bone.getModulF().trim().equals("") && !bone.getModulF().contains("09140009950")){
            error.add("1## Brak informacji o wtyczce o numerze producenta: " + bone.getModulF());
        }


  //Wyszukiwanie pinów lub jezeli nie jest w stanie, zwrócenie wartosci null
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
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Metoda do wyszukiwania obiektów kości modułowych po numerze producenta
    //Jako parametr przyjmuje obiekt instrukcji hartingowej całej wtyczki, rozpatrywany obiekt kości do porównania oraz numer wtyczki która znajduje sie w danym module.
    public BoneHarting findNumber(InstructionHarting  bone, BoneHarting boneHarting, String modul){
  //Jezeli kośc rozpatrywana ma taki sam numer jak numer produkcujny znajdujacy sie w odpowiednim module informujacy nas jaka to wtyczka lub jezeli sa to nr adapterów  i zwracamu odpowiedni obiekt kości modułowej
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

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Metoda do znajdowania odpowiedniego pinu dla wtyczki Modułowej.
    //jako parametr przyjmuje obiekt Instrukcji hartingowej o odpowiedniej nazwie, Obiekt rozaptrywanego połączenie z kabellisty,
    // obiekt modułu znajdujacy sie w odpowiednym miescu w ramce i litere A-F informujacy które miejsce zajmuje w ramce.
    //Zwraca Znaleziony pin lub null
    public PinsToBone findPin(InstructionHarting  bone, KabelList kabelList, BoneHarting tmp , String x){
        if(tmp != null){
 //Rozpatrywanie dla pierwszej strony połaczenia
            if(kabelList.getPositionTo().contains(bone.getNameBone()) && kabelList.getPinTo().toUpperCase().startsWith(x)){
               float sizeWire = 0.0F;

                List<PinsToBone> pinsToBoneList = pinsToBoneService.findAllByBoneHartingId(tmp.getId());
//Jezeli przewód z kabellist który został przekazany jako parametr jest w rozmiarze 0.14-0.37 definiujemy zmiennna o rozmiarze górnym. W innym wypadku przypisujemy mu rozmiar jaki ma podany.
                if(0.14 <= Float.parseFloat(kabelList.getPrzekrojZyly()) && Float.parseFloat(kabelList.getPrzekrojZyly()) <= 0.37){
                    sizeWire = 0.37F;
                }else{
                    sizeWire= Float.parseFloat(kabelList.getPrzekrojZyly());
                }
//Przechodzimy po wszystkich pinach które sa przypisane w bazie do tej wtyczki.
                for (PinsToBone pinsToBone : pinsToBoneList) {
//Jezeli znajdziemy pin odpowiadajacy rozmiarowi przewodu to sprawdzamy informacje czy ma to byc pin koloru srebrnego czy złotego lub jest to pin TC który jest tylko srebrny. JEzeli nie posiadamy takiej informacji przypisujemy mu pierwszy napotkany.
                    if(pinsToBone.getSize() == sizeWire){
                        if(bone.getContact().contains(pinsToBone.getNamePin() + pinsToBone.getColor()) || pinsToBone.getNamePin().toUpperCase().contains("TC")){
                            informations.add("Dla pinu: " + kabelList.getPositionTo() + ":" + kabelList.getPinTo() + " - "+ kabelList.getNameCable() + " --- " + kabelList.getDescription() +  " dobrano pin o numerze producenta: " + pinsToBone.getNumberProducer());
                           return pinsToBone;
                        }else if(!bone.getContact().contains("srebrne") && !bone.getContact().contains("złote")){
                            informations.add("Dla pinu: " + kabelList.getPositionTo() + ":" + kabelList.getPinTo() + " - "+ kabelList.getNameCable() + " --- " + kabelList.getDescription() +  " dobrano pin o numerze producenta: " + pinsToBone.getNumberProducer());
                            return pinsToBone;
                        }
                    }
                }
                error.add("2## Brak informacji o pinie o przekroju: " + kabelList.getPrzekrojZyly() + ", typie: " + bone.getContact() + " przypisanego do kości o numerze producenta: " + tmp.getNumberProducer() + " i nazwie pina: " + kabelList.getPositionTo()+ ":" + kabelList.getPinTo());
            }
//Rozpatrywanie dla drugiej strony połaczenia
            if(kabelList.getPositionFrom().contains(bone.getNameBone()) && kabelList.getPinFrom().toUpperCase().startsWith(x)){
                float sizeWire = 0.0F;

                List<PinsToBone> pinsToBoneList = pinsToBoneService.findAllByBoneHartingId(tmp.getId());
//Jezeli przewód z kabellist który został przekazany jako parametr jest w rozmiarze 0.14-0.37 definiujemy zmiennna o rozmiarze górnym. W innym wypadku przypisujemy mu rozmiar jaki ma podany.
                if(0.14 <= Float.parseFloat(kabelList.getPrzekrojZyly()) && Float.parseFloat(kabelList.getPrzekrojZyly()) <= 0.37){
                    sizeWire = 0.37F;
                }else{
                    sizeWire= Float.parseFloat(kabelList.getPrzekrojZyly());
                }
//Przechodzimy po wszystkich pinach które sa przypisane w bazie do tej wtyczki.
                for (PinsToBone pinsToBone : pinsToBoneList) {
//Jezeli znajdziemy pin odpowiadajacy rozmiarowi przewodu to sprawdzamy informacje czy ma to byc pin koloru srebrnego czy złotego lub jest to pin TC który jest tylko srebrny. JEzeli nie posiadamy takiej informacji przypisujemy mu pierwszy napotkany.
                    if(pinsToBone.getSize() == sizeWire) {
                        if (bone.getContact().contains(pinsToBone.getNamePin() + pinsToBone.getColor()) || pinsToBone.getNamePin().toUpperCase().contains("TC")) {
                            informations.add("Dla pinu: " + kabelList.getPositionFrom() + ":" + kabelList.getPinFrom() + " - "+ kabelList.getNameCable() + " --- " + kabelList.getDescription() +  " dobrano pin o numerze producenta: " + pinsToBone.getNumberProducer());
                            return pinsToBone;
                        } else if (!bone.getContact().contains("srebrne") && !bone.getContact().contains("złote")) {
                            informations.add("Dla pinu: " + kabelList.getPositionFrom() + ":" + kabelList.getPinFrom() + " - "+ kabelList.getNameCable() + " --- " + kabelList.getDescription() +  " dobrano pin o numerze producenta: " + pinsToBone.getNumberProducer());
                            return pinsToBone;
                        }
                    }
                }
                error.add("2## Brak informacji o pinie o przekroju: " + kabelList.getPrzekrojZyly() + ", typie: " + bone.getContact() + " przypisanego do kości o numerze producenta: " + tmp.getNumberProducer() + " i nazwie pina: " + kabelList.getPositionTo()+ ":" + kabelList.getPinTo());
            }
        }
        return null;
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



}