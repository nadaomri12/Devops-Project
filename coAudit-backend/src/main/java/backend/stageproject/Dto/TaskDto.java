package backend.stageproject.Dto;

import backend.stageproject.Entity.TypeData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String designation;
    private UUID operation;
    private List<String> dataDesignations;


    // private List<DataDto> dataDesignations;
   //  private List<TypeData> typeDataList;
}
