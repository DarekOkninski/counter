package pl.stadler.counter.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoneHarting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String numberProducer;
    private Integer size;


    @JsonIgnore
    @OneToMany(mappedBy = "boneHarting")
    private Set<PinsToBone> pinsToBoneSet;

}
