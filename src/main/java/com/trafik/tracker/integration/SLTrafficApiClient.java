package com.trafik.tracker.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trafik.tracker.integration.model.LineDataApiResponse;
import com.trafik.tracker.integration.model.LineJourney;
import com.trafik.tracker.integration.model.LinesResponse;
import com.trafik.tracker.integration.model.StopResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class SLTrafficApiClient {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(SLTrafficApiClient.class);

    @Value("${lines.url}")
    private String linesUrl;
    @Value("${stops.url}")
    private  String stopsUrl ;

    @Value("${journey.response.stub.file}")
    private String journeyResponseStubFile;

    @Autowired
    public SLTrafficApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LinesResponse fetchLinesResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<LinesResponse> responseEntity = restTemplate.exchange(
                linesUrl,
                HttpMethod.GET,
                requestEntity,
                LinesResponse.class);
        return responseEntity.getBody();
    }

    public Optional<List<StopResponse>> fetchStopsResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<StopResponse>> responseEntity = restTemplate.exchange(
                    stopsUrl,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>() {}
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(responseEntity.getBody());
            } else {
                logger.error("Failed to fetch stop responses: Status code {}", responseEntity.getStatusCode());
                return Optional.empty();
            }
        } catch (RestClientResponseException e) {
            logger.error("Error fetching stop responses: {}", e.getMessage());
            return Optional.empty();
        }
    }


    public Optional<LineDataApiResponse<LineJourney>> fetchJourneyData() {
        try {
            Resource resource = new ClassPathResource(journeyResponseStubFile, this.getClass().getClassLoader());

            // Read the JSON content from the resource
            ObjectMapper objectMapper = new ObjectMapper();
            LineDataApiResponse<LineJourney> response = objectMapper.readValue(resource.getInputStream(),
                    new TypeReference<>() {
                    });
            return Optional.ofNullable(response);

        } catch (IOException e) {
            logger.error("Error reading lines responses: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
