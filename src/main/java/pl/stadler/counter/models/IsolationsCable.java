package pl.stadler.counter.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IsolationsCable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeIsolations;

    private String przekrojWew;

    private String przekrojZew;

//    public IsolationsCable(String typeIsolations, String przekrojWew, String przekrojZew) {
//        this.typeIsolations = typeIsolations;
//        this.przekrojZew = przekrojZew;
//        this.przekrojWew = przekrojWew;
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
//    public String getTypeIsolations() {
//        return typeIsolations;
//    }
//
//    public void setTypeIsolations(String typeIsolations) {
//        this.typeIsolations = typeIsolations;
//    }
//
//    public String getPrzekrojZew() {
//        return przekrojZew;
//    }
//
//    public void setPrzekrojZew(String przekrojZew) {
//        this.przekrojZew = przekrojZew;
//    }
//
//    public String getPrzekrojWew() {
//        return przekrojWew;
//    }
//
//    public void setPrzekrojWew(String przekrojWew) {
//        this.przekrojWew = przekrojWew;
//    }
//
//    @Override
//    public String toString() {
//        return "Isolations{" +
//                "id=" + id +
//                ", typeIsolations='" + typeIsolations + '\'' +
//                ", przekrojZew='" + przekrojZew + '\'' +
//                ", przekrojWew='" + przekrojWew + '\'' +
//                '}';
//    }
}
