package backend.stageproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Objective extends GenericEntity {

    private String designation;
    @ManyToOne

    @JoinColumn(name = "qualityPolicy_id")
    private QualityPolicy axe;
    @ManyToOne
    @JsonBackReference(value = "")
    @JoinColumn(name = "process_id")
    private Process process;

    @Override
    public void generateCode() {

    }
}