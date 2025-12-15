package com.crypto.model;

public abstract class Asset {
    protected String symbol;

    public Asset(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}