package com.triador.Utils;

import java.math.BigDecimal;

public class ConvertUtils {
    public static BigDecimal convertWeiToEther(String number) {
        BigDecimal wei = new BigDecimal(number);
        return wei.divide(BigDecimal.TEN.pow(18));
    }
}
