package backend.stageproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "tasks")
@Builder

public class Task extends GenericEntity {

    private String designation;
    @JsonBackReference(value = "")
    @ManyToOne
    @JoinColumn(name = "operation_id")
    private Operation operation;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskData> taskData = new HashSet<>();

    @Override
    public void generateCode() {
    }
}