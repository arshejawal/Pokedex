package com.example.pokedex.model;

import lombok.*;

/**
 * @apiNote class for Pokemon Basic Information
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pokemon {

    private String name;
    private String is_legendary;
    private Habitat habitat;
    private Flavor_text_entries[] flavor_text_entries;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Flavor_text_entries{
        private String flavor_text;
        private DescriptionLanguage language;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class DescriptionLanguage {
            private String name;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Habitat {

        private String name;

    }
}
