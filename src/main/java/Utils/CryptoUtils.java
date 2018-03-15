package Utils;

import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CryptoUtils {

    public static String getAuthenticationSignature(URI address, String nonce, JsonObject body, String seckey) {

        StringBuilder message = new StringBuilder("/api")
                .append(address.getPath())
                .append(nonce);

        if (!body.isJsonNull()) {
            message.append(body);
        }

        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            Charset charset = StandardCharsets.US_ASCII;

            hmac.init(new SecretKeySpec(seckey.getBytes(charset), "HmacSHA512"));

            return Hex.encodeHexString(
                    hmac.doFinal(message.toString().getBytes(charset)));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException();
        }
    }

    public static Collection<Header> getAuthenticationHeaders(URI address, JsonObject body, Charset charset, String pubkey, String seckey) {
        String nonce = Long.toString(
                Instant.now().toEpochMilli()
        );

        Collection<Header> headers = new ArrayList<>();

        headers.add(new BasicHeader("bfx-nonce", nonce));
        headers.add(new BasicHeader("bfx-apikey", pubkey));
        headers.add(new BasicHeader("bfx-signature", CryptoUtils.getAuthenticationSignature(address, nonce, body, seckey)));

        return Collections.unmodifiableCollection(headers);
    }
}
