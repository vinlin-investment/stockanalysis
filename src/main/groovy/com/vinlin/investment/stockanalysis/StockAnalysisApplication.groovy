package com.vinlin.investment.stockanalysis

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class StockAnalysisApplication {

	public static final String APPLICATION_NAME = "StockAnalysisApplication"
	static void main(String[] args) {
		SpringApplication.run StockAnalysisApplication, args
	}
}
