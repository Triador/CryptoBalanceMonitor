package bitfinex;

import Utils.Properties;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
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

public class Bitfinex {

    private String seckey;
    private String pubkey;
    private HttpClient client;

    public Bitfinex(HttpClient client) {
        this (
                client,
                Properties.getPropertyValue("bitfinexSeckey"),
                Properties.getPropertyValue("bitfinexPubkey")
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

    private String getAuthenticationSignature(URI address, String nonce, JsonObject body) {
        StringBuilder message = new StringBuilder("/api")
                .append(address.getPath())
                .append(nonce);

        if (!body.isJsonNull()) {
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
        headers.add(new BasicHeader("bfx-signature", this.getAuthenticationSignature(address, nonce, body)));

        return Collections.unmodifiableCollection(headers);
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
                this.getAuthenticationHeaders(address, body, charset).forEach(request::addHeader);
            }

            if (body.isJsonNull()) {
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
            Bitfinex example = new Bitfinex(client);

            // Get active orders.
            System.out.println(example.post("/v2/auth/r/orders"));

            // Get account funding info.
            JsonObject payload = new JsonObject();

            payload.addProperty("dir", 1);
            payload.addProperty("rate", 800);
            payload.addProperty("type", "EXCHANGE");
            payload.addProperty("symbol", "tBTCUSD");

            System.out.println(example.post("/v2/auth/r/info/funding/fUSD", payload));

            // Calculate the average execution rate for Trading or Margin funding.
            Collection<NameValuePair> parameters = new ArrayList<>();

            parameters.add(new BasicNameValuePair("symbol", "tBTCUSD"));
            parameters.add(new BasicNameValuePair("amount", "1.123"));

            System.out.println(example.post("/v2/calc/trade/avg", parameters));

            System.out.println(example.post("/v2/auth/r/wallets"));
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

}
