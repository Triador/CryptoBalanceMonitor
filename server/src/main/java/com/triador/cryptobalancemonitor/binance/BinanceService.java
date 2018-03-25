package com.triador.cryptobalancemonitor.binance;

import com.binance.api.client.domain.account.AssetBalance;

import java.util.List;

public interface BinanceService {
    List<AssetBalance> getAllAssets();
}
