package backend.stageproject.Dto;

import backend.stageproject.Entity.TypeData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDto {
    private String designation;
    private UUID task;
    private TypeData type;
}