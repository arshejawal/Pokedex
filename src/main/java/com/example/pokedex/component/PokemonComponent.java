package com.example.pokedex.component;

import com.example.pokedex.model.Pokemon;
import com.example.pokedex.utils.PokemonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class PokemonComponent {


    /**
     * This method return Basic Information for Pokemon.
     * @param pokemonName - Name of Pokemon
     * @param restTemplate  -  To communicate with Pokemon API service
     */
    public ResponseEntity<Pokemon> getPokemonBasicInformation(String pokemonName, RestTemplate restTemplate){

        log.info(" Calling getPokemonBasicInformation() method in PokemonComponent....!! ");

        final ResponseEntity<Pokemon> basicDetails ;

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        basicDetails = restTemplate.exchange(PokemonConstants.POKEMON_API_URL +pokemonName, HttpMethod.GET,entity,Pokemon.class);

        return basicDetails;
    }


    /**
     * This method return Standard Description of Pokemon.
     * @param basicDetails - Basic Information Details by calling Pokemon API
     */
    public String getStandardDescription(ResponseEntity<Pokemon> basicDetails){

        log.info(" Calling getStandardDescription() method in PokemonComponent....!! ");

        final Pokemon.Flavor_text_entries[] flavor_text_entries;

        flavor_text_entries =basicDetails.getBody().getFlavor_text_entries();

        String standardDescription = Arrays.stream(flavor_text_entries)
                .filter(fte -> fte.getLanguage().getName().equals(PokemonConstants.POKEMON_DESCRIPTION_LANGUAGE_ENGLISH))
                .map(Pokemon.Flavor_text_entries::getFlavor_text)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Pokemon Description not found "));

        return this.removeSpecialCharacters(standardDescription);
    }


    /**
     * This method return Custom Generated Response.
     * @param basicDetails - Basic Information Details by calling Pokemon API
     * @param description - Pokemon Description (Standard/Translated)
     */
    public ResponseEntity<Map<String, String>> generateResponse(ResponseEntity<Pokemon> basicDetails, String description){

        log.info(" Calling generateResponse() method in PokemonComponent....!! ");

        final Map<String, String> map = new LinkedHashMap<String, String>();

        map.put("name", basicDetails.getBody().getName());
        map.put("description", description);
        map.put("habitat", basicDetails.getBody().getHabitat().getName());
        map.put("isLegendary", basicDetails.getBody().getIs_legendary());

        return ResponseEntity.ok().body(map);
    }


    /**
     * This method removes special characters from input String.
     * @param inputString - Input String
     */
    public String removeSpecialCharacters(String inputString){

        return inputString.replaceAll("[^a-zA-Z0-9]", " ");
    }

}
