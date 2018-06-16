package com.vinlin.investment.stockanalysis.domain

class SuggestionRequest {
    String type = "52weeklow"
    String ratio = "0.9"
    String sector
    String rangeAt = "0"
    String priceBelow = "1"
}
