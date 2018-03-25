package com.triador.cryptobalancemonitor.service;

import com.triador.cryptobalancemonitor.Utils.BalanceUtils;
import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonObject;
import com.triador.cryptobalancemonitor.hibtc.HitBTC;

import java.math.BigDecimal;
import java.util.List;

public class HitBTCBalance implements BalanceService {

    private final HitBTC hitBTC;

    public HitBTCBalance(HitBTC hitBTC) {
        this.hitBTC = hitBTC;
    }

    @Override
    public BigDecimal getBalance() {

        JsonObject coinmarketcapJson = BalanceUtils.getCoinMarketCapJsonObject();
        List<AssetBalance> HitBTCBalance = hitBTC.getAllAssets();

        return BalanceUtils.getExchangeTotalBalance(HitBTCBalance, coinmarketcapJson).setScale(1, 1);
    }
}
