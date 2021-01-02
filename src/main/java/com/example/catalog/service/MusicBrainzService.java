package com.example.catalog.service;

import com.example.catalog.service.dto.MusicBrainzArtistDTO;
import com.example.catalog.service.dto.MusicBrainzRecordingDTO;
import com.example.catalog.service.response.MusicBrainzArtistResp;
import com.example.catalog.service.response.MusicBrainzRecordingResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MusicBrainz Service interface.
 * <p>
 * https://musicbrainz.org/doc/Development
 * https://musicbrainz.org/doc/MusicBrainz_API
 */
@Slf4j
@Service
public class MusicBrainzService {
    final private WebClient webClient;

    @Autowired
    public MusicBrainzService(final WebClient webClient) {
        this.webClient = webClient;
    }

    public List<MusicBrainzArtistDTO> searchArtist(String query) {
        try {
            //Example: https://musicbrainz.org/ws/2/artist/?query=kesha&fmt=json

            MusicBrainzArtistResp resp = webClient.get()
                    .uri("https://musicbrainz.org/ws/2/artist/?query=" + query + "&fmt=json")
                    .retrieve()
                    .bodyToMono(MusicBrainzArtistResp.class)
                    .block(Duration.ofSeconds(30L));

            assert resp != null;

            //Map to version and implementation agnostic POJO in case specs change, write business logic and unit tests against agnostic POJO
            return resp.getArtists().stream()
                    .map(MusicBrainzArtistDTO::new)
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode().is4xxClientError())
                log.error("Error {} - MusicBrainz Service - Get artist. Query: '{}'.", e.getRawStatusCode(), query);
            else if (e.getStatusCode().is5xxServerError())
                log.error("Error {} trying to reach MusicBrainz Service API .", e.getRawStatusCode());
            else
                log.error(e.getMessage(), e);
        } catch (Exception e2) {
            log.error(e2.getMessage(), e2);
        }
        return Collections.emptyList(); //Exception reached
    }

    public List<MusicBrainzRecordingDTO> searchRecordings(String artistId) {
        try {
            MusicBrainzRecordingResp resp = webClient.get()
                    .uri("https://musicbrainz.org/ws/2/recording?query=arid:" + artistId + "&fmt=json")
                    .retrieve()
                    .bodyToMono(MusicBrainzRecordingResp.class)
                    .block(Duration.ofSeconds(30L));
            assert resp != null;

            //Map to version and implementation agnostic POJO in case specs change, write business logic and unit tests against agnostic POJO
            return resp.getRecordings().stream()
                    .map(MusicBrainzRecordingDTO::new)
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode().is4xxClientError())
                log.error("Error {} - MusicBrainz Service - Get recordings. Artist Id: '{}'.", e.getRawStatusCode(), artistId);
            else if (e.getStatusCode().is5xxServerError())
                log.error("Error {} trying to reach MusicBrainz Service API.", e.getRawStatusCode());
            else
                log.error(e.getMessage(), e);
        } catch (Exception e2) {
            log.error(e2.getMessage(), e2);
        }
        return Collections.emptyList(); //Exception reached
    }

    public List<MusicBrainzRecordingDTO> getRecording(String recordingId) {
        try {
            MusicBrainzRecordingResp resp = webClient.get()
                    .uri("https://musicbrainz.org/ws/2/recording?query=arid:" + recordingId + "&fmt=json")
                    .retrieve()
                    .bodyToMono(MusicBrainzRecordingResp.class)
                    .block(Duration.ofSeconds(30L));
            assert resp != null;

            //Map to version and implementation agnostic POJO in case specs change, write business logic and unit tests against agnostic POJO
            return resp.getRecordings().stream()
                    .map(MusicBrainzRecordingDTO::new)
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode().is4xxClientError())
                log.error("Error {} - MusicBrainz Service - Get recordings. Recording Id: '{}'.", e.getRawStatusCode(), recordingId);
            else if (e.getStatusCode().is5xxServerError())
                log.error("Error {} trying to reach MusicBrainz Service API.", e.getRawStatusCode());
            else
                log.error(e.getMessage(), e);
        } catch (Exception e2) {
            log.error(e2.getMessage(), e2);
        }
        return Collections.emptyList(); //Exception reached
    }

}
