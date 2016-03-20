package de.twiechert.roleexample.test.security.testutil;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
public class SecuredRestClient extends RestTemplate {

    public SecuredRestClient(CredentialsProvider credsProvider) {
        HttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    public static SecuredRestClient withBasicAuthCredentials(String username, String password) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
        return new SecuredRestClient(credentialsProvider);
    }
}
