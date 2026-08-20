package com.example.MEEK.services;

import com.example.MEEK.ItunesSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

@Service
public class ItunesService {
    private RestClient restClient;
    private ObjectMapper objectMapper;

    public ItunesService(RestClient.Builder builder, ObjectMapper objectMapper){
        restClient = builder.baseUrl("https://itunes.apple.com/search").build();
        this.objectMapper = objectMapper;
    }

    public ItunesSearchResponse searchQuery(String query){
        try {
            String jsonResponse = restClient.get().uri(uriBuilder ->
                            uriBuilder.queryParam("term",query.trim())
                                    .queryParam("media","music")
                                    .queryParam("limit",20)
                                    .build())
                    .retrieve()
                    .body(String.class);
            return objectMapper.readValue(jsonResponse, ItunesSearchResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
