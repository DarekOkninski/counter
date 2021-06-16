package pl.stadler.counter.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KabelListSettings {


    @ManyToOne
    private Project project;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer descriptionColumnNumber;
    private Integer nameCableColumnNumber;
    private Integer potentialColumnNumber;
    private Integer strangColumnNumber;
    private Integer areaFromColumnNumber;
    private Integer positionFromColumnNumber;
    private Integer pinFromColumnNumber;
    private Integer areaToColumnNumber;
    private Integer positionToColumnNumber;
    private Integer pinToColumnNumber;
    private Integer meshColumnNumber;
    private Integer gelifertColumnNumber;
    private Integer colorColumnNumber;
    private Integer przekrojZylyColumnNumber;
    private Integer type1ColumnNumber;
    private Integer type2ColumnNumber;
    private Integer lengthKableColumnNumber;


}
