package service;

import Utils.Properties;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;

import java.math.BigDecimal;
import java.util.List;

public class BinanceServiceImpl implements BinanceService {

    private static final BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
            Properties.getPropertyValue("binanceSeckey"),
            Properties.getPropertyValue("binancePubkey")
    );
    private static final BinanceApiRestClient client = factory.newRestClient();

    @Override
    public BigDecimal getTotalBalance() {

        Account account = new Account();
        List<AssetBalance> assetBalances = account.getBalances();
        BigDecimal totalBalance = new BigDecimal(0);

        for (AssetBalance assetBalance: assetBalances) {
            totalBalance =  totalBalance.add(new BigDecimal(assetBalance.getFree()));
        }

        return totalBalance;
    }
}
