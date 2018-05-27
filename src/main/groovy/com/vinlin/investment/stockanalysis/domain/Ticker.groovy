package com.vinlin.investment.stockanalysis.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
class Ticker {
    @JsonProperty
    String symbol
    @JsonProperty
    String name
    @JsonProperty
    String date
    @JsonProperty
    String isEnabled
    @JsonProperty
    String type
    @JsonProperty
    String iexId
}
