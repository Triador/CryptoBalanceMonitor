package com.triador.cryptobalancemonitor.coinmarketcap;

import com.triador.cryptobalancemonitor.model.CoinMarketCapTicker;

import java.util.Map;

public interface CoinMarketCapService {
    CoinMarketCapTicker getCoinMarketCapTicker(String coin);
    Map<String, CoinMarketCapTicker> getCoinMarketCapTickers();
}
