package be.fotova.houseseeker.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
@Getter @Setter
public class HouseDto
{
    private String type;
    private String pricePrincipal;
    private String priceRent;
    private String postalCode;
    private String city;
    private String nbrRooms;
    private String dimension;
    private String url;
    private String peb;
    private String agency;
    private String constructYear;
}
