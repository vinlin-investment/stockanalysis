package com.vinlin.investment.stockanalysis.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
class SuggestionResponse {
    @JsonProperty
    Map<String, QuoteResponse> suggestions
}
