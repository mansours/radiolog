package com.example.catalog.service;

import com.example.catalog.service.response.MusicBrainzArtistResp;
import com.example.catalog.service.response.MusicBrainzRecordingResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class MusicBrainzService {
    final private WebClient webClient;

    @Autowired
    public MusicBrainzService(final WebClient webClient) {
        this.webClient = webClient;
    }

    public Map<String,String> searchArtist(String query) {
        try {
            MusicBrainzArtistResp resp = webClient.get()
                    .uri("https://musicbrainz.org/ws/2/artist/?query=" + query + "&fmt=json")
                    .retrieve()
                    .bodyToMono(MusicBrainzArtistResp.class)
                    .block(Duration.ofSeconds(30L));

            assert resp != null;

            //Map to version and implementation agnostic POJO in case specs change, write business logic and unit tests against agnostic POJO
            return resp.getArtists().stream().collect(Collectors.toMap(x -> x.getName(), x -> x.getId()));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode().is4xxClientError())
                log.error("Error {} - User Management - Get supervisors. Logged in user {}.", e.getRawStatusCode(), query);
            else if (e.getStatusCode().is5xxServerError())
                log.error("Error {} trying to reach User Management API service.", e.getRawStatusCode());
            else
                log.error(e.getMessage(), e);
        } catch (Exception e2) {
            log.error(e2.getMessage(), e2);
        }
        return null; //Exception reached
    }
    public List<String> searchRecording(String query) {
        try {
            MusicBrainzRecordingResp resp = webClient.get()
                    .uri("https://musicbrainz.org/ws/2/recording?query=arid:" + query + "&fmt=json")
                    .retrieve()
                    .bodyToMono(MusicBrainzRecordingResp.class)
                    .block(Duration.ofSeconds(30L));
            assert resp != null;

            //Map to version and implementation agnostic POJO in case specs change, write business logic and unit tests against agnostic POJO
            return resp.getRecordings().stream()
                    .map(x -> x.getTitle())
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode().is4xxClientError())
                log.error("Error {} - User Management - Get supervisors. Logged in user {}.", e.getRawStatusCode(), query);
            else if (e.getStatusCode().is5xxServerError())
                log.error("Error {} trying to reach User Management API service.", e.getRawStatusCode());
            else
                log.error(e.getMessage(), e);
        } catch (Exception e2) {
            log.error(e2.getMessage(), e2);
        }
        return null; //Exception reached
    }

}
