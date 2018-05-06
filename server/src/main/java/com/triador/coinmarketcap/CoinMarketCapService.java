package com.triador.coinmarketcap;

import com.triador.model.CoinMarketCapTicker;

import java.util.Map;

public interface CoinMarketCapService {
    CoinMarketCapTicker getCoinMarketCapTicker(String coin);
    Map<String, CoinMarketCapTicker> getCoinMarketCapTickers();
}
