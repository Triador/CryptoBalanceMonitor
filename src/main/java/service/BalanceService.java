package service;

import java.math.BigDecimal;

public interface BalanceService {

    BigDecimal getTotalBalance();
    BigDecimal getBinanceBalance();
    BigDecimal getBitfinexBalance();
    BigDecimal getBittrexBalance();
    BigDecimal getHitBTCBalance();
    BigDecimal getMyEtherWalletBalance();
}
