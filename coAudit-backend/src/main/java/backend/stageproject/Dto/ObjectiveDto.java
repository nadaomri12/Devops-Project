package backend.stageproject.Dto;

import backend.stageproject.Entity.QualityPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectiveDto {
    private String designation;
    private String axe;
    private UUID process;


}
