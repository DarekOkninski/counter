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
public class ClipLibra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameClip;

    private String  typCable;
    private String  diameterCable;
    private String  clipNumber;
    private String  diameterClip;


    @JsonIgnore
    @OneToMany(mappedBy = "type1")
    private Set<KabelList> cableNumer;




//    public ClipLibra() {
//    }
//
//    public ClipLibra(String nameClip  ,String typCable, String diameterCable, String clipNumber, String diameterClip) {
//        this.nameClip = nameClip;
//        this.typCable = typCable;
//        this.diameterCable = diameterCable;
//        this.clipNumber = clipNumber;
//        this.diameterClip = diameterClip;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Set<KabelList> getCableNumer() {
//        return cableNumer;
//    }
//
//    public void setCableNumer(Set<KabelList> cableNumer) {
//        this.cableNumer = cableNumer;
//    }
//
//    public String getTypCable() {
//        return typCable;
//    }
//
//    public void setTypCable(String typCable) {
//        this.typCable = typCable;
//    }
//
//    public String getDiameterCable() {
//        return diameterCable;
//    }
//
//    public void setDiameterCable(String diameterCable) {
//        this.diameterCable = diameterCable;
//    }
//
//    public String getClipNumber() {
//        return clipNumber;
//    }
//
//    public void setClipNumber(String clipNumber) {
//        this.clipNumber = clipNumber;
//    }
//
//    public String getDiameterClip() {
//        return diameterClip;
//    }
//
//    public void setDiameterClip(String diameterClip) {
//        this.diameterClip = diameterClip;
//    }
//
//    @Override
//    public String toString() {
//        return "ClipLibra{" +
//                "id=" + id +
////                ", cableNumer=" + cableNumer +
//                ", typCable='" + typCable + '\'' +
//                ", diameterCable='" + diameterCable + '\'' +
//                ", clipNumber='" + clipNumber + '\'' +
//                ", diameterClip='" + diameterClip + '\'' +
//                '}';
//    }
}
