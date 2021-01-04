package com.example.catalog.service.dto;

import com.example.catalog.service.response.MusicBrainzRecordingResp;
import lombok.Data;

@Data
public class MusicBrainzRecordingDTO {

    private String id;
    private String title;

    public MusicBrainzRecordingDTO(MusicBrainzRecordingResp.MusicBrainzRecording recording) {
        this.id = recording.getId();
        this.title = recording.getTitle();
    }
}
