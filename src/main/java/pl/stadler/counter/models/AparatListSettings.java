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
public class AparatListSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    private Integer positionColumnNumber;
    private Integer areaColumnNumber;
    private Integer countColumnNumber;
    private Integer functionColumnNumber;
    private Integer typColumnNumber;
    private Integer descriptionColumnNumber;
    private Integer numberProducerColumnNumber;
    private Integer nameProducerColumnNumber;
    private Integer schemaColumnNumber;
    private Integer stadlerNrColumnNumber;

}
