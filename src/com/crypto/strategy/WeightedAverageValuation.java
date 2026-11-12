package com.crypto.strategy;

import java.util.List;
import com.crypto.model.Trade;

public class WeightedAverageValuation implements ValuationService {
    @Override
    public double computePnL(List<Trade> trades, double currentPrice) {
        if (trades == null || trades.isEmpty()) {
            throw new IllegalArgumentException("At least one trade is required for valuation.");
        }
        if (!Double.isFinite(currentPrice) || currentPrice < 0) {
            throw new IllegalArgumentException("Current price must be a non-negative finite number.");
        }

        double totalQuantity = 0;
        double totalCost = 0;
        for (Trade trade : trades) {
            totalQuantity += trade.getQuantity();
            totalCost += trade.getPrice() * trade.getQuantity();
        }
        if (totalQuantity <= 0 || !Double.isFinite(totalCost)) {
            throw new IllegalStateException("Portfolio totals are invalid.");
        }

        double averageCost = totalCost / totalQuantity;
        return totalQuantity * (currentPrice - averageCost);
    }
}
