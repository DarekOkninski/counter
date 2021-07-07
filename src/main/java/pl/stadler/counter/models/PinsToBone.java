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
public class PinsToBone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BoneHarting boneHarting;

    private String namePin;
    private String color;
    private float size;
    private String numberProducer;
    private String stadlerId;

    @Override
    public String toString() {
        return "PinsToBone{" +
                "id=" + id +
                ", boneHarting=" + boneHarting.getNumberProducer() +
                ", namePin='" + namePin + '\'' +
                ", color='" + color + '\'' +
                ", size=" + size +
                ", numberProducer='" + numberProducer + '\'' +
                ", stadlerId='" + stadlerId + '\'' +
                '}';
    }
}
