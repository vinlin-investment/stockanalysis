package com.vinlin.investment.stockanalysis.controller

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
class ManualController {

    public static Logger LOGGER = LoggerFactory.getLogger(ManualController.class.getName())

    @Autowired
    StockAnalysisService stockAnalysisService

    @GetMapping(path = "/symbols", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Ticker[]> getSymbols() {
        long startTime = new Date().getTime()
        int status = 0
        LOGGER.info("Got Symbols Request")

        Mono<Ticker[]> symbolResponseMono = stockAnalysisService.getSymbols().doOnSuccess({ quoteResponse ->
            status = HttpStatus.OK.value()
        }).doOnError({ throwable ->
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        })
        return symbolResponseMono
    }

}
