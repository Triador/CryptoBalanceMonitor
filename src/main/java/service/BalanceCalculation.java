package service;

import bitfinex.Bitfinex;
import bittrex.Bittrex;
import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonArray;
import org.apache.http.impl.client.HttpClients;

import java.util.List;

public class BalanceCalculation {
    private static final BinanceService binanceService = new BinanceServiceImpl();
    private static final Bittrex bittrex = new Bittrex();
    private static final Bitfinex bitfinex = new Bitfinex(HttpClients.createDefault());

    public static void main(String[] args) {

        List<AssetBalance> binanceBalances = binanceService.getAllAssets();
        JsonArray bitfinexBalances = bitfinex.post("/v2/auth/r/wallets");
        List<AssetBalance> bittrexBalances = bittrex.getBalances();

        System.out.println("binanceBalances" + " " + binanceBalances);
        System.out.println("bittrexBalances" + " " + bittrexBalances);
        System.out.println("bitfinexBalances" + " " + bitfinexBalances);

    }
}
