package pl.stadler.counter.models;


import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.h2.util.IntArray;

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

    private String typ;


    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private Set<KabelList> kabelListSet;
    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private Set<KabelListSettings> kabelListSettingsSet;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private Set<AparatList> aparatListSet;
    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private Set<AparatListSettings> aparatListSettingsSet;

}
