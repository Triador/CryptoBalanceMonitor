package com.triador.Utils;

import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.triador.coinmarketcap.CoinMarketCapServiceImpl;
import org.apache.http.impl.client.HttpClients;
import com.triador.coinmarketcap.CoinMarketCapService;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

public class BalanceUtils {

    private static final CoinMarketCapService coinMarketCapService = new CoinMarketCapServiceImpl(HttpClients.createDefault());

    public static BigDecimal getExchangeTotalBalance(List<AssetBalance> assetBalances, JsonObject coinmarketcapJson) {

        BigDecimal totalBalance = new BigDecimal(0);
        for (AssetBalance assetBalance: assetBalances) {
            JsonElement jsonElement = coinmarketcapJson.get(assetBalance.getAsset());
            String id;
            if (jsonElement != null) {
                id = jsonElement.getAsString();
            } else id = assetBalance.getAsset();

            BigDecimal usdPrice;
            BigDecimal available = new BigDecimal(assetBalance.getFree());
            BigDecimal reserved = new BigDecimal(assetBalance.getLocked());
            if (id.equals("USD")) {
                totalBalance = totalBalance.add(available.add(reserved));
                break;
            }
            usdPrice = new BigDecimal(coinMarketCapService.getCoinMarketCapTicker(id).getPriceUSD());
            totalBalance = totalBalance.add(available.add(reserved).multiply(usdPrice));
        }

        return totalBalance;
    }

    public static JsonObject getCoinMarketCapJsonObject() {

        InputStream is = BalanceUtils.class.getClassLoader().getResourceAsStream("coinmarketcap.json");
        StringBuilder data = new StringBuilder();
        try {
            while (is.available() > 0) {
                data.append(Character.toChars(is.read()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonParser().parse(data.toString()).getAsJsonObject();
    }
}
