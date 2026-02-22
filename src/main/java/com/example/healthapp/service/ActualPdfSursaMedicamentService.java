package com.example.healthapp.service;

import java.net.URI;
import java.util.Optional;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;

@Service
public class ActualPdfSursaMedicamentService {

    private final RestTemplate restTemplate;

    public ActualPdfSursaMedicamentService(@Qualifier("pdfRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public byte[] descarcaPdf(String url) {
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
            .accept(MediaType.APPLICATION_PDF)
            .build();

        ResponseEntity<byte[]> response = restTemplate.exchange(request, byte[].class);
        return Optional.ofNullable(response.getBody()).orElse(new byte[0]);
    }
}
