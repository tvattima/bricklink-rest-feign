package com.bricklink.api.rest.support;

import com.bricklink.api.rest.client.BricklinkRestClient;
import feign.Request;
import feign.RequestTemplate;
import feign.Target;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class BricklinkTarget<T> implements Target<T> {

    private String consumerKey;
    private String consumerSecret;
    private String tokenValue;
    private String tokenSecret;
    private URI uri;
    private String authorizationHeader = "OAuth realm=\"\",oauth_consumer_key=\"{0}\",oauth_token=\"{1}\",oauth_signature_method=\"HMAC-SHA1\",oauth_signature=\"{2}\",oauth_timestamp=\"{3}\",oauth_nonce=\"{4}\",oauth_version=\"1.0\"";

    public BricklinkTarget(String consumerKey, String consumerSecret, String tokenValue, String tokenSecret, URI uri) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.tokenValue = tokenValue;
        this.tokenSecret = tokenSecret;
        this.uri = uri;
    }

    @Override
    public Request apply(RequestTemplate input) {
        if (input.url().indexOf("http") != 0) {
            input.insert(0, url());
        }

        BLAuthSigner signer = new BLAuthSigner(consumerKey, consumerSecret);
        signer.setToken(tokenValue, tokenSecret);
        signer.setVerb(input.method());
        signer.setURL(input.url());

//        input.queries().keySet().forEach(k -> {
//            input.queries().get(k).forEach(v -> {
//                log.info("Adding [{},{}] to signer", k, v);
//                signer.addParameter(k, v);
//            });
//        });
        input.queries()
             .keySet()
             .forEach(k -> {
                 signer.addParameter(k, String.join(",", input.queries()
                                                              .get(k)));
             });
        try {
            input.header("Authorization", getAuthorizationHeader(signer.getFinalOAuthParams()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return input.request();
    }

    @Override
    public Class type() {
        return BricklinkRestClient.class;
    }

    @Override
    public String name() {
        return "Bricklink REST API";
    }

    @Override
    public String url() {
        return uri.toString();
    }

    private String getFirstValue(Collection<String> strings) {
        String value = null;
        for (String string : strings) {
            value = string;
            break;
        }
        return value;
    }

    private String getAuthorizationHeader(Map<String, String> oauthParameters) {
        return MessageFormat.format(authorizationHeader,
                oauthParameters.get("oauth_consumer_key"),
                oauthParameters.get("oauth_token"),
                oauthParameters.get("oauth_signature"),
                oauthParameters.get("oauth_timestamp"),
                oauthParameters.get("oauth_nonce"));
    }
}
