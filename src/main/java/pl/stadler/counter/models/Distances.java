package pl.stadler.counter.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Distances {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numberHarting;

    public Distances() {
    }

    public Distances(String numberHarting) {
        this.numberHarting = numberHarting;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberHarting() {
        return numberHarting;
    }

    public void setNumberHarting(String numberHarting) {
        this.numberHarting = numberHarting;
    }

    @Override
    public String toString() {
        return "Distances{" +
                "id=" + id +
                ", numberHarting='" + numberHarting + '\'' +
                '}';
    }
}
