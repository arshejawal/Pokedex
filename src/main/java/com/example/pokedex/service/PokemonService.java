package com.example.pokedex.service;

import com.example.pokedex.component.PokemonComponent;
import com.example.pokedex.model.Pokemon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class PokemonService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PokemonComponent pokemonComponent;


    public ResponseEntity<Map<String, String>> getPokemonBasicInformation(String pokemonName) {

        log.info(" Calling getPokemonBasicInformation() method in PokemonService....!! ");

        final ResponseEntity<Pokemon> basicDetails ;
        final ResponseEntity<Map<String, String>> customisedResponse;
        final String standardDescription;

        basicDetails = pokemonComponent.getPokemonBasicInformation(pokemonName, restTemplate);

        standardDescription=pokemonComponent.getStandardDescription(basicDetails);

        customisedResponse = pokemonComponent.generateResponse(basicDetails,standardDescription);

        return customisedResponse;
    }


    public ResponseEntity<Map<String, String>> getPokemonTranslatedInformation(String pokemonName) {

        log.info(" Calling getPokemonTranslatedDescription() method in PokemonService....!! ");

        final ResponseEntity<Pokemon> basicDetails ;
        final ResponseEntity<Map<String, String>> customisedResponse;
        final String translatedDescription;

        basicDetails = pokemonComponent.getPokemonBasicInformation(pokemonName, restTemplate);

        translatedDescription=pokemonComponent.getTranslatedDescription(basicDetails,restTemplate);

        customisedResponse = pokemonComponent.generateResponse(basicDetails,translatedDescription);

        return customisedResponse;
    }



}
