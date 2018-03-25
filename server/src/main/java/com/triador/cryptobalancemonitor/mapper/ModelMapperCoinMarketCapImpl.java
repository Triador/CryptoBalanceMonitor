package com.triador.cryptobalancemonitor.mapper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.triador.cryptobalancemonitor.model.CoinMarketCapTicker;

public class ModelMapperCoinMarketCapImpl implements ModelCoinMarketCapMapper {

    public CoinMarketCapTicker mapToCoinMarketCapTicker(JsonObject jsonObject) {

        CoinMarketCapTicker coinMarketCapTicker = new CoinMarketCapTicker();
        JsonElement jsonElement = jsonObject.get("id");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setId(jsonElement.getAsString());
        jsonElement = jsonObject.get("name");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setName(jsonElement.getAsString());
        jsonElement = jsonObject.get("symbol");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setSymbol(jsonElement.getAsString());
        jsonElement = jsonObject.get("rank");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setRank(jsonElement.getAsString());
        jsonElement = jsonObject.get("price_usd");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setPriceUSD(jsonElement.getAsString());
        jsonElement = jsonObject.get("price_btc");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setPriceBTC(jsonElement.getAsString());
        jsonElement = jsonObject.get("24h_volume_usd");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setVolumeUSD24h(jsonElement.getAsString());
        jsonElement = jsonObject.get("market_cap_usd");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setMarketCapUSD(jsonElement.getAsString());
        jsonElement = jsonObject.get("available_supply");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setAvailableSupply(jsonElement.getAsString());
        jsonElement = jsonObject.get("max_supply");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setMaxSupply(jsonElement.getAsString());
        jsonElement = jsonObject.get("percent_change_1h");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setPercentChange1h(jsonElement.getAsString());
        jsonElement = jsonObject.get("percent_change_24h");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setPercentChange24h(jsonElement.getAsString());
        jsonElement = jsonObject.get("percent_change_7d");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setPercentChange7d(jsonElement.getAsString());
        jsonElement = jsonObject.get("last_updated");
        if (!jsonElement.isJsonNull()) coinMarketCapTicker.setLastUpdated(jsonElement.getAsString());

        return coinMarketCapTicker;
    }
}
