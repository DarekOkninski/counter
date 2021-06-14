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

    private String type1;

    private String type2;

    private String lengthKable;

    @Override
    public String toString() {
        return "KabelList{" +
                ", description='" + description + '\'' +
                ", nameCable='" + nameCable + '\'' +
                ", potential='" + potential + '\'' +
                ", strang='" + strang + '\'' +
                ", positionFrom='" + positionFrom + '\'' +
                ", pinFrom='" + pinFrom + '\'' +
                ", positionTo='" + positionTo + '\'' +
                ", pinTo='" + pinTo + '\'' +
                ", mesh='" + mesh + '\'' +
                ", gelifert='" + gelifert + '\'' +
                ", color='" + color + '\'' +
                ", przekrojZyly='" + przekrojZyly + '\'' +
                ", type1='" + type1 + '\'' +
                ", type2='" + type2 + '\'' +
                ", lengthKable='" + lengthKable + '\'' +
                '}';
    }
}
    //