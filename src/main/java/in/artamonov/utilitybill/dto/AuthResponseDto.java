package in.artamonov.utilitybill.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class AuthResponseDto {

    private Long userId;
    private String token;
    private Date issuedAt;
    private Date expiresAt;

}
