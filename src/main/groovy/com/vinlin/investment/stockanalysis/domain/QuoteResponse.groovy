package com.vinlin.investment.stockanalysis.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
class QuoteResponse {
    @JsonProperty
    String symbol
    @JsonProperty
    String companyName
    @JsonProperty
    String primaryExchange
    @JsonProperty
    String sector
    @JsonProperty
    String calculationPrice
    @JsonProperty
    String open
    @JsonProperty
    String openTime
    @JsonProperty
    String close
    @JsonProperty
    String closeTime
    @JsonProperty
    String high
    @JsonProperty
    String low
    @JsonProperty
    String latestPrice
    @JsonProperty
    String latestSource
    @JsonProperty
    String latestTime
    @JsonProperty
    String latestUpdate
    @JsonProperty
    String latestVolume
    @JsonProperty
    String iexRealtimePrice
    @JsonProperty
    String iexRealtimeSize
    @JsonProperty
    String iexLastUpdated
    @JsonProperty
    String delayedPrice
    @JsonProperty
    String delayedPriceTime
    @JsonProperty
    String extendedPrice
    @JsonProperty
    String extendedPriceTime
    @JsonProperty
    String previousClose
    @JsonProperty
    String change
    @JsonProperty
    String changePercent
    @JsonProperty
    String iexMarketPercent
    @JsonProperty
    String iexVolume
    @JsonProperty
    String avgTotalVolume
    @JsonProperty
    String iexBidPrice
    @JsonProperty
    String iexBidSize
    @JsonProperty
    String iexAskPrice
    @JsonProperty
    String iexAskSize
    @JsonProperty
    String marketCap
    @JsonProperty
    String peRatio
    @JsonProperty
    String week52High
    @JsonProperty
    String week52Low
    @JsonProperty
    String ytdChange

}
