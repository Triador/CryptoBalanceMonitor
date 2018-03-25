package com.triador.cryptobalancemonitor.binance;

import com.triador.cryptobalancemonitor.Utils.PropertyHandler;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.AssetBalance;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

public class BinanceServiceImpl implements BinanceService {

    private static final BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
            PropertyHandler.getInstance().getValue("binancePubkey"),
            PropertyHandler.getInstance().getValue("binanceSeckey")
    );
    private static final BinanceApiRestClient client = factory.newRestClient();

    public List<AssetBalance> getAllAssets() {

        List<AssetBalance> assetBalances = client.getAccount().getBalances();
        Iterator<AssetBalance> iterator = assetBalances.iterator();

        while (iterator.hasNext()) {

            AssetBalance assetBalance = iterator.next();
            BigDecimal available = new BigDecimal(assetBalance.getFree());
            BigDecimal reserved = new BigDecimal(assetBalance.getLocked());

            if (available.compareTo(BigDecimal.ZERO) == 0 && reserved.compareTo(BigDecimal.ZERO) == 0) {
                iterator.remove();
            }
        }

        return assetBalances;
    }
}
