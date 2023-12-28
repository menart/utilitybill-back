package in.artamonov.utilitybill.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class AuthRequestDto {

    private String username;
    private String password;
}
