package com.example.pokedex.controller;

import com.example.pokedex.service.PokemonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/pokemon")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;


    @GetMapping("/{pokemonName}")
    public ResponseEntity<Map<String, String>> getPokemonBasicInformation(@PathVariable(value = "pokemonName") String pokemonName){

        log.info(" Calling getPokemonBasicInformation() method in PokemonController....!! ");

        return pokemonService.getPokemonBasicInformation(pokemonName);
    }

}
