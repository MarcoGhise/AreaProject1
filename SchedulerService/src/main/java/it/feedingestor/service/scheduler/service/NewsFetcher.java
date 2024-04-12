package it.feedingestor.service.scheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.feedingestor.service.scheduler.entity.BbcNews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class NewsFetcher {

    final RestTemplate restTemplate;

    final ObjectMapper objectMapper;


    public NewsFetcher(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public BbcNews getNewsFromBbcHeadline() throws JsonProcessingException {

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=9acc642023684f07b46fae89185513ce");

            URI uri = builder.build(true).toUri();
            log.info("Url to call:{}", uri.toString());

            ResponseEntity<BbcNews> response = restTemplate.exchange(uri, HttpMethod.GET, null,  BbcNews.class);

            log.info("Response:{}", objectMapper.writeValueAsString(response.getBody()));

            return response.getBody();
    }
}
