package in.artamonov.utilitybill.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AuthResponseDto {

    private Long userId;
    private String username;
    private String token;
    private Date issuedAt;
    private Date expiresAt;

}
