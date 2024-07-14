package be.fotova.houseseeker.service;

import be.fotova.houseseeker.dto.UserDataToExport;
import be.fotova.houseseeker.entity.HouseEntity;
import be.fotova.houseseeker.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class DataService {

    @Autowired
    HouseRepository houseRepository;

    public void retrieveData() throws IOException {

        List<HouseEntity> houseList = houseRepository.findAll();
        String csvFilePath = "C:/Users/thoma/OneDrive/Bureau/informatique/Java/maven-projet/Gestion-Appart-app/houseSeeker/src/main/java/be/fotova/houseseeker/files/testing.csv";

        FileWriter fileWriter = new FileWriter(csvFilePath);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        writer.write("ID, CITY, PEB, DIMENSION, AGENCY, CONTRUCT-YEAR, POSTAL-CODE, PRICE-1, PRICE-2, TYPE, URL, ROOMS");
        writer.newLine();

        for (var entity : houseList) {
            writer.append(entity.getId() + ", "
                + entity.getCity() + ", "
                + entity.getPeb() + ", "
                + entity.getDimension() + ", "
                + entity.getAgency() + ", "
                + entity.getConstructYear() + ", "
                + entity.getPostalCode() + ", "
                + entity.getPricePrincipal() + ", "
                + entity.getPriceRent() + ", "
                + entity.getType() + ", "
                + entity.getUrl() + ", "
                + entity.getNbrRooms() + "\n");

        }

        writer.flush();
        writer.close();
    }
}
