import com.google.gson.JsonObject;
import service.CoinMarketCapService;
import service.CoinMarketCapServiceImpl;
import service.EtherScannerService;
import service.EtherScannerServiceImpl;

public class App {

    private static EtherScannerService etherScannerService = new EtherScannerServiceImpl();
    private static CoinMarketCapService coinMarketCapService = new CoinMarketCapServiceImpl();

    public static void main(String[] args) throws Exception {

        System.out.println("\nTesting - Send Http GET request to myetherwallet");
        System.out.println(etherScannerService.getMyEtherWalletBalance());
        System.out.println("\nTesting - Send Http GET request to coinmarketcap");
        System.out.println(coinMarketCapService.getCoinMarketCapTicker().getPriceUSD());

    }
}