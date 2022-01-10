package com.example.pokedex.component;

import com.example.pokedex.model.Pokemon;
import com.example.pokedex.model.TranslatedDescription;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PokemonComponentTest {

    @Mock
    private RestTemplate restTemplate;


    @InjectMocks
    private PokemonComponent pokemonComponent;

    ResponseEntity<Pokemon> basicDetails;
    Pokemon pokemon;
    Pokemon.Flavor_text_entries[] flavor_text_entries;
    Pokemon.Habitat habitat;
    ResponseEntity<Map<String, String>> customisedResponse;
    String standardDescription;
    String translatedDescription;
    String pokemonName;
    String pokemonUrl;
    String shakespeareUrl;
    String yodaUrl;
    Map<String, String> map;
    TranslatedDescription translatedDescContents;
    TranslatedDescription.Contents contents;


    @BeforeEach
    void setDataUp() {

        pokemonName="mewtwo";
        pokemonUrl="https://pokeapi.co/api/v2/pokemon-species/mewtwo";
        standardDescription="Master Obiwan has lost a planet";
        translatedDescription= "Master obiwan hath did lose a planet";
        shakespeareUrl="https://api.funtranslations.com/translate/shakespeare.json?text="+standardDescription;
        yodaUrl ="https://api.funtranslations.com/translate/yoda.json?text="+standardDescription;
        flavor_text_entries =
        new Pokemon.Flavor_text_entries[]{new Pokemon.Flavor_text_entries(standardDescription, new Pokemon.Flavor_text_entries.DescriptionLanguage("en"))};

        habitat =new Pokemon.Habitat();
        habitat.setName("rare");

        pokemon = new Pokemon();
        pokemon.setName(pokemonName);
        pokemon.setIs_legendary("true");
        pokemon.setHabitat(habitat);
        pokemon.setFlavor_text_entries(flavor_text_entries);

        basicDetails =new ResponseEntity<Pokemon>(pokemon, HttpStatus.OK);

    }

    @Test
    @Order(1)
    public void test_getPokemonBasicInformation(){

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(pokemonUrl,HttpMethod.GET,entity,Pokemon.class)).thenReturn(basicDetails);

        ResponseEntity<Pokemon> responseEntity = pokemonComponent.getPokemonBasicInformation(pokemonName,restTemplate);

        assertAll( "Get Basic Information for Pokemon - PokemonComponent Test",
                () -> assertEquals(basicDetails, responseEntity),
                () -> assertEquals("200 OK", responseEntity.getStatusCode().toString()),
                () -> assertEquals(pokemonName, responseEntity.getBody().getName()),
                () -> verify(restTemplate, times(1)).exchange(pokemonUrl,HttpMethod.GET,entity,Pokemon.class)

        );
    }


    @Test
    @Order(2)
    public void test_getStandardDescription(){

        String standardDescriptionResult = pokemonComponent.getStandardDescription(basicDetails);

        assertAll( "Get Standard Description for Pokemon - PokemonComponent Test",
                () -> assertNotNull(standardDescriptionResult),
                () -> assertEquals(standardDescription, standardDescriptionResult)

        );
    }

    @Test
    @Order(3)
    public void test_getStandardDescription_IsNull() {

        pokemon = new Pokemon();
        pokemon.setName(pokemonName);
        pokemon.setIs_legendary("true");
        pokemon.setHabitat(habitat);
        pokemon.setFlavor_text_entries(null);

        basicDetails =new ResponseEntity<Pokemon>(pokemon, HttpStatus.OK);

        assertThrows(NullPointerException.class, () -> {
            pokemonComponent.getStandardDescription(basicDetails);
        });

    }



    @Test
    @Order(4)
    public void test_generateResponse_BasicInformation(){

        map = new LinkedHashMap<String, String>();
        map.put("name", "mewtwo");
        map.put("description",standardDescription);
        map.put("habitat", "rare");
        map.put("isLegendary","true");
        customisedResponse =new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);

        ResponseEntity<Map<String, String>> responseEntity = pokemonComponent.generateResponse(basicDetails,standardDescription);

        assertAll( "Generate Response for Pokemon Basic Information  - PokemonComponent Test",
                () -> assertEquals(customisedResponse, responseEntity),
                () -> assertNotNull(responseEntity),
                () -> assertEquals("200 OK", responseEntity.getStatusCode().toString())

        );
    }

    @Test
    @Order(5)
    public void test_generateResponse_TranslatedDescription(){

        map = new LinkedHashMap<String, String>();
        map.put("name", "mewtwo");
        map.put("description",translatedDescription);
        map.put("habitat", "rare");
        map.put("isLegendary","true");
        customisedResponse =new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);



        ResponseEntity<Map<String, String>> responseEntity = pokemonComponent.generateResponse(basicDetails,translatedDescription);

        assertAll( "Generate Response for Pokemon Translated Description  - PokemonComponent Test",
                () -> assertEquals(customisedResponse, responseEntity),
                () -> assertNotNull(responseEntity),
                () -> assertEquals("200 OK", responseEntity.getStatusCode().toString())

        );
    }


    @Test
    @Order(6)
    public void test_getTranslatedDescription_shakespeareTranslation(){

        habitat =new Pokemon.Habitat();
        habitat.setName("rare");

        pokemon = new Pokemon();
        pokemon.setName(pokemonName);
        pokemon.setIs_legendary("false");
        pokemon.setHabitat(habitat);
        pokemon.setFlavor_text_entries(flavor_text_entries);

        basicDetails =new ResponseEntity<Pokemon>(pokemon, HttpStatus.OK);

        contents= new TranslatedDescription.Contents();
        contents.setText(standardDescription);
        contents.setTranslated("Master obiwan hath did lose a planet");
        contents.setTranslation("shakespeare");

        translatedDescContents = new TranslatedDescription();
        translatedDescContents.setContents(contents);

        when(restTemplate.getForObject(shakespeareUrl, TranslatedDescription.class)).thenReturn(translatedDescContents);

        String translatedDescriptionResult = pokemonComponent.getTranslatedDescription(basicDetails,restTemplate);

        assertAll( "Get Shakespeare Translated Description for Pokemon - PokemonComponent Test",
                () -> assertNotNull(translatedDescriptionResult),
                () -> assertEquals(translatedDescContents.getContents().getTranslated(), translatedDescriptionResult)

        );
    }


    @Test
    @Order(7)
    public void test_getTranslatedDescription_yodaTranslation(){

        habitat =new Pokemon.Habitat();
        habitat.setName("cave");

        pokemon = new Pokemon();
        pokemon.setName(pokemonName);
        pokemon.setIs_legendary("true");
        pokemon.setHabitat(habitat);
        pokemon.setFlavor_text_entries(flavor_text_entries);

        basicDetails =new ResponseEntity<Pokemon>(pokemon, HttpStatus.OK);

        contents= new TranslatedDescription.Contents();
        contents.setText(standardDescription);
        contents.setTranslated("Lost a planet,  master obiwan has");
        contents.setTranslation("yoda");

        translatedDescContents = new TranslatedDescription();
        translatedDescContents.setContents(contents);

        when(restTemplate.getForObject(yodaUrl, TranslatedDescription.class)).thenReturn(translatedDescContents);

        String translatedDescriptionResult = pokemonComponent.getTranslatedDescription(basicDetails,restTemplate);

        assertAll( "Get Yoda Translated Description for Pokemon - PokemonComponent Test",
                () -> assertNotNull(translatedDescriptionResult),
                () -> assertEquals(translatedDescContents.getContents().getTranslated(), translatedDescriptionResult)

        );
    }


}