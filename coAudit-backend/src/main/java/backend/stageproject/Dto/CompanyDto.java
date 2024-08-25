package backend.stageproject.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private String name;
    private  Long phone;
    private String address;
    private String responsible;


}
