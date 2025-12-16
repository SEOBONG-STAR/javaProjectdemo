package com.crypto.service;

import java.util.*;

import com.crypto.exception.SymbolNotFoundException;
import com.crypto.model.Trade;

import com.crypto.exception.InvalidTradeException;
import com.crypto.strategy.ValuationService;


public class PortfolioService {

    private Map<String, List<Trade>> tradeMap = new HashMap<>();
    private ValuationService valuationService;

    public PortfolioService(ValuationService strategy) {
        this.valuationService = strategy;
    }

    public void addTrade(Trade t) throws InvalidTradeException {
        if (t.getQuantity() <= 0 || t.getPrice() <= 0) {
            throw new InvalidTradeException("Trade values cannot be zero or negative.");
        }

        tradeMap.computeIfAbsent(t.getSymbol(), k -> new ArrayList<>()).add(t);
    }

    public double computePnL(String symbol, double currentPrice) throws SymbolNotFoundException {
        if (!tradeMap.containsKey(symbol)) {
            throw new SymbolNotFoundException("Symbol not found: " + symbol);
        }
        return valuationService.computePnL(tradeMap.get(symbol), currentPrice);
    }

    public Map<String, List<Trade>> getTradeMap() {
        return tradeMap;
    }
}