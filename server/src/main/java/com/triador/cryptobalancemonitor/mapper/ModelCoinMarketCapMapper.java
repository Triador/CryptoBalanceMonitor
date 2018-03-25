package com.triador.cryptobalancemonitor.mapper;

import com.google.gson.JsonObject;
import com.triador.cryptobalancemonitor.model.CoinMarketCapTicker;

public interface ModelCoinMarketCapMapper {
    CoinMarketCapTicker mapToCoinMarketCapTicker(JsonObject jsonObject);
}
