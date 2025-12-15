package com.crypto.observer;

import java.util.ArrayList;
import java.util.List;

public class PriceAlertService {

    private List<PriceAlertObserver> observers = new ArrayList<>();

    public void subscribe(PriceAlertObserver obs) {
        observers.add(obs);
    }

    public void notifyChange(String symbol, double price) {
        for (PriceAlertObserver obs : observers) {
            obs.onPriceChange(symbol, price);
        }
    }
}