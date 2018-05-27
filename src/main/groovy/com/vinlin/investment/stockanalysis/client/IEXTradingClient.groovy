package com.vinlin.investment.stockanalysis.client

import com.vinlin.investment.stockanalysis.domain.*
import com.vinlin.investment.stockanalysis.exception.StockAnalysisServiceException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

import javax.annotation.PostConstruct

@Component
class IEXTradingClient {

    @Value('${iextrading.api.quote.url}')
    String iexTradingQuoteUrl

    @Value('${iextrading.api.symbols.url}')
    String iexTradingSymbolUrl

    @Value('${iextrading.api.suggestion.url}')
    String iexTradingSuggestionlUrl

    WebClient webClient

    List<Ticker> tickers

    public static Logger LOGGER = LoggerFactory.getLogger(IEXTradingClient.class.getName())

    @PostConstruct
    void init() {
        webClient = WebClient.create()
        this.tickers = new ArrayList<Ticker>()
        getSymbols().subscribe( { res -> res as Ticker[]
            this.tickers = new ArrayList<>(Arrays.asList(res))
        })
    }

    Mono<QuoteResponse> getQuote(QuoteRequest quoteRequest) {
        def uriVariableMap = [symbol: quoteRequest.symbol]

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(iexTradingQuoteUrl)
                .buildAndExpand(uriVariableMap).encode()
        String getUrl = uriComponents.toUriString()

        LOGGER.info("IEXTrading Quote Request URL: $getUrl")
        long startTime = new Date().getTime()
        Mono<QuoteResponse> monoQuoteRes = webClient.get().uri(getUrl).accept(MediaType.APPLICATION_JSON).exchange().flatMap({ response ->
            def statusCode = response.statusCode().value()
            if (response.statusCode().is2xxSuccessful()) {
                return response.bodyToMono(QuoteResponse.class)
            } else if (response.statusCode().isError()) {
                String trackingID = UUID.randomUUID().toString()
                LOGGER.warn("Got Exception while calling IEXTrading Quote: ${quoteRequest.symbol} Response code: ${response.statusCode()} url: $getUrl")
                def monoError = Mono.error(new StockAnalysisServiceException("Unknown Symbol", trackingID))
                return monoError
            }
        })

        return monoQuoteRes
    }

    Mono<Map<String, Map<String, QuoteResponse>>> getSuggestion(SuggestionRequest suggestionRequest) {

        List<Ticker> filteredTicker = tickers.findAll { ticker ->
            ticker.type.equalsIgnoreCase(StockAnalysisConstant.COMMON_STOCK)
        }

        LOGGER.info("Total Common Stock: ${filteredTicker.size()}")

        Integer rangeAt = suggestionRequest.rangeAt.toInteger()
        Integer rangeEnd = rangeAt + 1500 < filteredTicker.size() ? rangeAt + 1500 : filteredTicker.size()-1

        def uriVariableMap = [symbols: filteredTicker[rangeAt..rangeEnd].symbol.join(",")]
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(iexTradingSuggestionlUrl)
                .buildAndExpand(uriVariableMap).encode()
        String getUrl = uriComponents.toUriString()

        LOGGER.info("IEXTrading Suggestion/Batch Request URL: $getUrl")
        long startTime = new Date().getTime()
        Mono<Map<String, Map<String, QuoteResponse>>> monoSuggestionRes = webClient.get().uri(getUrl).accept(MediaType.APPLICATION_JSON).exchange().flatMap({ response ->
            def statusCode = response.statusCode().value()
            if (response.statusCode().is2xxSuccessful()) {
                return response.bodyToMono(Map.class)
            } else if (response.statusCode().isError()) {
                String trackingID = UUID.randomUUID().toString()
                LOGGER.warn("Got Exception while calling IEXTrading Quote - Response code: ${response.statusCode()} - url: $getUrl")
                def monoError = Mono.error(new StockAnalysisServiceException("Unknown Error", trackingID))
                return monoError
            }
        })

        return monoSuggestionRes
    }

    Mono<Ticker[]> getSymbols() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(iexTradingSymbolUrl)
                .buildAndExpand().encode()
        String getUrl = uriComponents.toUriString()

        LOGGER.info("IEXTrading Symbol Request URL: $getUrl")
        long startTime = new Date().getTime()
        Mono<Ticker[]> monoSymbolRes = webClient.get().uri(getUrl).accept(MediaType.APPLICATION_JSON).exchange().flatMap({ response ->
            def statusCode = response.statusCode().value()
            if (response.statusCode().is2xxSuccessful()) {
                return response.bodyToMono(Ticker[].class)
            } else if (response.statusCode().isError()) {
                String trackingID = UUID.randomUUID().toString()
                LOGGER.warn("Got Exception while calling IEXTrading Symbols - Response code: ${response.statusCode()} - url: $getUrl")
                def monoError = Mono.error(new StockAnalysisServiceException("Unknown Error", trackingID))
                return monoError
            }
        })
        return monoSymbolRes
    }


}

