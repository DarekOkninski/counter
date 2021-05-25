package pl.stadler.counter.models;


import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.h2.util.IntArray;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numberProject;

    private String name;

    private String changeProject;

    private Integer numberVehicles;

    private Integer descriptionColumnNumber;
    private Integer nameCableColumnNumber;
    private Integer potentialColumnNumber;
    private Integer strangColumnNumber;
    private Integer positionFromColumnNumber;
    private Integer pinFromColumnNumber;
    private Integer positionToColumnNumber;
    private Integer pinToColumnNumber;
    private Integer meshColumnNumber;
    private Integer gelifertColumnNumber;
    private Integer colorColumnNumber;
    private Integer przekrojZylyColumnNumber;
    private Integer type1ColumnNumber;
    private Integer type2ColumnNumber;
    private Integer lengthKableColumnNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private Set<KabelList> kabelListSet;


}
