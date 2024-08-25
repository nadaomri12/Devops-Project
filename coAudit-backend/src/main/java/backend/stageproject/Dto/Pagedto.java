package backend.stageproject.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagedto<T>  {
    private List<T> content;
    private long totalElements;
}
