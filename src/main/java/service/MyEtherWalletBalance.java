package service;

import coinmarketcap.CoinMarketCapService;
import etherscan.EtherScannerService;


import java.math.BigDecimal;

public class MyEtherWalletBalance implements BalanceService {

    private final EtherScannerService etherScannerService;
    private final CoinMarketCapService coinMarketCapService;

    public MyEtherWalletBalance(EtherScannerService etherScannerService, CoinMarketCapService coinMarketCapService) {
        this.etherScannerService = etherScannerService;
        this.coinMarketCapService = coinMarketCapService;
    }

    @Override
    public BigDecimal getBalance() {

        BigDecimal myEtherWalletBalance = etherScannerService.getMyEtherWalletBalance();
        BigDecimal usdPriceForETH = new BigDecimal(coinMarketCapService.getCoinMarketCapTicker("ethereum").getPriceUSD());

        return myEtherWalletBalance.multiply(usdPriceForETH).setScale(1, 1);
    }
}
