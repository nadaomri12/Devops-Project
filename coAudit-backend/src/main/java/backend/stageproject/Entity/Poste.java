package backend.stageproject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Poste extends GenericEntity {
    private String designation;

    @OneToMany(mappedBy = "responsibility", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Operation> operationSet = new HashSet<>();

    @OneToMany(mappedBy = "poste", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<User> Userlist = new HashSet<>();
    @Override
    public void generateCode() {
    }
}
