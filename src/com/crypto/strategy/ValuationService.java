package com.crypto.strategy;

import java.util.List;
import com.crypto.model.Trade;

public interface ValuationService {
    double computePnL(List<Trade> trades, double currentPrice);
}