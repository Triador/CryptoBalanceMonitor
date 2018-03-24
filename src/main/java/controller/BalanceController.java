package controller;

import Utils.BalanceUtils;
import binance.BinanceService;
import binance.BinanceServiceImpl;
import bitfinex.Bitfinex;
import bittrex.Bittrex;
import coinmarketcap.CoinMarketCapService;
import coinmarketcap.CoinMarketCapServiceImpl;
import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonObject;
import etherscan.EtherScannerService;
import etherscan.EtherScannerServiceImpl;
import hibtc.HitBTC;
import model.Balance;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public List<Balance> getBalances() {

        JsonObject coinmarketcapJson = BalanceUtils.getCoinMarketCapJsonObject();

        List<AssetBalance> binanceBalances = binanceService.getAllAssets();
        List<AssetBalance> bitfinexBalances = bitfinex.getAllAssets();
        List<AssetBalance> bittrexBalances = bittrex.getAllAssets();
        List<AssetBalance> hitBTCBalances = hitBTC.getAllAssets();

        BigDecimal binanceBalance = BalanceUtils.getExchangeTotalBalance(binanceBalances, coinmarketcapJson);
        BigDecimal bitfinexBalance = BalanceUtils.getExchangeTotalBalance(bitfinexBalances, coinmarketcapJson);
        BigDecimal bittrexBalance = BalanceUtils.getExchangeTotalBalance(bittrexBalances, coinmarketcapJson);
        BigDecimal hitBTCBalance = BalanceUtils.getExchangeTotalBalance(hitBTCBalances, coinmarketcapJson);
        BigDecimal myEtherWalletBalance = etherScannerService.getMyEtherWalletBalance();

        List<Balance> balances = new ArrayList<>();
        balances.add(new Balance("binance", binanceBalance));
        balances.add(new Balance("bitfinex", bitfinexBalance));
        balances.add(new Balance("bittrex", bittrexBalance));
        balances.add(new Balance("hitbtc", hitBTCBalance));
        balances.add(new Balance("myetherwallet", myEtherWalletBalance));

        return balances;
    }
}
