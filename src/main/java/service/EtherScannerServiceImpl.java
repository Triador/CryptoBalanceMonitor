package service;

import Utils.ConvertUtils;
import Utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class EtherScannerServiceImpl implements EtherScannerService {

    public BigDecimal getMyEtherWalletBalance() {
        String url = "https://api.etherscan.io/api?module=account&action=balance&address=0xfff28a5c5410af2257fadee512b7da05ec3a757f&tag=latest&apikey=YS16T1GMQ9Q21MD173IXYFPRQKZ58S9SFM";
        BigDecimal balance = null;

        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("Content-type", "application/json");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String input = in.readLine();
            in.close();

            JsonObject jsonObject = JsonUtils.convertStringToJson(input);
            String result = jsonObject.get("result").getAsString();
            balance = ConvertUtils.convertWeiToEther(result);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return balance;
    }
}
