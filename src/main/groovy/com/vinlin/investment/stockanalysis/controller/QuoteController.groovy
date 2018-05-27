package com.vinlin.investment.stockanalysis.controller

import com.vinlin.investment.stockanalysis.client.IEXTradingClient
import com.vinlin.investment.stockanalysis.domain.QuoteRequest
import com.vinlin.investment.stockanalysis.domain.QuoteResponse
import com.vinlin.investment.stockanalysis.domain.StockAnalysisConstant.SuggestionType
import com.vinlin.investment.stockanalysis.domain.SuggestionRequest
import com.vinlin.investment.stockanalysis.domain.Ticker
import com.vinlin.investment.stockanalysis.services.StockAnalysisService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class QuoteController {

    public static Logger LOGGER = LoggerFactory.getLogger(QuoteController.class.getName())

    @Autowired
    StockAnalysisService stockAnalysisService

    @Autowired
    IEXTradingClient iexTradingClient

    List<QuoteResponse> quoteResponses = new ArrayList<>()

    @GetMapping(path = "/quote", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<QuoteResponse> getQuote(QuoteRequest quoteRequest) {
        long startTime = new Date().getTime()
        int status = 0
        LOGGER.info("Got Quote Request Query: $quoteRequest.symbol")

        Mono<QuoteResponse> quoteResponseMono = stockAnalysisService.getQuote(quoteRequest).doOnSuccess({ quoteResponse ->
            status = HttpStatus.OK.value()
        }).doOnError({ throwable ->
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        })
        return quoteResponseMono
    }

    @GetMapping(path = "/suggestion", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<QuoteResponse>> getSuggestion(SuggestionRequest suggestionRequest) {
        long startTime = new Date().getTime()
        int status = 0
        LOGGER.info("Got Suggestion Request: $suggestionRequest.type")

        Mono<List<QuoteResponse>> monoSuggestionRes = stockAnalysisService.getSuggestion(suggestionRequest).doOnSuccess({ quoteResponse ->
            status = HttpStatus.OK.value()
            handleResponse(suggestionRequest, quoteResponse)

        }).doOnError({ throwable ->
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        }).then(Mono.just(quoteResponses))

        return monoSuggestionRes

    }

    public List<QuoteResponse> handleResponse(SuggestionRequest reg , Map<String, Map<String, QuoteResponse>> res) {
        quoteResponses.clear()
        for (Ticker ticker : iexTradingClient.tickers) {
            if (res.get(ticker.symbol) == null) continue
            if (reg.sector != null && !reg.sector.equalsIgnoreCase(res.get(ticker.symbol).get("quote").sector)) continue
            if (reg.type.equalsIgnoreCase(SuggestionType.FIFTY_TWO_WEEK_LOW.val)) {
                handleFiftyTwoWeekLow(reg, res, ticker)
            }
        }
        return quoteResponses
    }

    public List<QuoteResponse> handleFiftyTwoWeekLow(SuggestionRequest reg , Map<String, Map<String, QuoteResponse>> res, Ticker ticker) {
        Double latestPrice = res.get(ticker.symbol).get("quote").latestPrice
        Double week52High = res.get(ticker.symbol).get("quote").week52High
        Double week52low = res.get(ticker.symbol).get("quote").week52Low
        Double ratioTodayPriceTo52WeekLow = week52low / latestPrice
        Double ratio52WeekLowToHigh = week52low / week52High
        if (ratioTodayPriceTo52WeekLow >= reg.ratio.toDouble() && ratio52WeekLowToHigh <= 0.5) {
            LOGGER.info("Symbol: " + res.get(ticker.symbol).get("quote").symbol + " | Ratio: $ratioTodayPriceTo52WeekLow")
            quoteResponses.add(res.get(ticker.symbol).get("quote"))
        }
        return quoteResponses
    }

}
