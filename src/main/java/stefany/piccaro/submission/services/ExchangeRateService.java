package stefany.piccaro.submission.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import stefany.piccaro.submission.dto.ExchangeRateResponseDTO;
import stefany.piccaro.submission.exceptions.NotFoundException;
import stefany.piccaro.submission.exceptions.ValidationException;

import java.math.BigDecimal;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    public ExchangeRateService(
            RestTemplate restTemplate,
            @Value("${exchange.api.key}") String apiKey,
            @Value("${exchange.api.base-url}") String baseUrl
    ) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public BigDecimal getRateFromEur(String targetCurrency) {

        String url = String.format(
                "%s/%s/latest/EUR",
                baseUrl,
                apiKey
        );

        ResponseEntity<ExchangeRateResponseDTO> response =
                restTemplate.getForEntity(url, ExchangeRateResponseDTO.class);

        ExchangeRateResponseDTO body = response.getBody();

        if (body == null || !"success".equals(body.result())) {
            throw new NotFoundException("Failed to retrieve exchange rates");
        }

        BigDecimal rate = body.conversion_rates()
                .get(targetCurrency.toUpperCase());

        if (rate == null) {
            throw new ValidationException("Unsupported currency: " + targetCurrency);
        }

        return rate;
    }
}