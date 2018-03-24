import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import service.*;

@SpringBootApplication
public class App {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(App.class, args);

        if (args.length != 1) {
            throw new Exception("Wrong number of arguments");
        } 
        
        else if ("total".equals(args[0])) {
            System.out.println("total balance = " + BalanceServiceImpl.getTotalBalance() + " USD");
        }
        
        else if ("bitfinex".equals(args[0])) {
            System.out.println("bitfinex balance = " + BalanceServiceImpl.getBitfinexBalance() + " USD");
        }

        else if ("binance".equals(args[0])) {
            System.out.println("binance balance = " + BalanceServiceImpl.getBinanceBalance() + " USD");
        }

        else if ("bittrex".equals(args[0])) {
            System.out.println("bittrex balance = " + BalanceServiceImpl.getBittrexBalance() + " USD");
        }

        else if ("hitbtc".equals(args[0])) {
            System.out.println("hitBTC balance = " + BalanceServiceImpl.getHitBTCBalance() + " USD");
        }

        else if ("myetherwallet".equals(args[0])) {
            System.out.println("myEtherWallet balance = " + BalanceServiceImpl.getMyEtherWalletBalance() + " USD");
        }
        
        else throw new Exception("Wrong argument");

    }
}