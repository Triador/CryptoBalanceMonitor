package com.triador.cryptobalancemonitor.etherscan;

import java.math.BigDecimal;

public interface EtherScannerService {
    BigDecimal getMyEtherWalletBalance();
}
