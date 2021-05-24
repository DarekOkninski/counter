package pl.stadler.counter.models;

import javax.persistence.*;


@Entity
public class KabelList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    private String description;

    private String x;

    private String potential;


    private String strang;

    private String areaFrom;

    private String x1;

    private String positionFrom;

    private String x2;

    private String pinFrom;

    private String areaTo;

    private String x3;

    private String positionTo;

    private String x4;

    private String pinTo;

    private String mesh;
    private String x5;
    private String x6;
    private String x7;
    private boolean gelifert;

    private String x8;

    private String color;

    private String mm;

    @ManyToOne
    private ClipLibra type1;

    private String type2;

    private String lengthKable;

    private String schema;

    private String revision;

    @Override
    public String toString() {
        return "KabelList{" +
                "id=" + id +
                ", project=" + project +
                ", description='" + description + '\'' +
                ", x='" + x + '\'' +
                ", potential='" + potential + '\'' +
                ", strang='" + strang + '\'' +
                ", areaFrom='" + areaFrom + '\'' +
                ", x1='" + x1 + '\'' +
                ", positionFrom='" + positionFrom + '\'' +
                ", x2='" + x2 + '\'' +
                ", pinFrom='" + pinFrom + '\'' +
                ", areaTo='" + areaTo + '\'' +
                ", x3='" + x3 + '\'' +
                ", positionTo='" + positionTo + '\'' +
                ", x4='" + x4 + '\'' +
                ", pinTo='" + pinTo + '\'' +
                ", mesh='" + mesh + '\'' +
                ", x5='" + x5 + '\'' +
                ", x6='" + x6 + '\'' +
                ", x7='" + x7 + '\'' +
                ", gelifert=" + gelifert +
                ", x8='" + x8 + '\'' +
                ", color='" + color + '\'' +
                ", mm='" + mm + '\'' +
                ", type1='" + type1 + '\'' +
                ", type2='" + type2 + '\'' +
                ", lengthKable='" + lengthKable + '\'' +
                ", schema='" + schema + '\'' +
                ", revision='" + revision + '\'' +
                '}';
    }
}
