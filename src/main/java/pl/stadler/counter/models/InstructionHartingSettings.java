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
public class InstructionHartingSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer nameBoneColumnNumber;
    private Integer areaBoneColumnNumber;
    private Integer genderColumnNumber;
    private Integer orOneBoneColumnNumber;
    private Integer boneNumberColumnNumber;
    private Integer modulAColumnNumber;
    private Integer modulBColumnNumber;
    private Integer modulCColumnNumber;
    private Integer modulDColumnNumber;
    private Integer modulEColumnNumber;
    private Integer modulFColumnNumber;
    private Integer contactColumnNumber;
    private Integer additionalMaterialColumnNumber;

    @ManyToOne
    private Project project;
}
