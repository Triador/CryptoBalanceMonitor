package controller;

import binance.BinanceService;
import binance.BinanceServiceImpl;
import bitfinex.Bitfinex;
import bittrex.Bittrex;
import coinmarketcap.CoinMarketCapService;
import coinmarketcap.CoinMarketCapServiceImpl;
import etherscan.EtherScannerService;
import etherscan.EtherScannerServiceImpl;
import hibtc.HitBTC;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
}
