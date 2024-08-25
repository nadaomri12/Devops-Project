package backend.stageproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class QualityPolicy extends GenericEntity {
    @JsonBackReference(value = "")
    @OneToMany(mappedBy = "axe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Objective> objectiveList;
    private String title;
    @Override
    public void generateCode() {
        if (getTitle() != null && !getTitle().isEmpty()) {
            String[] words = getTitle().split("\\s+"); // Split title by whitespace

            StringBuilder codeBuilder = new StringBuilder("QP-");

            for (String word : words) {
                if (!word.isEmpty()) {
                    codeBuilder.append(word.charAt(0)); // Append the first letter of each word
                }
            }

            this.setCode(codeBuilder.toString().toUpperCase()); // Convert code to uppercase
        } else {
            throw new IllegalStateException("Title is null or empty. Ensure title is set before calling generateCode()");
        }
    }


}
