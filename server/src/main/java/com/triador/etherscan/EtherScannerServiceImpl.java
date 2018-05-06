package com.triador.etherscan;

import com.triador.Utils.ConvertUtils;
import com.triador.Utils.JsonUtils;
import com.triador.Utils.PropertyHandler;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class EtherScannerServiceImpl implements EtherScannerService {

    public BigDecimal getMyEtherWalletBalance() {
        String address = PropertyHandler.getInstance().getValue("myEtherWalletAddress");
        String url = "https://api.etherscan.io/api?module=account&action=balance&address=0x" + address + "&tag=latest&apikey=YS16T1GMQ9Q21MD173IXYFPRQKZ58S9SFM";
        BigDecimal balance = null;

        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/json");

            BufferedReader in = null;
            String input = "";
            while (input.equals("")) {
                 in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                input = in.readLine();
            }
            in.close();

            JsonObject jsonObject = JsonUtils.convertStringToJsonObject(input);
            String result = jsonObject.get("result").getAsString();
            balance = ConvertUtils.convertWeiToEther(result);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return balance;
    }
}
