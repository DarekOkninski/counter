
package pl.stadler.counter.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wrapper {
    private Mesh mesh;
    private Float amountFloat;
    private Integer amountInteger;
//    private TermoTube termoTube;
    private ClipLibra clipLibra;
    //private PinsToBone pinsToBone;
    //private String numberProducer;
    private Map<TermoTube, Integer> termoTube;
    private Map<Mesh, Float> groupMap;
    private Map<String, Integer> finalScore;
    private LinkedHashSet<String> errors;
    private LinkedHashSet<String> informations;
    private ProjectSettings projectSettings;


    @Override
    public String toString() {
        return "Wrapper{" +
                "mesh=" + mesh +
                ", amountFloat=" + amountFloat +
                ", amountInteger=" + amountInteger +
                ", termoTube=" + termoTube +
                ", clipLibra=" + clipLibra +
                ", groupMap=" + groupMap +
                //", numberProducer='" + numberProducer + '\'' +
                ", finalScore=" + finalScore +
                ", errors=" + errors +
                ", informations=" + informations +
                ", projectSettings=" + projectSettings +
                '}';
    }
}