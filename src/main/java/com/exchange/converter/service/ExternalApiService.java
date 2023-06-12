package com.exchange.converter.service;

import com.exchange.converter.dto.ConversionRateResponseDto;
import com.exchange.converter.dto.CurrencyApiDto;
import com.exchange.converter.dto.CurrencyConversionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ExternalApiService {

    private final RestTemplate restTemplate;
    private final String currencyBasePathUrl;
    private final String currencyApiKey;
    private final HttpHeaders headers;

    @Autowired
    public ExternalApiService(RestTemplateBuilder builder, @Value("${currency.converter.api.url}") String currencyBasePathUrl, @Value("${currency.converter.api.key}") String currencyApiKey) {
        this.restTemplate = builder.build();
        this.currencyBasePathUrl = currencyBasePathUrl;
        this.currencyApiKey = currencyApiKey;
        this.headers = new HttpHeaders();
        headers.add("apikey", currencyApiKey);
    }

    public CurrencyApiDto getAllowedCurrency() throws URISyntaxException {
        String currencyListUrl = currencyBasePathUrl + "list";

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, new URI(currencyListUrl));

        ResponseEntity<CurrencyApiDto> responseEntity = restTemplate.exchange(requestEntity, CurrencyApiDto.class);

        return responseEntity.getBody();
    }


    public ConversionRateResponseDto getExchangeRate(CurrencyConversionRequestDto currencyConversionRequestDTO) {
        String url = UriComponentsBuilder.fromUriString(currencyBasePathUrl).path("/live").queryParam("source", currencyConversionRequestDTO.getBaseCurrency()).queryParam("currencies", currencyConversionRequestDTO.getTargetCurrency()).build().toUriString();

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        ResponseEntity<ConversionRateResponseDto> response = restTemplate.exchange(requestEntity, ConversionRateResponseDto.class);
        return response.getBody();
    }
}
