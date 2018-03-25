package com.triador.cryptobalancemonitor.coinmarketcap;

import com.triador.cryptobalancemonitor.Utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.triador.cryptobalancemonitor.mapper.ModelCoinMarketCapMapper;
import com.triador.cryptobalancemonitor.mapper.ModelMapperCoinMarketCapImpl;
import com.triador.cryptobalancemonitor.model.CoinMarketCapTicker;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class CoinMarketCapServiceImpl implements CoinMarketCapService {

    private final ModelCoinMarketCapMapper modelCoinMarketCapMapper = new ModelMapperCoinMarketCapImpl();
    private HttpClient client;

    public CoinMarketCapServiceImpl(HttpClient client) {
        this.client = client;
    }

    public CoinMarketCapTicker getCoinMarketCapTicker(String coin) {
        String url = "https://api.coinmarketcap.com/v1/ticker/" + coin + "/";
        CoinMarketCapTicker coinMarketCapTicker = null;

        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/json");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            JsonObject result = JsonUtils.convertStringToJsonObject(sb.toString());
            coinMarketCapTicker = modelCoinMarketCapMapper.mapToCoinMarketCapTicker(result);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return coinMarketCapTicker;
    }

    public Map<String, CoinMarketCapTicker> getCoinMarketCapTickers() {
        Map<String, CoinMarketCapTicker> coinMarketCapTickers = new HashMap<>();

        try {

            int start = 0;
            BufferedWriter bw = new BufferedWriter(new FileWriter("coinmarketcap.json"));
            bw.write("{");
            for (int i = 0; i < 16; i++) {
                URI address = new URIBuilder()
                        .setScheme("https")
                        .setHost("api.com.triador.cryptobalancemonitor.coinmarketcap.com")
                        .setPath("v1/ticker/")
                        .setParameter("start", String.valueOf(start))
                        .build();

                RequestBuilder request = RequestBuilder
                        .get(address)
                        .setCharset(Charset.defaultCharset())
                        .addHeader(HttpHeaders.ACCEPT, "application/json");

                JsonArray jsonCoinMarketCapTickers = new JsonParser().parse(EntityUtils.toString(
                        this.client.execute(request.build()).getEntity()
                )).getAsJsonArray();
                for (int j = 0; j < jsonCoinMarketCapTickers.size(); j++) {
                    CoinMarketCapTicker coinMarketCapTicker = modelCoinMarketCapMapper.mapToCoinMarketCapTicker(jsonCoinMarketCapTickers.get(j).getAsJsonObject());
                    bw.write("\"" + coinMarketCapTicker.getSymbol() + "\": " + "\"" + coinMarketCapTicker.getId() + "\"" + ",");
                    coinMarketCapTickers.put(coinMarketCapTicker.getSymbol(), coinMarketCapTicker);
                }

                start += 100;
            }
            bw.write("}");
            bw.close();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return coinMarketCapTickers;
    }
}
