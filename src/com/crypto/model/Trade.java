package com.crypto.model;

import java.util.Locale;

/** Immutable purchase trade used by portfolio valuation. */
public class Trade {
    private final String symbol;
    private final double quantity;
    private final double price;

    public Trade(String symbol, double quantity, double price) {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Trade symbol is required.");
        }
        if (!Double.isFinite(quantity) || quantity <= 0) {
            throw new IllegalArgumentException("Trade quantity must be a positive finite number.");
        }
        if (!Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException("Trade price must be a positive finite number.");
        }
        this.symbol = symbol.trim().toUpperCase(Locale.ROOT);
        this.quantity = quantity;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public double getQuantity() { return quantity; }
    public double getPrice() { return price; }
}
