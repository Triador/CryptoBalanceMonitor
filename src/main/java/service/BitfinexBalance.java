package service;

import Utils.BalanceUtils;
import bitfinex.Bitfinex;
import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.List;

public class BitfinexBalance implements BalanceService {

    private final Bitfinex bitfinex;

    public BitfinexBalance(Bitfinex bitfinex) {
        this.bitfinex = bitfinex;
    }

    @Override
    public BigDecimal getBalance() {

        JsonObject coinmarketcapJson = BalanceUtils.getCoinMarketCapJsonObject();
        List<AssetBalance> bitfinexBalance = bitfinex.getAllAssets();

        return BalanceUtils.getExchangeTotalBalance(bitfinexBalance, coinmarketcapJson).setScale(1, 1);
    }
}
