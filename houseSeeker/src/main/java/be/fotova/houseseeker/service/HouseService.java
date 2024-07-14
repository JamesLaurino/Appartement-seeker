package be.fotova.houseseeker.service;

import be.fotova.houseseeker.constant.Energy;
import be.fotova.houseseeker.dto.HouseDto;
import be.fotova.houseseeker.dto.RestResponse;
import be.fotova.houseseeker.entity.HouseEntity;
import be.fotova.houseseeker.repository.HouseRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class HouseService
{

    @Autowired
    private HouseRepository houseRepository;

    public HouseDto getInformation(String url)
    {
        try {

            Document doc = Jsoup.connect(url).get();
            String dataLayerScript = getDataLayerScript(doc);

            if (dataLayerScript != null) {
                JSONArray dataLayerArray = extractDataLayerArray(dataLayerScript);
                RestResponse restResponse = getAgenceAndConstructYear(dataLayerArray);
                HouseDto houseDto = new HouseDto();
                this.getAll(url,houseDto);
                houseDto.setAgency(restResponse.getAgence());
                houseDto.setConstructYear(restResponse.getConstructYear());
                houseDto.setUrl(url);
                return houseDto;

            } else {
                System.out.println("Le script window.dataLayer n'a pas été trouvé sur la page.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public HouseDto insertHouse(HouseDto houseDto)
    {
        try
        {
            List<HouseEntity> houseEntityList = houseRepository.findAll();
            HouseEntity houseEntity = this.mapToHouseEntity(houseDto);

            if(houseEntityList.stream()
                    .anyMatch(house -> house.getUrl().equals(house.getUrl())))
            {
                System.out.println("Cet appartement existe déjà");
                return null;
            }
            else
            {
                this.houseRepository.save(houseEntity);
                return houseDto;
            }
        }
        catch (Exception e)
        {
            System.out.println("Une erreur est survenue : " + e.getMessage());
            return null;
        }
    }

    public List<HouseDto> getAll()
    {
        return this.houseRepository.findAll()
                .stream().map(this::mapToHouseDto).toList();
    }

    private HouseDto mapToHouseDto(HouseEntity houseEntity)
    {
        HouseDto houseDto = new HouseDto();
        houseDto.setAgency(houseEntity.getAgency());
        houseDto.setType(houseEntity.getType());
        houseDto.setPeb(houseEntity.getPeb());
        houseDto.setUrl(houseEntity.getUrl());
        houseDto.setCity(houseEntity.getCity());
        houseDto.setDimension(houseEntity.getDimension());
        houseDto.setNbrRooms(houseEntity.getNbrRooms());
        houseDto.setPostalCode(houseEntity.getPostalCode());
        houseDto.setConstructYear(houseEntity.getConstructYear());
        houseDto.setPricePrincipal(houseEntity.getPricePrincipal());
        houseDto.setPriceRent(houseEntity.getPriceRent());
        return houseDto;
    }

    private HouseEntity mapToHouseEntity(HouseDto houseDto)
    {
            HouseEntity houseEntity = new HouseEntity();
            houseEntity.setAgency(houseDto.getAgency());
            houseEntity.setPeb(houseDto.getPeb());
            houseEntity.setUrl(houseDto.getUrl());
            houseEntity.setType(houseDto.getType());
            houseEntity.setCity(houseDto.getCity());
            houseEntity.setDimension(houseDto.getDimension());
            houseEntity.setNbrRooms(houseDto.getNbrRooms());
            houseEntity.setPostalCode(houseDto.getPostalCode());
            houseEntity.setConstructYear(houseDto.getConstructYear());
            houseEntity.setPricePrincipal(houseDto.getPricePrincipal());
            houseEntity.setPriceRent(houseDto.getPriceRent());
            return houseEntity;
    }

    private String getDataLayerScript(Document doc) {

        Elements scripts = doc.getElementsByTag("script");
        for (Element script : scripts) {
            String scriptContent = script.html();
            if (scriptContent.contains("window.dataLayer")) {
                return scriptContent;
            }
        }
        return null;
    }

    private JSONArray extractDataLayerArray(String dataLayerScript) {

        int start = dataLayerScript.indexOf('[');
        int end = dataLayerScript.lastIndexOf(']') + 1;
        String jsonContent = dataLayerScript.substring(start, end);

        return new JSONArray(jsonContent);
    }

    private RestResponse getAgenceAndConstructYear(JSONArray dataLayerArray) {

        RestResponse rest = new RestResponse();
        for (int i = 0; i < dataLayerArray.length(); i++) {
            JSONObject obj = dataLayerArray.getJSONObject(i);
            System.out.println(obj);

            String classifiedId = obj.getJSONObject("customer").getString("name");
//            String classifiedType = obj.getJSONObject("classified").getString("type");
//            String classifiedPrice = obj.getJSONObject("classified").getString("price");
            String classifiedConstructionYear = obj.getJSONObject("classified").getJSONObject("building").getString("constructionYear");

            rest.setConstructYear(classifiedConstructionYear);
            rest.setAgence(classifiedId);

        }

        return rest;
    }

    private void getAll(String url, HouseDto houseDto) throws IOException {

        //HouseDto houseDto = new HouseDto();
        Document doc = Jsoup.connect(url).get();

        String title = doc.title();
        String[] listes = title.split("-");

        List<Element> peb = doc.body().getElementsByClass("classified-table__data");

        for (int i = 0; i < listes.length; i++)
        {
            if(i== 0) // type
            {
                String[] getType = listes[0].split(" ");
                System.out.println("Type : " + getType[0]);
                houseDto.setType(getType[0]);

            }
            else if(i == 1) // price
            {
                String price = "";
                List<String> listePrices = new ArrayList<>();
                if(listes[i].contains("+"))
                {
                    String[] prices = listes[i].split("\\+");
                    for(String priceUnit : prices)
                    {
                        for (int j = 0; j < priceUnit.length() ; j++) {
                            if(Character.isDigit(priceUnit.charAt(j)))
                            {
                                price += priceUnit.charAt(j);
                            }
                        }
                        listePrices.add(price);
                        price = "";
                    }

                    System.out.println("Price : " + listePrices.get(0) + " " + "Price 2 : " + listePrices.get(1));
                    // houseDto.setPricePrincipal(Integer.parseInt(listePrices.get(0)));
                    houseDto.setPricePrincipal(listePrices.get(0));
                    // houseDto.setPriceRent(Integer.parseInt(listePrices.get(1)));
                    houseDto.setPriceRent(listePrices.get(1));
                }
                else
                {
                    for (int j = 0; j < listes[i].length() ; j++) {
                        if(Character.isDigit(listes[i].charAt(j)))
                        {
                            price += listes[i].charAt(j);
                        }
                    }
                    listePrices.add(price);
                    System.out.println("Price : " + listePrices.get(0));
                    // houseDto.setPricePrincipal(Integer.parseInt(listePrices.get(0)));
                    houseDto.setPricePrincipal(listePrices.get(0));
                    // houseDto.setPriceRent(0);
                    houseDto.setPriceRent("");
                }
            }
            else if(i == 2) // nbr chambre
            {
                for (int j = 0; j < listes[i].length() ; j++) {
                    if(Character.isDigit(listes[i].charAt(j)))
                    {
                        System.out.print("Nombre de chambres : " + listes[i].charAt(j));
                       // houseDto.setNbrRooms(listes[i].charAt(j));
                        houseDto.setNbrRooms(String.valueOf(listes[i].charAt(j)));
                    }
                }
            } else if (i == 3) // nb mettre carré
            {
                System.out.println("");
                String mettreCarre = "";
                for (int j = 0; j < listes[i].length() ; j++) {
                    if(Character.isDigit(listes[i].charAt(j)))
                    {
                        mettreCarre += listes[i].charAt(j);
                    }
                }

                System.out.println("Mettre carré: " + mettreCarre);
                //houseDto.setDimension(Integer.parseInt(mettreCarre));
                houseDto.setDimension(mettreCarre);
            }
        }

        String[] parseUrl = url.split("/");
        System.out.println("Code postal : " + parseUrl[parseUrl.length-2]);
        houseDto.setPostalCode(parseUrl[parseUrl.length-2]);

        String ville = parseUrl[parseUrl.length-3];
        ville =  ville.substring(0, 1).toUpperCase() + ville.substring(1);
        System.out.println("Ville : " + ville);
        houseDto.setCity(ville);

        Boolean isSet = false;

        for(Element item : peb)
        {
            switch (item.text())
            {
                case("A"):
                    System.out.println("PEB : " + "A");
                    houseDto.setPeb("A");
                    isSet=true;
                    break;
                case("B"):
                    System.out.println("PEB : " + "B");
                    houseDto.setPeb("B");
                    isSet=true;
                    break;
                case("C"):
                    System.out.println("PEB : " + "C");
                    houseDto.setPeb("C");
                    isSet=true;
                    break;
                case("D"):
                    System.out.println("PEB : " + "D");
                    houseDto.setPeb("D");
                    isSet=true;
                    break;
                case("E"):
                    System.out.println("PEB : " + "E");
                    houseDto.setPeb("E");
                    isSet=true;
                    break;
                case("F"):
                    houseDto.setPeb("F");
                    System.out.println("PEB : " + "F");
                    isSet=true;
                    break;
                case("G"):
                    System.out.println("PEB : " + "G");
                    houseDto.setPeb("G");
                    isSet=true;
                    break;
                default:
                    houseDto.setPeb("NAN");
                    break;
            }

            if(isSet == true)
            {
                break;
            }
        }
        //return houseDto;
    }
}
