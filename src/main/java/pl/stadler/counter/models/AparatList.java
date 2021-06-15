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
public class AparatList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    private String position;
    private String area;
    private String count;
    private String function;
    private String typ;
    private String description;
    private String numberProducer;
    private String nameProducer;
    private String schema;
    private String stadlerNr;


    @Override
    public String toString() {
        return "AparatList{" +
                ", project=" + project.getNumberProject() +
                ", position='" + position + '\'' +
                ", area='" + area + '\'' +
                ", count='" + count + '\'' +
                ", function='" + function + '\'' +
                ", typ='" + typ + '\'' +
                ", description='" + description + '\'' +
                ", numberProducer='" + numberProducer + '\'' +
                ", nameProducer='" + nameProducer + '\'' +
                ", schema='" + schema + '\'' +
                ", stadlerNr='" + stadlerNr + '\'' +
                '}';
    }
}
