package com.triador.service;

import com.triador.Utils.BalanceUtils;
import com.triador.bittrex.Bittrex;
import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.List;

public class BittrexBalance implements BalanceService {

    private final Bittrex bittrex;

    public BittrexBalance(Bittrex bittrex) {
        this.bittrex = bittrex;
    }

    @Override
    public BigDecimal getBalance() {

        JsonObject coinmarketcapJson = BalanceUtils.getCoinMarketCapJsonObject();
        List<AssetBalance> bittrexBalance = bittrex.getAllAssets();

        return BalanceUtils.getExchangeTotalBalance(bittrexBalance, coinmarketcapJson).setScale(1, 1);
    }
}
