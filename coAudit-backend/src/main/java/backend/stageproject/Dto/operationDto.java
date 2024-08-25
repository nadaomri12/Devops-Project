package backend.stageproject.Dto;

import backend.stageproject.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class operationDto {
    private String designation;
    private String  responsibility;
    private String responsible;
    private UUID process;
}
