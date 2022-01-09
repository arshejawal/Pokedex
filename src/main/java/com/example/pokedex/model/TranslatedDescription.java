package com.example.pokedex.model;

import lombok.*;

/**
 * @apiNote class for Pokemon Translated Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TranslatedDescription {

    private Contents contents;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Contents {

        private String translated;
        private String text;
        private String translation;
    }
}