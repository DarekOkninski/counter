
package pl.stadler.counter.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    private TermoTube termoTube;
    private ClipLibra clipLibra;
    private List<String> errors;
    private ProjectSettings projectSettings;

    @Override
    public String toString() {
        return "Wrapper{}";
    }
}