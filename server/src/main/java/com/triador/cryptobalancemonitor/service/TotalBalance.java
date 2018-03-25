package com.triador.cryptobalancemonitor.service;

import com.triador.cryptobalancemonitor.Utils.BalanceUtils;
import com.triador.cryptobalancemonitor.binance.BinanceService;
import com.triador.cryptobalancemonitor.binance.BinanceServiceImpl;
import com.triador.cryptobalancemonitor.bitfinex.Bitfinex;
import com.triador.cryptobalancemonitor.bittrex.Bittrex;
import com.triador.cryptobalancemonitor.coinmarketcap.CoinMarketCapService;
import com.triador.cryptobalancemonitor.coinmarketcap.CoinMarketCapServiceImpl;
import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonObject;
import com.triador.cryptobalancemonitor.etherscan.EtherScannerService;
import com.triador.cryptobalancemonitor.etherscan.EtherScannerServiceImpl;
import com.triador.cryptobalancemonitor.hibtc.HitBTC;
import org.apache.http.impl.client.HttpClients;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class TotalBalance implements BalanceService {

    private static final BinanceService binanceService = new BinanceServiceImpl();
    private static final Bittrex bittrex = new Bittrex();
    private static final Bitfinex bitfinex = new Bitfinex(HttpClients.createDefault());
    private static final HitBTC hitBTC = new HitBTC(HttpClients.createDefault());
    private static final EtherScannerService etherScannerService = new EtherScannerServiceImpl();
    private static final CoinMarketCapService coinMarketCapService = new CoinMarketCapServiceImpl(HttpClients.createDefault());

    @Override
    public BigDecimal getBalance() {

        JsonObject coinmarketcapJson = BalanceUtils.getCoinMarketCapJsonObject();

        List<AssetBalance> binanceBalances = binanceService.getAllAssets();
        List<AssetBalance> bitfinexBalances = bitfinex.getAllAssets();
        List<AssetBalance> bittrexBalances = bittrex.getAllAssets();
        List<AssetBalance> hitBTCBalances = hitBTC.getAllAssets();
        BigDecimal myEtherWalletBalance = etherScannerService.getMyEtherWalletBalance();

        List<List<AssetBalance>> allBalances = new ArrayList<>();
        allBalances.add(binanceBalances);
        allBalances.add(bitfinexBalances);
        allBalances.add(bittrexBalances);
        allBalances.add(hitBTCBalances);

        BigDecimal totalBalance = new BigDecimal(0);
        for (List<AssetBalance> assetBalances: allBalances) {
            totalBalance = totalBalance.add(BalanceUtils.getExchangeTotalBalance(assetBalances, coinmarketcapJson));
        }
        BigDecimal usdPriceForETH = new BigDecimal(coinMarketCapService.getCoinMarketCapTicker("ethereum").getPriceUSD());
        totalBalance = totalBalance.add(myEtherWalletBalance.multiply(usdPriceForETH));

        return totalBalance.setScale(1, 1);
    }
}
