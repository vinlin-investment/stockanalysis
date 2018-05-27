package com.vinlin.investment.stockanalysis.exception

class StockAnalysisServiceException extends RuntimeException {
    public String trackingId
    public StockAnalysisServiceException(String msg) { super(msg)}
    public StockAnalysisServiceException(String msg, String trackingId) {
        super(msg)
        this.trackingId = trackingId
    }
    public StockAnalysisServiceException(String msg, Exception ex) { super(msg, ex) }
    public StockAnalysisServiceException(Exception ex) { super(ex) }
}
