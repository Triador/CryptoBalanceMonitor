package service;

import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.List;

public class BinanceBalance implements Balance {

    @Override
    public BigDecimal getBalanec() {

        JsonObject coinmarketcapJson = getCoinMarketCapJsonObject();
        List<AssetBalance> binanceBalance = binanceService.getAllAssets();

        return getExchangeTotalBalance(binanceBalance, coinmarketcapJson).setScale(1, 1);
    }
}
