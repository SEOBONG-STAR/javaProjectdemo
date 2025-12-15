package com.crypto.observer;

public interface PriceAlertObserver {
    void onPriceChange(String symbol, double price);
}