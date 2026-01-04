package stefany.piccaro.submission.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRateResponseDTO(
        String result,
        String base_code,
        Map<String, BigDecimal> conversion_rates
) {}