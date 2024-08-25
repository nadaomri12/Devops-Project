package backend.stageproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "task_data")
public class TaskData  extends GenericEntity {

    @Id
    @GeneratedValue
    private UUID id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    @ManyToOne

    @JoinColumn(name = "data_id")
    private Data data;
    @Enumerated(EnumType.STRING)
    private TypeData type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskData taskData = (TaskData) o;
        return Objects.equals(task.getId(), taskData.task.getId()) &&
                Objects.equals(data.getId(), taskData.data.getId()) &&
                Objects.equals(type, taskData.type);
    }
    public void generateCode(){

    }
    @Override
    public int hashCode() {
        return Objects.hash(task.getId(), data.getId(), type);
    }
}