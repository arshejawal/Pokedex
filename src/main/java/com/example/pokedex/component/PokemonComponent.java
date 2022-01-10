package com.example.pokedex.component;

import com.example.pokedex.model.Pokemon;
import com.example.pokedex.model.TranslatedDescription;
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

        return inputString.replaceAll("[\\n\\r\\t\\f]+", " ");
    }


    /**
     * This method return Translated Description for Pokemon.
     * @param basicDetails - Basic Information Details by calling Pokemon API
     * @param restTemplate - To communicate with Translation service API
     */
    public String getTranslatedDescription(ResponseEntity<Pokemon> basicDetails,RestTemplate restTemplate){

        log.info(" Calling getTranslatedDescription() method in PokemonComponent....!! ");

        final String translatedDescription;
        final TranslatedDescription translatedDescContents;
        final String habitatName;
        final String is_legendary;

        habitatName = basicDetails.getBody().getHabitat().getName();
        is_legendary = basicDetails.getBody().getIs_legendary();

        String standardDescription = this.getStandardDescription(basicDetails);

        /**
         * Call to Shakespeare translator / Yoda translator API for Translation of Pokemon Description
         */
        final String shakespeareUrl = PokemonConstants.SHAKESPEARE_TRANSLATOR_URL+"?text="+standardDescription;
        final String yodaUrl = PokemonConstants.YODA_TRANSLATOR_URL+"?text="+standardDescription;


        if ((null != habitatName && habitatName.equals(PokemonConstants.POKEMON_HABITAT_CAVE))
                || (null != is_legendary && is_legendary.equals(PokemonConstants.POKEMON_IS_LEGENDARY_TRUE))){

            translatedDescContents = restTemplate.getForObject(yodaUrl, TranslatedDescription.class);

        }else{
            translatedDescContents = restTemplate.getForObject(shakespeareUrl, TranslatedDescription.class);
        }
        /**
         * End
         */


        translatedDescription=translatedDescContents.getContents().getTranslated();

        return translatedDescription;
    }

}
