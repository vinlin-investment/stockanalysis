package com.vinlin.investment.stockanalysis.domain

class StockAnalysisConstant {
     enum SuggestionType {
        FIFTY_TWO_WEEK_LOW("52WeekLow")

        String val

        SuggestionType(String val) {
            this.val = val
        }

    }

    static final String COMMON_STOCK = "CS"
}
