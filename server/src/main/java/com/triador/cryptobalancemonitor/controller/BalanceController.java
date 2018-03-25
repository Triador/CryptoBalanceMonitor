package com.triador.cryptobalancemonitor.controller;

import com.triador.cryptobalancemonitor.binance.BinanceService;
import com.triador.cryptobalancemonitor.binance.BinanceServiceImpl;
import com.triador.cryptobalancemonitor.bitfinex.Bitfinex;
import com.triador.cryptobalancemonitor.bittrex.Bittrex;
import com.triador.cryptobalancemonitor.coinmarketcap.CoinMarketCapService;
import com.triador.cryptobalancemonitor.coinmarketcap.CoinMarketCapServiceImpl;
import com.triador.cryptobalancemonitor.etherscan.EtherScannerService;
import com.triador.cryptobalancemonitor.etherscan.EtherScannerServiceImpl;
import com.triador.cryptobalancemonitor.hibtc.HitBTC;
import com.triador.cryptobalancemonitor.model.Balance;
import com.triador.cryptobalancemonitor.service.*;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping({"/api"})
public class BalanceController {

    private static final BinanceService binanceService = new BinanceServiceImpl();
    private static final Bittrex bittrex = new Bittrex();
    private static final Bitfinex bitfinex = new Bitfinex(HttpClients.createDefault());
    private static final HitBTC hitBTC = new HitBTC(HttpClients.createDefault());
    private static final EtherScannerService etherScannerService = new EtherScannerServiceImpl();
    private static final CoinMarketCapService coinMarketCapService = new CoinMarketCapServiceImpl(HttpClients.createDefault());

    @GetMapping
    public List<Balance> getBalances() {

        BinanceBalance binanceBalance = new BinanceBalance(binanceService);
        BitfinexBalance bitfinexBalance = new BitfinexBalance(bitfinex);
        BittrexBalance bittrexBalance = new BittrexBalance(bittrex);
        HitBTCBalance hitBTCBalance = new HitBTCBalance(hitBTC);
        MyEtherWalletBalance myEtherWalletBalance = new MyEtherWalletBalance(etherScannerService, coinMarketCapService);

        BigDecimal usdBinanceBalance = binanceBalance.getBalance();
        BigDecimal usdBitfinexBalance = bitfinexBalance.getBalance();
        BigDecimal usdBittrexBalance = bittrexBalance.getBalance();
        BigDecimal usdHitBTCBalance = hitBTCBalance.getBalance();
        BigDecimal usdMyEtherWalletBalance = myEtherWalletBalance.getBalance();
        BigDecimal usdTotalBalance = usdBinanceBalance
                .add(usdBitfinexBalance)
                .add(usdBittrexBalance)
                .add(usdHitBTCBalance)
                .add(usdMyEtherWalletBalance);

        List<Balance> balances = new ArrayList<>();
        balances.add(new Balance("binance", usdBinanceBalance));
        balances.add(new Balance("bitfinex", usdBitfinexBalance));
        balances.add(new Balance("bittrex", usdBittrexBalance));
        balances.add(new Balance("hitbtc", usdHitBTCBalance));
        balances.add(new Balance("myetherwallet", usdMyEtherWalletBalance));
        balances.add(new Balance("total", usdTotalBalance));

        return balances;
    }
}
