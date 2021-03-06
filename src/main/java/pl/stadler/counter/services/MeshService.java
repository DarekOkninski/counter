package pl.stadler.counter.services;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.IsolationsCable;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.models.Wrapper;
import pl.stadler.counter.repositories.MeshRepository;

import java.io.IOException;
import java.util.*;

@Service
public class MeshService {

    private final MeshRepository meshRepository;
    private final KabelListService kabelListService;
    private final ExcelMenager excelMenager;
    private final IsolationsCableService isolationsCableService;
    private final ProjectService projectService;
    private LinkedHashSet<String> error = new LinkedHashSet<>();
    private LinkedHashSet<String> informations = new LinkedHashSet<>();
    @Autowired
    public MeshService(MeshRepository meshRepository, KabelListService kabelListService, ExcelMenager excelMenager, IsolationsCableService isolationsCableService, ProjectService projectService) {
        this.meshRepository = meshRepository;
        this.kabelListService = kabelListService;
        this.excelMenager = excelMenager;
        this.isolationsCableService = isolationsCableService;
        this.projectService = projectService;
    }

    public List<Mesh> findAll() {
        return meshRepository.findAll();
    }

    public Mesh findByNumberProducer(String numberProducer) {
        return meshRepository.findByNumberProducer(numberProducer).orElse(null);
    }
    public Mesh findByColorMinMax(String color, String min, String max) {
        return meshRepository.findByColorAndMinSizeAndMaxSize(color, min, max).orElse(null);
    }

    public Mesh findMesh(Integer size, String color) {
        return meshRepository.findMesh(size, color).orElse(null);
    }

    public Mesh save(Mesh mesh) {
        return meshRepository.save(mesh);
    }


//    public Map<Mesh, Float> groupMap(String projectNumber) throws IOException {
//        if(projectService.findByNumberProject(projectNumber).getTyp().equals("E3")){
//            return groupMapE3();
//        }else{
//            return groupMapRuplan();
//        }
//    }

    public Wrapper groupMap(String projectNumber) throws IOException {
        Map<Mesh, Float> x = new HashMap<>();
        if(projectService.findByNumberProject(projectNumber).getTyp().equals("E3")){
            x =  groupMapE3();
        }else{
            x =  groupMapRuplan();
        }
        Wrapper wrapper = Wrapper.builder()
                .groupMap(x)
                .errors(error)
                .informations(informations)
                .build();
        return wrapper;
    }


//Tworzenie grup i uzystanie wyniku
    public Map<Mesh, Float> groupMapRuplan() throws IOException {
        Map<Mesh, Float> finalScore = new HashMap<>();

        Map<Integer, List<List<String>>> groupExpection = new HashMap<>();
        List<Object[]> data = kabelListService.mesh();
        String fileLocation = "L://05_KIEROWNICTWO//07_Teamleader//Okninski Dariusz//Ustawienia aplikacji//do uzupelnienia//groupExceptionRuplan.csv";
        Map<Integer, List<String>> mapExceptions  = excelMenager.getMapFromCSV(fileLocation);
//tworzenie listy siatek z przypisanym zapotrzebowaniem
        for(Mesh mesh: this.findAll()){
            finalScore.put(mesh, 0.0F);
        }
        int count = 0;
        List<List<String>> pom = new ArrayList<>();
// tworzenie grup z wczytanego pliku z wyjatkami na zasadzie ??aczenia w grupe az napotka pusta linijke.
        for(Map.Entry<Integer, List<String>> x : mapExceptions.entrySet()) {
            if(x.getValue().isEmpty()){
                groupExpection.put(count, pom);
                count ++;
                pom = new ArrayList<>();
            }else{
                pom.add(x.getValue());
            }
        }
        groupExpection.put(count, pom);

        //groupExpection.forEach((key, value) -> System.out.println(key + " --- " + value));


        int i = 0;

        // przechodzenie po kazdej grupie
        for (Object[] x : data) {
       // odrzucenie strang??w z konc??wka 99 poniewaz sa to po????czenia intern kt??rych nie liczymy
            // na tablice nie ma siatek
            if (!x[0].toString().endsWith("99")) {
                //ustawienie flagi czy grupa wystepuje juz w wyjatkach
                boolean flag =true;
                for(Map.Entry<Integer, List<List<String>>> k : groupExpection.entrySet()) {
                    // por??wnanie grupy wyjatk??w i grup standardowych
                    for (List<String> j :k.getValue()){
                        if(j.get(0).equals(x[0].toString()) && j.get(1).equals(x[1].toString()) && j.get(2).equals(x[2].toString())) {
                            flag = false;
                        }
                    }
                }

                // wszystkie po????czenia dla danej grupy
                List<KabelList> mesh = kabelListService.findAllByStrangAndPositionFromAndPositionTo(x[0].toString(), x[1].toString(), x[2].toString());

                //Zliczanie rozmiaru grupy przewod??w jezeli nie wystepuje w wyjatkach
                if(!countsCrossSection(mesh).isEmpty() && flag){

                    // countsCrossSection liczenie siatek dla wybranej grupy
                    List<String> score = countsCrossSection(mesh);

                    float val = finalScore.get(findByNumberProducer(score.get(0)));
                    //dodanie wyniku do zapotrzebowania odpowiedniej siatki
                    finalScore.replace(findByNumberProducer(score.get(0)), val + Float.parseFloat(score.get(1)));

                    informations.add("Grupa nr: " + i + " Po????czenie: " + mesh + " -- Dobrano siatke: " + findByNumberProducer(score.get(0)) + " d??ugo????: " + score.get(1));
                }
            }
            i++;
        }
        i = 0;
        for(Map.Entry<Integer, List<List<String>>> k : groupExpection.entrySet()) {
            List<KabelList> mesh = new ArrayList<>();
            // Laczenie grup z wyjatk??w
            for (List<String> j :k.getValue()){
                List<KabelList> meshTmp = kabelListService.findAllByStrangAndPositionFromAndPositionTo(j.get(0), j.get(1), j.get(2));
                mesh.addAll(meshTmp);
            }
            //Zliczanie rozmiaru grupy przewod??w z wyjatk??w
            if(!countsCrossSection(mesh).isEmpty()){

                List<String> score = countsCrossSection(mesh);
                float val = finalScore.get(findByNumberProducer(score.get(0)));
                //dodanie wyniku do zapotrzebowania odpowiedniej siatki
                finalScore.replace(findByNumberProducer(score.get(0)), val + Float.parseFloat(score.get(1)));
                informations.add("Grupa nr: " + i + " Po????czenie: " + mesh + " -- Dobrano siatke: " + findByNumberProducer(score.get(0)) + " d??ugo????: " + score.get(1));
            }
            i++;
        }

        //finalScore.forEach((key, value) -> System.out.println(key.getName() + "  --  " + key.getNumberProducer() + "  --  " + value));

        return finalScore;
    }


    public List<String> countsCrossSection(List<KabelList> group){
        boolean orMesh = false;
        List<IsolationsCable> isolationsCable = isolationsCableService.findAll();
        // grupowanie przewod??w wielozy??owych, aby unikn???? powt??rze??
        Map<String, IsolationsCable> numberW = new HashMap<>();
        // kable kt??rych nie ma w bazie
        //Map<String, String> missingCable = new HashMap<>();

        String color = "Black";
        List<String> score = new ArrayList<>();

// Sprawdzenie czy jest w grupie przew??d gkw i nie jest on wielo??y??owy
        float maxLength = 0.0F;
        for(int i = 0; i <group.size(); i++){
            if (group.get(i).getType1().toUpperCase().contains("GKW") && (!group.get(i).getType2().contains("x"))){
                orMesh = true;
            }
        };
        if(orMesh){
            float sum = 0;
            for (int i = 0; i <group.size(); i++){

                    Boolean orTypeCable = false;
        //sprawdzenie czy siatka powinna byc czarna czy szara
                    if(group.get(i).getMesh().contains("Hauptstrom")){
                        color = "Gray";
                    }
                    for(int j = 0; j < isolationsCable.size(); j++){
                        //sprawdzenie czy ten typ przewodu mamy w bazie
                        if(( group.get(i).getType1().trim().equals(isolationsCable.get(j).getTypeIsolations()) ) && ( group.get(i).getType2().contains(isolationsCable.get(j).getPrzekrojWew()) )){
                            orTypeCable = true;
                            if(group.get(i).getLengthKable().contains(",")){
                                group.get(i).setLengthKable(group.get(i).getLengthKable().replace(",","."));
                            }
                            if( NumberUtils.isParsable(group.get(i).getLengthKable()) && maxLength < Float.parseFloat(group.get(i).getLengthKable())){
                                maxLength = Float.parseFloat(group.get(i).getLengthKable());
                            }
                            //jezeli jest to przew??d wielozy??owy to dadajemy do do mapy inaczej obliczamy
                            if(!group.get(i).getType1().contains("GKW")){
                                    numberW.put(group.get(i).getNameCable(), isolationsCableService.findByTypeIsolationsAndPrzekrojWew(group.get(i).getType1(), group.get(i).getType2()));
                            }else{
                                sum +=  Float.parseFloat(isolationsCable.get(j).getPrzekrojZew()) * Float.parseFloat(isolationsCable.get(j).getPrzekrojZew());
                            }
                        }
                    }
                  //  jezeli niemamy tego typu to dodajemy do listy
                    if(!orTypeCable){
                        error.add("1## brak przewodu typu: " + group.get(i).getType1() + " o przekroju: " + group.get(i).getType2());

                    }

            }
            //obliczanie pola przewodu z izolacja
            for(IsolationsCable k : numberW.values()){

                sum += Float.parseFloat(k.getPrzekrojZew()) * Float.parseFloat(k.getPrzekrojZew());
            }
            double wynik = Math.sqrt(sum);
            //dobranie siatki i czy taka istnieje w bazie
            if(findMesh((int) Math.round(wynik) , color) != null){
                score.add(findMesh((int) Math.round(wynik), color).getNumberProducer());
                score.add(maxLength+"");
            }else{
                error.add("2## Nie dobrano siatki z bazy do ??rednicy: " + (int) Math.round(wynik) + " oraz koloru: " + color );

            }





        }


        return score;
    }


    public Map<Mesh, Float> groupMapE3() throws IOException {
        List<Object[]> data = kabelListService.groupE3();
        Map<Mesh, Float> finalScore = new HashMap<>();
        String GroupExpection = "L://05_KIEROWNICTWO//07_Teamleader//Okninski Dariusz//Ustawienia aplikacji//do uzupelnienia//groupExceptionE3.csv";
        String MultiWireExpection = "L://05_KIEROWNICTWO//07_Teamleader//Okninski Dariusz//Ustawienia aplikacji//do uzupelnienia//multiWireExceptionE3.csv";

        Map<Integer, List<String>> mapExceptions  = excelMenager.getMapFromCSV(GroupExpection);
        Map<Integer, List<List<String>>> groupExpection = new HashMap<>();
        Map<Integer, List<String>> multiWireExpection = excelMenager.getMapFromCSV(MultiWireExpection);

        //tworzenie listy siatek i przypisanym zapotrzebowaniem
        for(Mesh mesh: this.findAll()){
            finalScore.put(mesh, 0.0F);
        }
        //wtorzenie grup z wczytanego pliku z wyjatkami
        int count = 0;
        // list po??acze?? w grupieWyjatk??w
        //tworzenie jednej grupy do znalezienia pustej linijki. po znalezieniu pustej linijkii tworzy nastepna grupe
        List<List<String>> pom = new ArrayList<>();
        for(Map.Entry<Integer, List<String>> x : mapExceptions.entrySet()) {
            if(x.getValue().isEmpty()){
                groupExpection.put(count, pom);
                count ++;
                pom = new ArrayList<>();
            }else{
                pom.add(x.getValue());
            }
        }

        groupExpection.put(count, pom);

        int licznik = 0;
        for (Object[] x : data) {
            // lista wielo??y??owych kt??re ida w siatke
            List<String> multiWire = new ArrayList<>();
            for(Map.Entry<Integer, List<String>> j : multiWireExpection.entrySet()) {
                if(j.getValue().size()!= 0){
                    //jezeli przew??d wielo??y??owy znajdzie swoja grupe to jest on dodawany
                    if(j.getValue().get(0).equals(x[0].toString()) && j.getValue().get(1).equals(x[1].toString()) && j.getValue().get(2).equals(x[2].toString()) && j.getValue().get(3).equals(x[3].toString())) {


                        for(int i = 4; i < j.getValue().size(); i++){

                            multiWire.add(j.getValue().get(i));
                        }
                    }
                }

            }

            boolean flag =true;
            //ustawienie flagi czy grupa wystepuje juz w wyjatkach
            for(Map.Entry<Integer, List<List<String>>> k : groupExpection.entrySet()) {
                for (List<String> j :k.getValue()){
                    if(j.get(0).equals(x[0].toString()) && j.get(1).equals(x[1].toString()) && j.get(2).equals(x[2].toString()) && j.get(3).equals(x[3].toString())) {
                        flag =false;
                    }
                }
            }

            List<KabelList> mesh = kabelListService.findAllByAreaFromAndPositionFromAndAreaToAndPositionTo(x[0].toString(), x[1].toString(), x[2].toString(), x[3].toString());


            //Zliczanie rozmiaru grupy przewod??w jezeli nie wystepuje w wyjatkach
            if(!countsCrossSection(mesh, multiWire).isEmpty() && flag){

                List<String> score = countsCrossSection(mesh, multiWire);

                if(!score.isEmpty()){
                    float val = finalScore.get(findByNumberProducer(score.get(0)));

                    finalScore.replace(findByNumberProducer(score.get(0)), val + Float.parseFloat(score.get(1)));

                    informations.add("Grupa nr: s" + licznik + " Po????czenie: " + mesh + " -- Dobrano siatke: " + findByNumberProducer(score.get(0)) + " d??ugo????: " + score.get(1));
                }
            }
            licznik++;
        }
        licznik =0;
        for(Map.Entry<Integer, List<List<String>>> k : groupExpection.entrySet()) {
            List<String> multiWire = new ArrayList<>();

            List<KabelList> mesh = new ArrayList<>();
            // Laczenie grup z wyjatk??w
            for (List<String> j :k.getValue()){
                List<KabelList> meshTmp = kabelListService.findAllByAreaFromAndPositionFromAndAreaToAndPositionTo(j.get(0), j.get(1), j.get(2),j.get(3));


                for(Map.Entry<Integer, List<String>> x : multiWireExpection.entrySet()) {
                    //jezeli przew??d wielo??y??owy znajdzie swoja grupe to jest on dodawany
                    if(x.getValue().get(0).equals(j.get(0)) && x.getValue().get(1).equals(j.get(1)) && x.getValue().get(2).equals(j.get(2)) && x.getValue().get(3).equals(j.get(3))) {
                        for(int i = 4 ;i<x.getValue().size(); i++){
                            multiWire.add(x.getValue().get(i));
                        }

                    }
                }


                mesh.addAll(meshTmp);
            }
            //Zliczanie rozmiaru grupy przewod??w z wyjatk??w
            if(!countsCrossSection(mesh, multiWire).isEmpty()){
                List<String> score = countsCrossSection(mesh, multiWire);
                float val = finalScore.get(findByNumberProducer(score.get(0)));
                //dodanie wyniku do zapotrzebowania odpowiedniej siatki
                finalScore.replace(findByNumberProducer(score.get(0)), val + Float.parseFloat(score.get(1)));

                informations.add("Grupa nr: e" + licznik + " Po????czenie: " + mesh + " -- Dobrano siatke: " + findByNumberProducer(score.get(0)) + " d??ugo????: " + score.get(1));
            }
        }
        return finalScore;
    }


    public List<String> countsCrossSection(List<KabelList> group, List<String> multiWire){
        boolean orMesh = false;
        List<IsolationsCable> isolationsCable = isolationsCableService.findAll();
        Map<String, IsolationsCable> numberW = new HashMap<>();
        //Map<String, String> missingCable = new HashMap<>();

        String color = "Black";
        List<String> score = new ArrayList<>();

// Sprawdzenie czy jest to przewod jednozy??owy
        float maxLength = 0.0F;
        for(int i = 0; i <group.size(); i++){

            if (isolationsCableService.findByTypeIsolationsAndPrzekrojWew(group.get(i).getType1(), group.get(i).getPrzekrojZyly()) != null ){
                if(!isolationsCableService.findByTypeIsolationsAndPrzekrojWew(group.get(i).getType1(), group.get(i).getPrzekrojZyly()).isMultiWire()){
                    orMesh = true;
                }
            }else{
                //brak kabla

                error.add("1## brak przewodu typu: " + group.get(i).getType1() + " o przekroju: " + group.get(i).getPrzekrojZyly());

            }
        };
        //System.out.println(orMesh + " ----- or mesh");
        if(orMesh){
            float sum = 0;
            for(int i = 0; i <group.size(); i++){

                Boolean orTypeCable = false;
                //sprawdzenie czy siatka powinna byc czarna czy szara
                if(group.get(i).getMesh().contains("Hauptstrom")){
                    color = "Gray";
                }
               // System.out.println(color + " ----- color");
                for(int j = 0; j < isolationsCable.size(); j++){
                    //sprawdzenie czy ten typ przewodu mamy w bazie
                    if(( group.get(i).getType1().trim().equals(isolationsCable.get(j).getTypeIsolations()) ) && ( group.get(i).getPrzekrojZyly().contains(isolationsCable.get(j).getPrzekrojWew()) )){
                        orTypeCable = true;

                        if(group.get(i).getLengthKable().contains(",")){
                            group.get(i).setLengthKable(group.get(i).getLengthKable().replace(",","."));
                        }
                        if( NumberUtils.isParsable(group.get(i).getLengthKable()) && maxLength < Float.parseFloat(group.get(i).getLengthKable())){
                            maxLength = Float.parseFloat(group.get(i).getLengthKable());
                        }
                        //jezeli jest to przew??d wielozy??owy to dadajemy go do mapy inaczej obliczamy
//                        System.out.println(group.get(i).getType1() + " -- " + group.get(i).getPrzekrojZyly() +  " ---------" + isolationsCableService.findByTypeIsolationsAndPrzekrojWew(group.get(i).getType1(), group.get(i).getPrzekrojZyly()).isMultiWire());
                        if(isolationsCableService.findByTypeIsolationsAndPrzekrojWew(group.get(i).getType1(), group.get(i).getPrzekrojZyly()).isMultiWire()){
                 //           System.out.println(multiWire);
                            for(String name : multiWire){

                                if(name.equals(group.get(i).getNameCable())){

                                    numberW.put(name, isolationsCableService.findByTypeIsolationsAndPrzekrojWew(group.get(i).getType1(), group.get(i).getPrzekrojZyly()));
                                }
                            }

                        }else{
                            sum +=  Float.parseFloat(isolationsCable.get(j).getPrzekrojZew()) * Float.parseFloat(isolationsCable.get(j).getPrzekrojZew());
                            //System.out.println(isolationsCable.get(j).getPrzekrojWew());
                        }
                    }
                }
                //  jezeli niemamy tego typu to dodajemy do listy
                if(!orTypeCable){
                    error.add("1## brak przewodu typu: " + group.get(i).getType1() + " o przekroju: " + group.get(i).getPrzekrojZyly());
                }
            }
            //obliczanie pola przewodu z izolacja
            for(IsolationsCable k : numberW.values()){

                sum += Float.parseFloat(k.getPrzekrojZew()) * Float.parseFloat(k.getPrzekrojZew());
            }
            double wynik = Math.sqrt(sum);
            //dobranie siatki i czy taka istnieje w bazie
            if(findMesh((int) Math.round(wynik) , color) != null){
                score.add(findMesh((int) Math.round(wynik), color).getNumberProducer());
                score.add(maxLength+"");
            }else{
                error.add("2## Nie dobrano siatki z bazy do ??rednicy: " + (int) Math.round(wynik) + " oraz koloru: " + color );
            }
        }
        return score;
    }
}