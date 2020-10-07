package example;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.libra.AuthKey;
import org.libra.jsonrpc.InvalidResponseException;

import java.net.URI;
import java.net.URISyntaxException;

import static org.libra.Testnet.FAUCET_SERVER_URL;

/**
 * MintExample demonstrates how to add currencies to account on the Libra blockchain
 * The mint also use to create new account by adding currencies base on new auth_key
 */
public class MintExample {
    public static void mint(AuthKey authKey, String amount, String currencyCode) {
        URI build;

        try {
            URIBuilder builder = new URIBuilder(FAUCET_SERVER_URL);

            builder.setParameter("amount", amount).setParameter("auth_key", authKey.hex().toLowerCase())
                    .setParameter("currency_code", currencyCode);
            build = builder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        System.out.println(build);

        HttpPost post = new HttpPost(build);
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpResponse response = httpClient.execute(post);
            String body = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new InvalidResponseException(response.getStatusLine().getStatusCode(), body);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
