package com.triador.cryptobalancemonitor.model;

import java.math.BigDecimal;

public class Balance {

    private String exchangeName;
    private BigDecimal amount;

    public Balance(String exchangeName, BigDecimal amount) {
        this.exchangeName = exchangeName;
        this.amount = amount;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
