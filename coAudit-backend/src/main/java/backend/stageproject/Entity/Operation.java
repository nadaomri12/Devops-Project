package backend.stageproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Operation  extends GenericEntity{
    private String designation;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User responsible;

    @ManyToOne
    @JoinColumn(name = "poste_id")
    private Poste  responsibility;
    @ManyToOne
    @JsonBackReference(value = "")
    @JoinColumn(name = "process_id")
    private Process process;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
    @Override
    public void generateCode() {

    }

}