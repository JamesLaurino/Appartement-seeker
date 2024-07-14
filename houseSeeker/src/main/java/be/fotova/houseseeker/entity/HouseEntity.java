package be.fotova.houseseeker.entity;

import be.fotova.houseseeker.constant.Energy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class HouseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
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
