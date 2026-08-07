package com.example.MEEK.services;

import com.example.MEEK.SpotifySearchResponse;
import com.example.MEEK.SpotifyTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Service
public class SpotifyService {
    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private RestClient restClient = RestClient.create();

    public String getAccessCode(){
        String authHeader = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        SpotifyTokenResponse response = restClient.post().uri(
                "https://accounts.spotify.com/api/token"
        ).header("Authorization", "Basic "+authHeader)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("grant_type=client_credentials")
                .retrieve()
                .body(SpotifyTokenResponse.class);

        return response != null ? response.accessToken() : null;
    }
    public SpotifySearchResponse searchQuery(String query){
        String token = getAccessCode();
        return restClient.get().uri(
                "http://api.spotify.com/v1/search/?q={query}&type=track&limit=10",query
        ).header("Authorization","Bearer "+token)
                .retrieve()
                .body(SpotifySearchResponse.class);
    }
}
