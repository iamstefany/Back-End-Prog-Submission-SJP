package stefany.piccaro.submission.dto;

import java.math.BigDecimal;

public record PropertyCityStatsDTO(
        String city,
        Long count,
        Double avgPrice,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {}
