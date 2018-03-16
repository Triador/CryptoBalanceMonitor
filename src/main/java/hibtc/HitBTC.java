package hibtc;

import Utils.PropertyHandler;
import com.google.gson.JsonObject;
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

public class HitBTC {
    private String seckey;
    private String pubkey;
    private HttpClient client;

    public HitBTC(HttpClient client) {
        this (
                client,
                PropertyHandler.getInstance().getValue("hitBTCPubkey"),
                PropertyHandler.getInstance().getValue("hitBTCSeckey")
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

    public String post(String endpoint) {
        return this.post(endpoint, Collections.<NameValuePair>emptyList());
    }

    public String post(String endpoint, Collection<NameValuePair> parameters) {
        return this.post(endpoint, parameters, new JsonObject());
    }

    public String post(String endpoint, JsonObject body) {
        return this.post(endpoint, Collections.<NameValuePair>emptyList(), body, StandardCharsets.UTF_8);
    }

    public String post(String endpoint, Collection<NameValuePair> parameters, JsonObject body) {
        return this.post(endpoint, parameters, body, StandardCharsets.UTF_8);
    }

    public String post(String endpoint, JsonObject body, Charset charset) {
        return this.post(endpoint, Collections.<NameValuePair>emptyList(), body, StandardCharsets.UTF_8);
    }

    public String post(String endpoint, Collection<NameValuePair> parameters, JsonObject body, Charset charset) {
        try {
                        String nonce = Long.toString(
                    Instant.now().toEpochMilli()
            );
//            request.addParameter("pubkey", this.pubkey);
//            request.addParameter("seckey", this.seckey);
            URI address = new URIBuilder()
                    .setScheme("https")
                    .setHost("api.hitbtc.com")
                    .setPath(endpoint)
                    .setParameter("nonce", nonce)
                    .setParameter("pubkey", this.pubkey)
                    .setParameter("seckey", this.seckey)
                    .build();

            RequestBuilder request = RequestBuilder
                    .get(address)
                    .setCharset(charset)
                    .addHeader(HttpHeaders.ACCEPT, "application/json");

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

//            return new JsonParser().parse(EntityUtils.toString(
//                    this.client.execute(request.build()).getEntity()
//            )).getAsJsonArray();
            return EntityUtils.toString(
                    this.client.execute(request.build()).getEntity()
            );
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
}
