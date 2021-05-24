package pl.stadler.counter.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Mesh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color;

    private String name;

    private String minSize;

    private String  maxSize;

    private String numberProducer;

    public Mesh() {
    }

    public Mesh(String color, String name, String minSize, String maxSize, String numberProducer) {
        this.color = color;
        this.name = name;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.numberProducer = numberProducer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinSize() {
        return minSize;
    }

    public void setMinSize(String minSize) {
        this.minSize = minSize;
    }

    public String getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

    public String getNumberProducer() {
        return numberProducer;
    }

    public void setNumberProducer(String numberProducer) {
        this.numberProducer = numberProducer;
    }

    @Override
    public String toString() {
        return "Mesh{" +
                "id=" + id +
                ", color='" + color + '\'' +
                ", name='" + name + '\'' +
                ", minSize='" + minSize + '\'' +
                ", maxSize='" + maxSize + '\'' +
                ", numberProducer='" + numberProducer + '\'' +
                '}';
    }
}
