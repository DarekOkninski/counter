package pl.stadler.counter.models;


import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.h2.util.IntArray;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numberProject;

    private String name;

    private Integer numberWagons;

    private Integer numberVehicles;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private Set<KabelList> kabelListSet;


    public Project() {
    }

    public Project(String numberProject, String name, Integer numberWagons, Integer numberVehicles) {
        this.numberProject = numberProject;
        this.name = name;
        this.numberWagons = numberWagons;
        this.numberVehicles = numberVehicles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberProject() {
        return numberProject;
    }

    public void setNumberProject(String numberProject) {
        this.numberProject = numberProject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberWagons() {
        return numberWagons;
    }

    public void setNumberWagons(Integer numberWagons) {
        this.numberWagons = numberWagons;
    }

    public Integer getNumberVehicles() {
        return numberVehicles;
    }

    public void setNumberVehicles(Integer numberVehicles) {
        this.numberVehicles = numberVehicles;
    }

    public Set<KabelList> getKabelListSet() {
        return kabelListSet;
    }

    public void setKabelListSet(Set<KabelList> kabelListSet) {
        this.kabelListSet = kabelListSet;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", numberProject='" + numberProject + '\'' +
                ", name='" + name + '\'' +
                ", numberWagons=" + numberWagons +
                ", numberVehicles=" + numberVehicles +
                ", kabelListSet=" + kabelListSet +
                '}';
    }
}
