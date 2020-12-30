package com.example.catalog.service.response;

import lombok.Data;

import java.util.List;

@Data
public class MusicBrainzRecordingResp {
    private String created;
    private String count;
    private String offset;
    private List<MusicBrainzRecording> recordings;


    @Data
    public static class MusicBrainzRecording {
        private String id;
        private String title;
        private String arid;

    }
}
