package backend.stageproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

public class Process extends GenericEntity {


    private String title;
    private String version;
    //@JsonBackReference(value = "")
    @OneToMany(mappedBy = "process",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Objective> objectiveList;
    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Operation> operationList;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User pilot;

    @Override
    public void generateCode() {
        if (getTitle() != null && !getTitle().isEmpty()) {
            String[] words = getTitle().split("\\s+"); // Split title by whitespace

            StringBuilder codeBuilder = new StringBuilder("Pr-");

            if (words.length == 1) {
                // If there is only one word in the title, take the first three letters
                String firstWord = words[0];
                if (firstWord.length() >= 3) {
                    codeBuilder.append(firstWord.substring(0, 3).toUpperCase());
                } else {
                    codeBuilder.append(firstWord.toUpperCase());
                }
            } else {
                // Otherwise, take the first letter of each word
                for (String word : words) {
                    if (!word.isEmpty()) {
                        codeBuilder.append(word.charAt(0)); // Append the first letter of each word
                    }
                }
            }

            this.setCode(codeBuilder.toString().toUpperCase()); // Convert code to uppercase
        } else {
            throw new IllegalStateException("Title is null or empty. Ensure title is set before calling generateCode()");
        }
    }


}