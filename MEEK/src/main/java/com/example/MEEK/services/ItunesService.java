package com.example.MEEK.services;

import com.example.MEEK.ItunesSearchResponse;
import com.example.MEEK.TrackItem;
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
        restClient = builder.baseUrl("https://itunes.apple.com").build();
        this.objectMapper = objectMapper;
    }

    public ItunesSearchResponse searchQuery(String query){
        try {
            String jsonResponse = restClient.get().uri(uriBuilder ->
                            uriBuilder.path("/search")
                                    .queryParam("term",query.trim())
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
    public TrackItem searchTrack(Long trackId){
        if (trackId == null){
            System.err.println("TrackID is null");
            return null;
        }
        try {
           String JsonResponse = restClient.get().uri(uriBuilder ->
                    uriBuilder.path("/lookup")
                            .queryParam("id",trackId)
                            .queryParam("entity","song")
                            .queryParam("limit",1)
                            .build())
                    .retrieve()
                    .body(String.class);

           ItunesSearchResponse response = objectMapper.readValue(JsonResponse, ItunesSearchResponse.class);
            if (response != null &&  response.trackItemList() != null && !response.trackItemList().isEmpty()){
                return response.trackItemList().get(0);
            }
        } catch (Exception e){
            System.err.println("Error looking up iTunes track " + trackId + ": " + e.getMessage());
            return null;
        }
        return null;
    }
}
