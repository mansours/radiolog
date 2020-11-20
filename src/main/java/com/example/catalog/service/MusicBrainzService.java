package com.example.catalog.service;

import com.example.catalog.service.response.MusicBrainzArtistResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MusicBrainzService {
    final private WebClient webClient;

    @Autowired
    public MusicBrainzService(final WebClient webClient) {
        this.webClient = webClient;
    }

    public List<String> searchArtist(String query) {
        try {
            MusicBrainzArtistResp resp = webClient.get()
                    .uri("https://musicbrainz.org/ws/2/artist/?query=" + query + "&fmt=json")
                    .retrieve()
                    .bodyToMono(MusicBrainzArtistResp.class)
                    .block(Duration.ofSeconds(30L));
            assert resp != null;

            //Map to version and implementation agnostic POJO in case specs change, write business logic and unit tests against agnostic POJO
            return resp.getArtists().stream()
                    .map(x -> x.getName())
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
