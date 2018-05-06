package com.triador.mapper;

import com.google.gson.JsonObject;
import com.triador.model.CoinMarketCapTicker;

public interface ModelCoinMarketCapMapper {
    CoinMarketCapTicker mapToCoinMarketCapTicker(JsonObject jsonObject);
}
