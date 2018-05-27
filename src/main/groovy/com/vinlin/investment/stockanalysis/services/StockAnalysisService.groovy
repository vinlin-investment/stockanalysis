package com.vinlin.investment.stockanalysis.services

import com.vinlin.investment.stockanalysis.client.IEXTradingClient
import com.vinlin.investment.stockanalysis.domain.QuoteRequest
import com.vinlin.investment.stockanalysis.domain.QuoteResponse
import com.vinlin.investment.stockanalysis.domain.SuggestionRequest
import com.vinlin.investment.stockanalysis.domain.Ticker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class StockAnalysisService {
    @Autowired
    IEXTradingClient iexTradingClient

    Mono<QuoteResponse> getQuote(QuoteRequest quoteRequest) {
        return iexTradingClient.getQuote(quoteRequest)
    }

    Mono<Map<String, Map<String, QuoteResponse>>> getSuggestion(SuggestionRequest suggestionRequest) {
        return iexTradingClient.getSuggestion(suggestionRequest)
    }

    Mono<Ticker[]> getSymbols() {
        return iexTradingClient.getSymbols()
    }
}
