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
public class InstructionHarting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameBone;
    private String areaBone;
    private boolean orOneBone;

    private String boneNumber;
    private String gender;

    private String modulA;
    private String modulB;
    private String modulC;
    private String modulD;
    private String modulE;
    private String modulF;
    private String contact;
    private String additionalMaterial;
    @ManyToOne
    private Project project;

    @Override
    public String toString() {
        return "InstructionHarting{" +
                "id=" + id +
                ", nameBone='" + nameBone + '\'' +
                ", areaBone='" + areaBone + '\'' +
                ", orOneBone=" + orOneBone +
                ", boneNumber='" + boneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", modulA='" + modulA + '\'' +
                ", modulB='" + modulB + '\'' +
                ", modulC='" + modulC + '\'' +
                ", modulD='" + modulD + '\'' +
                ", modulE='" + modulE + '\'' +
                ", modulF='" + modulF + '\'' +
                ", contact='" + contact + '\'' +
                ", additionalMaterial='" + additionalMaterial + '\'' +
                ", project=" + project.getNumberProject() +
                '}';
    }
}
