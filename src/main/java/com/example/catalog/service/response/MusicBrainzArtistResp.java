package com.example.catalog.service.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MusicBrainzArtistResp {
    private String created;
    private String count;
    private String offset;
    private List<MusicBrainzArtist> artists;

    @Data
    public static class MusicBrainzArtist {
        private String id;
        private String name;
        private String gender;
        private String country;
        @JsonProperty("life-span")
        private LifeSpan lifeSpan;

        @Data
        public static class LifeSpan {
            private String begin;
            private String ended;
        }
    }
}
