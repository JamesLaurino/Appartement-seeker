package be.fotova.houseseeker.dto;

import be.fotova.houseseeker.constant.Format;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class UserDataToExport
{
    private String path;
    private Format format; // csv, xml, json
}
