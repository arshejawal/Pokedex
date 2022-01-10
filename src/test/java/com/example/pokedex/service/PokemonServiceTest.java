package com.example.pokedex.service;

import com.example.pokedex.component.PokemonComponent;
import com.example.pokedex.model.Pokemon;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PokemonServiceTest {

    @Mock
    PokemonComponent pokemonComponent;

    @InjectMocks
    PokemonService pokemonService;

    ResponseEntity<Pokemon> basicDetails;
    RestTemplate restTemplate;
    Pokemon pokemon;
    Pokemon.Flavor_text_entries[] flavor_text_entries;
    Pokemon.Habitat habitat;
    ResponseEntity<Map<String, String>> customisedResponse;
    String standardDescription;
    String translatedDescription;
    String pokemonName;

    Map<String, String> map;


    @BeforeEach
    void setDataUp() {

        pokemonName="mewtwo";
        standardDescription="Master Obiwan has lost a planet";
        translatedDescription= "Master obiwan hath did lose a planet";
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


        map = new LinkedHashMap<String, String>();
        map.put("name", "mewtwo");
        map.put("description",standardDescription);
        map.put("habitat", "rare");
        map.put("isLegendary","true");

        customisedResponse =new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);

        when(pokemonComponent.getPokemonBasicInformation(pokemonName,restTemplate)).thenReturn(basicDetails);

        when(pokemonComponent.getStandardDescription(basicDetails)).thenReturn(standardDescription);

        when(pokemonComponent.generateResponse(basicDetails,standardDescription)).thenReturn(customisedResponse);

        ResponseEntity<Map<String, String>> responseEntity = pokemonService.getPokemonBasicInformation(pokemonName);

        assertAll( "Get Pokemon Basic Information - PokemonService Test",
                () -> assertEquals(customisedResponse,responseEntity),
                () -> assertTrue(responseEntity.getBody().size()>0),
                () -> assertEquals("200 OK", responseEntity.getStatusCode().toString()),
                () -> verify(pokemonComponent, times(1)).getPokemonBasicInformation(pokemonName,restTemplate),
                () -> verify(pokemonComponent, times(1)).getStandardDescription(basicDetails),
                () -> verify(pokemonComponent, times(1)).generateResponse(basicDetails,standardDescription)
        );
    }


    @Test
    @Order(2)
    public void test_getPokemonTranslatedInformation(){

        map = new LinkedHashMap<String, String>();
        map.put("name", "mewtwo");
        map.put("description",translatedDescription);
        map.put("habitat", "rare");
        map.put("isLegendary","true");

        customisedResponse =new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);

        when(pokemonComponent.getPokemonBasicInformation(pokemonName,restTemplate)).thenReturn(basicDetails);

        when(pokemonComponent.getTranslatedDescription(basicDetails, restTemplate)).thenReturn(translatedDescription);

        when(pokemonComponent.generateResponse(basicDetails,translatedDescription)).thenReturn(customisedResponse);

        ResponseEntity<Map<String, String>> responseEntity = pokemonService.getPokemonTranslatedInformation(pokemonName);

        assertAll( "Get Pokemon Translated Description - PokemonService Test",
                () -> assertEquals(customisedResponse,responseEntity),
                () -> assertTrue(responseEntity.getBody().size()>0),
                () -> assertEquals("200 OK", responseEntity.getStatusCode().toString()),
                () -> verify(pokemonComponent, times(1)).getPokemonBasicInformation(pokemonName,restTemplate),
                () -> verify(pokemonComponent, times(1)).getTranslatedDescription(basicDetails, restTemplate),
                () -> verify(pokemonComponent, times(1)).generateResponse(basicDetails,translatedDescription)
        );
    }

}