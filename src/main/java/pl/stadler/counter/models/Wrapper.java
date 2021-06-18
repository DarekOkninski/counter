
package pl.stadler.counter.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}