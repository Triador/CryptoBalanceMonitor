package service;

import Utils.PropertyHandler;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.AssetBalance;

import java.util.List;

public class BinanceServiceImpl implements BinanceService {

    private static final BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
            PropertyHandler.getInstance().getValue("binancePubkey"),
            PropertyHandler.getInstance().getValue("binanceSeckey")
    );
    private static final BinanceApiRestClient client = factory.newRestClient();

    public List<AssetBalance> getAllAssets() {
        return client.getAccount().getBalances();
    }
}
