package service;

import Utils.BalanceUtils;
import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonObject;
import hibtc.HitBTC;

import java.math.BigDecimal;
import java.util.List;

public class HitBTCBalance implements Balance {

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
