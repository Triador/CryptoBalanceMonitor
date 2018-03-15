package binance;

import Utils.Properties;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;

import java.util.List;

public class Binance {

    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
            Properties.getPropertyValue("binanceSeckey"),
            Properties.getPropertyValue("binancePubkey")
    );
    BinanceApiRestClient client = factory.newRestClient();

    public List<AssetBalance> getBalances() {

        Account account = new Account();
        return account.getBalances();
    }
}
