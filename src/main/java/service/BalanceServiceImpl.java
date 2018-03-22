package service;

import bitfinex.Bitfinex;
import bittrex.Bittrex;
import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hibtc.HitBTC;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BalanceServiceImpl implements BalanceService {
    private static final BinanceService binanceService = new BinanceServiceImpl();
    private static final Bittrex bittrex = new Bittrex();
    private static final Bitfinex bitfinex = new Bitfinex(HttpClients.createDefault());
    private static final HitBTC hitBTC = new HitBTC(HttpClients.createDefault());
    private static final EtherScannerService etherScannerService = new EtherScannerServiceImpl();
    private static final CoinMarketCapService coinMarketCapService = new CoinMarketCapServiceImpl(HttpClients.createDefault());

    @Override
    public BigDecimal getTotalBalance() {

        JsonObject coinmarketcapJson = getCoinMarketCapJsonObject();

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
            totalBalance = totalBalance.add(getExchangeTotalBalance(assetBalances, coinmarketcapJson));
        }
        BigDecimal usdPriceForETH = new BigDecimal(coinMarketCapService.getCoinMarketCapTicker("ethereum").getPriceUSD());
        totalBalance = totalBalance.add(myEtherWalletBalance.multiply(usdPriceForETH));

        return totalBalance.setScale(1, 1);
    }

    @Override
    public BigDecimal getBitfinexBalance() {

        JsonObject coinmarketcapJson = getCoinMarketCapJsonObject();
        List<AssetBalance> bitfinexBalance = bitfinex.getAllAssets();

        return getExchangeTotalBalance(bitfinexBalance, coinmarketcapJson).setScale(1, 1);
    }

    @Override
    public BigDecimal getBittrexBalance() {

        JsonObject coinmarketcapJson = getCoinMarketCapJsonObject();
        List<AssetBalance> bittrexBalance = bittrex.getAllAssets();

        return getExchangeTotalBalance(bittrexBalance, coinmarketcapJson).setScale(1, 1);
    }

    public BigDecimal getHitBTCBalance() {

        JsonObject coinmarketcapJson = getCoinMarketCapJsonObject();
        List<AssetBalance> HitBTCBalance = hitBTC.getAllAssets();

        return getExchangeTotalBalance(HitBTCBalance, coinmarketcapJson).setScale(1, 1);
    }

    @Override
    public BigDecimal getMyEtherWalletBalance() {

        BigDecimal myEtherWalletBalance = etherScannerService.getMyEtherWalletBalance();
        BigDecimal usdPriceForETH = new BigDecimal(coinMarketCapService.getCoinMarketCapTicker("ethereum").getPriceUSD());

        return myEtherWalletBalance.multiply(usdPriceForETH).setScale(1, 1);
    }

    private JsonObject getCoinMarketCapJsonObject() {

        InputStream is = BalanceServiceImpl.class.getClassLoader().getResourceAsStream("coinmarketcap.json");
        StringBuilder data = new StringBuilder();
        try {
            while (is.available() > 0) {
                data.append(Character.toChars(is.read()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonParser().parse(data.toString()).getAsJsonObject();
    }

    private BigDecimal getExchangeTotalBalance(List<AssetBalance> assetBalances, JsonObject coinmarketcapJson) {
        BigDecimal totalBalance = new BigDecimal(0);
        for (AssetBalance assetBalance: assetBalances) {
            JsonElement jsonElement = coinmarketcapJson.get(assetBalance.getAsset());
            String id;
            if (jsonElement != null) {
                id = jsonElement.getAsString();
            } else id = assetBalance.getAsset();
            BigDecimal usdPrice = new BigDecimal(coinMarketCapService.getCoinMarketCapTicker(id).getPriceUSD());
            BigDecimal available = new BigDecimal(assetBalance.getFree());
            BigDecimal reserved = new BigDecimal(assetBalance.getLocked());
            totalBalance = totalBalance.add(available.add(reserved).multiply(usdPrice));
        }

        return totalBalance;
    }
}
