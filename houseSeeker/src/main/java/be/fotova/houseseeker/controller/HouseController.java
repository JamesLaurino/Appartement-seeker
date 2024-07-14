package be.fotova.houseseeker.controller;

import be.fotova.houseseeker.dto.HouseDto;
import be.fotova.houseseeker.dto.UserInput;
import be.fotova.houseseeker.repository.HouseRepository;
import be.fotova.houseseeker.service.DataService;
import be.fotova.houseseeker.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/house")
@CrossOrigin("*")
public class HouseController
{
    @Autowired
    HouseService houseService;

    @Autowired
    DataService dataService;

    @Autowired
    HouseRepository houseRepository;

    @PostMapping
    public HouseDto getDataFromHouse(@RequestBody UserInput userInput)
    {
        return houseService.getInformation(userInput.getUrl());
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertDataHouse(@RequestBody HouseDto houseUserDto)
    {
        HouseDto houseDto = houseService.insertHouse(houseUserDto);

        if(houseDto == null)
        {
            return ResponseEntity.status(404).body("Une erreur est survenue pendant l'insertion");
        }

        return ResponseEntity.ok("Insertion des données réussies");
    }

    @GetMapping
    public List<HouseDto> getDataFromHouse()
    {
        return houseService.getAll();
    }

    @GetMapping("/export")
    public ResponseEntity<FileSystemResource> exportDataToCsv() throws IOException {

        dataService.retrieveData();
        String filePath = "C:/Users/thoma/OneDrive/Bureau/informatique/Java/maven-projet/Gestion-Appart-app/houseSeeker/src/main/java/be/fotova/houseseeker/files/testing.csv";
        File file = new File(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());

        return new ResponseEntity<>(new FileSystemResource(file), headers, HttpStatus.OK);
    }
}