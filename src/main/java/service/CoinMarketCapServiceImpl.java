package service;

import Utils.JsonUtils;
import com.google.gson.JsonObject;
import mapper.ModelCoinMarketCapMapper;
import mapper.ModelMapperCoinMarketCapImpl;
import model.CoinMarketCapTicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CoinMarketCapServiceImpl implements CoinMarketCapService {

    private final ModelCoinMarketCapMapper modelCoinMarketCapMapper = new ModelMapperCoinMarketCapImpl();

    public CoinMarketCapTicker getCoinMarketCapTicker() {
        String url = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";
        JsonObject jsonObject = null;
        CoinMarketCapTicker coinMarketCapTicker = null;

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
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            JsonObject result = JsonUtils.convertStringToJson(sb.toString());
            coinMarketCapTicker = modelCoinMarketCapMapper.mapToCoinMarketCapTicker(result);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return coinMarketCapTicker;
    }
}
