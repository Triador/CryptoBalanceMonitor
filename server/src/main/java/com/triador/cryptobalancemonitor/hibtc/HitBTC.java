package com.triador.cryptobalancemonitor.hibtc;

import com.triador.cryptobalancemonitor.Utils.PropertyHandler;
import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.*;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HitBTC {
    private String seckey;
    private String pubkey;
    private HttpClient client;

    public HitBTC(HttpClient client) {
        this (
                client,
                PropertyHandler.getInstance().getValue("hitBTCSeckey"),
                PropertyHandler.getInstance().getValue("hitBTCPubkey")
        );
    }

    public HitBTC(HttpClient client, String seckey, String pubkey) {
        if (client == null) {
            throw new IllegalArgumentException("client");
        }

        if (seckey == null || seckey.isEmpty()) {
            throw new IllegalArgumentException("seckey");
        }

        if (pubkey == null || pubkey.isEmpty()) {
            throw new IllegalArgumentException("pubkey");
        }

        this.client = client;
        this.seckey = seckey;
        this.pubkey = pubkey;
    }

    public JsonArray post(String endpoint) {
        return this.post(endpoint, Collections.<NameValuePair>emptyList());
    }

    public JsonArray post(String endpoint, Collection<NameValuePair> parameters) {
        return this.post(endpoint, parameters, new JsonObject());
    }

    public JsonArray post(String endpoint, JsonObject body) {
        return this.post(endpoint, Collections.<NameValuePair>emptyList(), body, StandardCharsets.UTF_8);
    }

    public JsonArray post(String endpoint, Collection<NameValuePair> parameters, JsonObject body) {
        return this.post(endpoint, parameters, body, StandardCharsets.UTF_8);
    }

    public JsonArray post(String endpoint, JsonObject body, Charset charset) {
        return this.post(endpoint, Collections.<NameValuePair>emptyList(), body, StandardCharsets.UTF_8);
    }

    public JsonArray post(String endpoint, Collection<NameValuePair> parameters, JsonObject body, Charset charset) {
        try {
                        String nonce = Long.toString(
                    Instant.now().toEpochMilli()
            );
            URI address = new URIBuilder()
                    .setScheme("https")
                    .setHost("api.hitbtc.com")
                    .setPath(endpoint)
                    .build();

            RequestBuilder request = RequestBuilder
                    .get(address)
                    .setCharset(charset)
                    .addHeader(HttpHeaders.ACCEPT, "application/json")
                    .addHeader(BasicScheme.authenticate(
                            new UsernamePasswordCredentials(this.pubkey, this.seckey),
                            "UTF-8", false));

            if (body.entrySet().isEmpty()) {
                request.setEntity(new ByteArrayEntity(new byte[0]));
            } else {
                request.setEntity(new ByteArrayEntity(body.toString().getBytes(charset)));
                request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=" + charset);
            }

            if (!parameters.isEmpty()) {
                request.addParameters(parameters.toArray(
                        new NameValuePair[parameters.size()]
                ));
            }

            return new JsonParser().parse(EntityUtils.toString(
                    this.client.execute(request.build()).getEntity()
            )).getAsJsonArray();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException();
        }
    }


    public static void main(String[] args) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HitBTC example = new HitBTC(client);
            System.out.println(example.post("/api/2/account/balance"));
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    public List<AssetBalance> getAllAssets() {

        List<AssetBalance> assetBalances = new ArrayList<>();
        JsonArray jsonAllAssets = post("/api/2/account/balance");

        for (int i = 0; i < jsonAllAssets.size(); i++) {

            JsonObject jsonAsset = jsonAllAssets.get(i).getAsJsonObject();
            AssetBalance assetBalance;
            BigDecimal available = new BigDecimal(jsonAsset.get("available").getAsString());
            BigDecimal reserved = new BigDecimal(jsonAsset.get("reserved").getAsString());

            if (available.compareTo(BigDecimal.ZERO) > 0) {

                assetBalance = new AssetBalance();
                assetBalance.setAsset(jsonAsset.get("currency").getAsString());
                assetBalance.setFree(available.toString());

                if (reserved.compareTo(BigDecimal.ZERO) > 0) {
                    assetBalance.setLocked(reserved.toString());
                }
                else assetBalance.setLocked("0");

                assetBalances.add(assetBalance);
            }
        }

        return assetBalances;
    }
}
