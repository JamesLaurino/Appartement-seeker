package be.fotova.houseseeker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
public class RestResponse
{
    private String agence;
    private String constructYear;
}
