package pl.stadler.counter.services;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.excel.ExcelMenager;
import pl.stadler.counter.models.IsolationsCable;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.repositories.MeshRepository;

import java.io.IOException;
import java.util.*;

@Service
public class MeshService {

    private final MeshRepository meshRepository;
    private final KabelListService kabelListService;
    private final ExcelMenager excelMenager;
    private final IsolationsCableService isolationsCableService;

    @Autowired
    public MeshService(MeshRepository meshRepository, KabelListService kabelListService, ExcelMenager excelMenager, IsolationsCableService isolationsCableService) {
        this.meshRepository = meshRepository;
        this.kabelListService = kabelListService;
        this.excelMenager = excelMenager;
        this.isolationsCableService = isolationsCableService;
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


    public Map<Mesh, Float> groupMap(String fileLocation) throws IOException {
        Map<Mesh, Float> finalScore = new HashMap<>();

        Map<Integer, List<List<String>>> groupExpection = new HashMap<>();
        List<Object[]> data = kabelListService.mesh();

        Map<Integer, List<String>> mapExceptions  = excelMenager.getMapFromCSV(fileLocation);
        for(var Mesh : this.findAll()){
            finalScore.put(Mesh, 0.0F);
        }
        int count = 0;
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

        //groupExpection.forEach((key, value) -> System.out.println(key + " --- " + value));


        int i = 0;


        for (Object[] x : data) {

            if (!x[0].toString().endsWith("99")) {
                boolean flag =true;
                for(Map.Entry<Integer, List<List<String>>> k : groupExpection.entrySet()) {
                    for (List<String> j :k.getValue()){
                        if(j.get(0).equals(x[0].toString()) && j.get(1).equals(x[1].toString()) && j.get(2).equals(x[2].toString())) {
                            flag =false;
                        }
                    }
                }
                List<KabelList> mesh = kabelListService.findAllByStrangAndPositionFromAndPositionTo(x[0].toString(), x[1].toString(), x[2].toString());


                if(!countsCrossSection(mesh).isEmpty() && flag){

                    List<String> score = countsCrossSection(mesh);

                    float val = finalScore.get(findByNumberProducer(score.get(0)));

                    finalScore.replace(findByNumberProducer(score.get(0)), val + Float.parseFloat(score.get(1)));
                }
            }
        }
        for(Map.Entry<Integer, List<List<String>>> k : groupExpection.entrySet()) {
            List<KabelList> mesh = new ArrayList<>();
            for (List<String> j :k.getValue()){
                List<KabelList> meshTmp = kabelListService.findAllByStrangAndPositionFromAndPositionTo(j.get(0), j.get(1), j.get(2));
                mesh.addAll(meshTmp);
            }
            if(!countsCrossSection(mesh).isEmpty()){
                List<String> score = countsCrossSection(mesh);
                float val = finalScore.get(findByNumberProducer(score.get(0)));
                finalScore.replace(findByNumberProducer(score.get(0)), val + Float.parseFloat(score.get(1)));
            }
        }


        finalScore.forEach((key, value) -> System.out.println(key.getName() + "  --  " + key.getNumberProducer() + "  --  " + value));

        return finalScore;
    }

    public List<String> countsCrossSection(List<KabelList> group){
        boolean orMesh = false;
        List<IsolationsCable> isolationsCable = isolationsCableService.findAll();
        Map<String, IsolationsCable> numberW = new HashMap<>();
        Map<String, String> missingCable = new HashMap<>();

        String color = "Black";
        List<String> score = new ArrayList<>();


        float maxLength = 0.0F;
        for(int i = 0; i <group.size(); i++){
            if (group.get(i).getType1().contains("GKW") && (!group.get(i).getType2().contains("x"))){
                orMesh = true;
            }
        };
        if(orMesh){
            float sum = 0;
            for(int i = 0; i <group.size(); i++){

                    Boolean orTypeCable = false;

                    if(group.get(i).getMesh().contains("Hauptstrom")){
                        color = "Gray";
                    }
                    for(int j = 0; j < isolationsCable.size(); j++){
                        if(( group.get(i).getType1().trim().equals(isolationsCable.get(j).getTypeIsolations()) ) && ( group.get(i).getType2().contains(isolationsCable.get(j).getPrzekrojWew()) )){
                            orTypeCable = true;
                            if(group.get(i).getLengthKable().contains(",")){
                                group.get(i).setLengthKable(group.get(i).getLengthKable().replace(",","."));
                            }
                            if( NumberUtils.isParsable(group.get(i).getLengthKable()) && maxLength < Float.parseFloat(group.get(i).getLengthKable())){
                                maxLength = Float.parseFloat(group.get(i).getLengthKable());
                            }

                            if(!group.get(i).getType1().contains("GKW")){
                                    numberW.put(group.get(i).getNameCable(), isolationsCableService.findByTypeIsolationsAndPrzekrojWew(group.get(i).getType1(), group.get(i).getType2()));
                            }else{
                                sum +=  Float.parseFloat(isolationsCable.get(j).getPrzekrojZew()) * Float.parseFloat(isolationsCable.get(j).getPrzekrojZew());
                            }
                        }
                    }
                    if(!orTypeCable){
                        missingCable.put(group.get(i).getType1(), group.get(i).getType2());
                    }
            }
            for(IsolationsCable k : numberW.values()){

                sum += Float.parseFloat(k.getPrzekrojZew()) * Float.parseFloat(k.getPrzekrojZew());
            }
            double wynik = Math.sqrt(sum);
            if(findMesh((int) Math.round(wynik) , color) != null){
                score.add(findMesh((int) Math.round(wynik), color).getNumberProducer());
                score.add(maxLength+"");
            }else{
                System.out.println("Nie dobrano siatki z bazy do średnicy" + (int) Math.round(wynik) + " oraz koloru " + color );
            }



            missingCable.forEach((key, value) -> {
                System.out.println(key + "  " + value);
            });


        }


        return score;
    }


    public Map<Mesh, Float> groupMapE3(String fileLocation) throws IOException {
        List<Object[]> data = kabelListService.groupE3();
        Map<Mesh, Float> finalScore = new HashMap<>();
        Map<Integer, List<String>> mapExceptions  = excelMenager.getMapFromCSV(fileLocation);
        Map<Integer, List<List<String>>> groupExpection = new HashMap<>();
        for(var Mesh : this.findAll()){
            finalScore.put(Mesh, 0.0F);
        }
        int count = 0;
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

        for (Object[] x : data) {

            boolean flag =true;
            for(Map.Entry<Integer, List<List<String>>> k : groupExpection.entrySet()) {
                for (List<String> j :k.getValue()){
                    if(j.get(0).equals(x[0].toString()) && j.get(1).equals(x[1].toString()) && j.get(2).equals(x[2].toString()) && j.get(3).equals(x[3].toString())) {
                        flag =false;
                    }
                }
            }
            System.out.println(x[0].toString() + " --- " + x[1].toString() + " --- " + x[2].toString() + " --- " + x[3].toString());
            List<KabelList> mesh = kabelListService.findAllByPositionFromAndPinFromAndPositionToAndPinTo(x[0].toString(), x[1].toString(), x[2].toString(), x[3].toString());

            for (KabelList kabelList : mesh) {
               System.out.println(kabelList.toString());

            }
            if(!countsCrossSection(mesh).isEmpty() && flag){

                List<String> score = countsCrossSection(mesh);

                float val = finalScore.get(findByNumberProducer(score.get(0)));

                finalScore.replace(findByNumberProducer(score.get(0)), val + Float.parseFloat(score.get(1)));
            }
        }



        return finalScore;
    }

//    RADOX 3 GKW 600V
//    RADOX TENUIS-TW 600V M
//    RADOX 4 GKW-AX 1800V M
//    RADOX 3 GKW 600V FR


}