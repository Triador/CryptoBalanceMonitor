package com.triador.cryptobalancemonitor.bitfinex;

import com.triador.cryptobalancemonitor.Utils.PropertyHandler;
import com.binance.api.client.domain.account.AssetBalance;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Bitfinex {

    private String seckey;
    private String pubkey;
    private HttpClient client;

    public Bitfinex(HttpClient client) {
        this (
                client,
                PropertyHandler.getInstance().getValue("bitfinexSeckey"),
                PropertyHandler.getInstance().getValue("bitfinexPubkey")
        );
    }

    public Bitfinex(HttpClient client, String seckey, String pubkey) {
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
            URI address = new URIBuilder()
                    .setScheme("https")
                    .setHost("api.bitfinex.com")
                    .setPath(endpoint)
                    .build();

            RequestBuilder request = RequestBuilder
                    .post(address)
                    .setCharset(charset)
                    .addHeader(HttpHeaders.ACCEPT, "application/json");

            if (endpoint.toLowerCase().startsWith("/v2/auth")) {
                getAuthenticationHeaders(address, body, charset).forEach(request::addHeader);
            }

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

    private String getAuthenticationSignature(URI address, String nonce, JsonObject body) {

        StringBuilder message = new StringBuilder("/api")
                .append(address.getPath())
                .append(nonce);

        if (!body.entrySet().isEmpty()) {
            message.append(body);
        }

        try {
            Mac hmac = Mac.getInstance("HmacSHA384");
            Charset charset = StandardCharsets.US_ASCII;

            hmac.init(new SecretKeySpec(this.seckey.getBytes(charset), "HmacSHA384"));

            return Hex.encodeHexString(
                    hmac.doFinal(message.toString().getBytes(charset)));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException();
        }
    }

    private Collection<Header> getAuthenticationHeaders(URI address, JsonObject body, Charset charset) {
        String nonce = Long.toString(
                Instant.now().toEpochMilli()
        );

        Collection<Header> headers = new ArrayList<>();

        headers.add(new BasicHeader("bfx-nonce", nonce));
        headers.add(new BasicHeader("bfx-apikey", this.pubkey));
        headers.add(new BasicHeader("bfx-signature", getAuthenticationSignature(address, nonce, body)));

        return Collections.unmodifiableCollection(headers);
    }

    public List<AssetBalance> getAllAssets() {

        List<AssetBalance> assetBalances = new ArrayList<>();
        JsonArray jsonAllAssets = post("/v2/auth/r/wallets");
        for (int i = 0; i < jsonAllAssets.size(); i++) {
            JsonArray jsonAsset = jsonAllAssets.get(i).getAsJsonArray();
            AssetBalance assetBalance = new AssetBalance();
            assetBalance.setAsset(jsonAsset.get(1).getAsString());
            assetBalance.setFree(jsonAsset.get(2).getAsString());
            assetBalance.setLocked(jsonAsset.get(3).getAsString());
            assetBalances.add(assetBalance);
        }

        return assetBalances;
    }

}
