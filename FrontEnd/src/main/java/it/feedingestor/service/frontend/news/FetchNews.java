package it.feedingestor.service.frontend.news;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import it.feedingestor.service.frontend.entity.WordSearch;
import it.library.commondata.DataInformation;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FetchNews {

    final RestTemplate restTemplate;

    final ObjectMapper objectMapper;

    public FetchNews(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getNews(WordSearch wordSearch) throws JsonProcessingException {

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("word", wordSearch.getWord());

        UriComponents encode = UriComponentsBuilder.fromUriString("http://localhost:8082/information")
                .pathSegment("{word}")
                .buildAndExpand(uriVariables)
                .encode();

        URI uri = encode.toUri();
        log.info("Url to call:{}", uri.toString());

        List<DataInformation> response = restTemplate.getForObject(uri, List.class);

        log.info("Response:{}", objectMapper.writeValueAsString(response));

        String indented = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(response);

        return indented;

    }
    public String postNews(DataInformation information) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

        /*
        Format payload
         */
            information.setPayload(this.mapPayload(information.getPayload().toString()));
            String bodyJson = objectMapper.writeValueAsString(information);
            log.info("Payload:{}", bodyJson);

            HttpEntity<String> entity = new HttpEntity<String>(bodyJson, headers);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8084/publish");

            URI uri = builder.build(true).toUri();
            log.info("Url to call:{}", uri.toString());

            ResponseEntity<String> response = restTemplate.postForEntity(uri, entity,
                    String.class);
            log.info("Response:{}", response);

            return "OK";
        }
        catch(Exception e){
            log.error("Error sending information", e);
            return e.getMessage();
        }

    }

    private Object mapPayload(String payload){
        TypeReference<HashMap<Object, Object>> typeRef =  new TypeReference<HashMap<Object, Object>>() {};
        try {
            return objectMapper.readValue(payload, typeRef);
        } catch (JsonProcessingException e) {
            return payload;
        }
    }

}
