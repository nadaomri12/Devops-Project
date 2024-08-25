
package backend.stageproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "data")
public class Data extends GenericEntity {
    private String designation;
    @JsonBackReference
    @OneToMany(mappedBy = "data", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskData> taskData = new HashSet<>();

    @Override
    public void generateCode() {
    }
}
