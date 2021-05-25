package pl.stadler.counter.models;

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
public class KabelList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    private String description;

    private String nameCable;

    private String potential;


    private String strang;

    private String positionFrom;

    private String pinFrom;

    private String positionTo;

    private String pinTo;

    private String mesh;

    private String gelifert;

    private String color;

    private String przekrojZyly;

//    @ManyToOne
//    private ClipLibra type1;
    private String type1;

    private String type2;

    private String lengthKable;


}
