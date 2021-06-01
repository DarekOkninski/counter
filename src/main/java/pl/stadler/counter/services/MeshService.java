package pl.stadler.counter.services;

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

    public Mesh findByColorMinMax(String color, String min, String max) {
        return meshRepository.findByColorAndMinSizeAndMaxSize(color, min, max).orElse(null);
    }

    public Mesh save(Mesh mesh) {
        return meshRepository.save(mesh);
    }


    public List<Object[]> groupMap(String fileLocation) throws IOException {

        Map<Integer, List<KabelList>> group = new HashMap<>();
        List<Object[]> data = kabelListService.mesh();
//        List<KabelList> mesh = new ArrayList<KabelList>();
        Map<Integer, List<String>> mapExceptions  = excelMenager.getMapFromCSV(fileLocation);
        int i = 0;

        for (Object[] x : data) {
            if (!x[0].toString().endsWith("99")) {
                //System.out.println(x[0] + " --- " + x[1] + " --- " + x[2]);
                //try {
                    List<KabelList> mesh = kabelListService.findAllByStrangAndPositionFromAndPositionTo(x[0].toString(), x[1].toString(), x[2].toString());
                    countsCrossSection(mesh);
                //}catch(Exception e){
                    //System.out.println("Brak tego rodzaju przewodu w bazie prosze dodac przewód: ");
                //}
            }
        }

        //System.out.println(mapExceptions);

        return data;
    }

    public void countsCrossSection(List<KabelList> group){
        boolean orMesh = false;
        List<IsolationsCable> isolationsCable = isolationsCableService.findAll();
        Map<String, String> numberW = new HashMap<>();
        Map<String, String> missingCable = new HashMap<>();
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
                //System.out.print(group.get(i).getStrang() +"  ------  " + group.get(i).getType1() + "   ------   " + group.get(i).getType2());
//                System.out.println("");
                for(int j = 0; j < isolationsCable.size(); j++){


                    if(( group.get(i).getType1().contains(isolationsCable.get(j).getTypeIsolations()) ) && ( group.get(i).getType2().contains(isolationsCable.get(j).getPrzekrojWew()) )){
                        orTypeCable = true;
                        if(maxLength < Float.parseFloat(group.get(i).getLengthKable())){
                            maxLength = Float.parseFloat(group.get(i).getLengthKable());
                        }
                        if(group.get(i).getType1().contains("L+S")){
                                numberW.put(group.get(i).getNameCable(), group.get(i).getType1());
                        }else{
                            sum +=  Float.parseFloat(isolationsCable.get(j).getPrzekrojZew()) * Float.parseFloat(isolationsCable.get(j).getPrzekrojZew());
                        }
                    }

                }
                if(!orTypeCable){
                    missingCable.put(group.get(i).getType1(), group.get(i).getType2());
                }



        }
//        numberW.forEach((key, value) ->{});

        for(String k : numberW.values()){
            sum += Float.parseFloat(isolationsCableService.findByTypeIsolations(k).getPrzekrojZew()) * Float.parseFloat(isolationsCableService.findByTypeIsolations(k).getPrzekrojZew());
        }
//            System.out.println(" ////////////////////////////////////////////////////////////////////////////////// " + key + " "+value);


        System.out.println(group.get(0).getStrang() + "   " + group.get(0).getPositionFrom() + "   " + group.get(0).getPositionTo() + "   --------------------------  " +sum + " dlu: " +maxLength);
        missingCable.forEach((key, value) -> {
            System.out.println(key + "   ---  " + value);
        });
        }
    }
}



//    public Map<String, List<KabelList>> strangMap() {
//
//        Map<String, List<KabelList>> strang = new HashMap<>();
//        Map<String, List<KabelList>> strang2 = new HashMap<>();
//        List<KabelList> kabelLists = kabelListService.findAll();
//        Set<String> setStrang = new HashSet<>();
//        Set<String> setPosition = new HashSet<>();
//
//        Set<String> setStrangTmp = new HashSet<>();
//
//        kabelLists.forEach(x -> setStrang.add(x.getStrang()));
//        for (String v : setStrang) {
//            if (!v.endsWith("99")) {
//                setStrangTmp.add(v);
//            }
//
//        }
//        setStrangTmp.forEach(x -> {
//
//            List<KabelList> z = kabelLists.stream().filter(v -> v.getStrang().equals(x)).collect(Collectors.toList());
//
//            strang.put(x, z);
//        });
//        return strang;
//    }