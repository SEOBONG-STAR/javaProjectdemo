package com.crypto.strategy;

import java.util.List;
import com.crypto.model.Trade;

public class WeightedAverageValuation implements ValuationService {

        public double computePnL(List<Trade> trades, double currentPrice) {
        double totalQty = 0;
        double totalCost = 0;

        for (Trade t : trades) {
            totalQty += t.getQuantity();
            totalCost += t.getPrice() * t.getQuantity();
        }

        double avgBuy = totalCost / totalQty;
        return totalQty * (currentPrice - avgBuy);
    }
}