package backend.stageproject.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Company  extends GenericEntity {
    private String name;

    @Lob
   private byte[] image;



    private  Long phone;
    private String address;
    @OneToOne
    @JoinColumn(name = "user_id")
    private  User responsible ;


    @Override
    public void generateCode() {
        // Vérification et génération du code de la société
        if (name != null && name.length() >= 2 && phone != null) {
            String companyCode = name.substring(0, 2).toUpperCase();
            String phonePrefix = String.valueOf(phone).substring(0, 3); // Prend les deux premiers chiffres du numéro de téléphone
            this.setCode(companyCode + "-" + phonePrefix);
        } else {
            throw new IllegalArgumentException("Le nom de la société doit contenir au moins deux caractères et un numéro de téléphone valide.");
        }
    }

}
