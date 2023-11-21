package br.com.pnipapi.utils.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@Component
@EnableAsync
public class KeyCloakRestTemplate {
    private static final Logger LOGGER = Logger.getLogger(KeyCloakRestTemplate.class.getName());

    private KeycloakRestTemplate keycloakRestTemplate;

    private HttpHeaders headers = new HttpHeaders();

    public KeyCloakRestTemplate(KeycloakRestTemplate restTemplate, HttpHeaders headers) {
        this.headers = headers;
        this.keycloakRestTemplate = restTemplate;
    }


    public KeyCloakRestTemplate() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        SSLContext context = null;
        try {
            ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
                @Override
                public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    HeaderElementIterator it = new BasicHeaderElementIterator
                            (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                    while (it.hasNext()) {
                        HeaderElement he = it.nextElement();
                        String param = he.getName();
                        String value = he.getValue();
                        if (value != null && param.equalsIgnoreCase
                                ("timeout")) {
                            return Long.parseLong(value) * 1000;
                        }
                    }
                    return 5 * 1000;
                }
            };

            context = SSLContext.getInstance("TLSv1.2");
            context.init(null, null, null);
            CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(context)
                    .setKeepAliveStrategy(myStrategy)
                    .build();
            KeycloakClientRequestFactory factory = new KeycloakClientRequestFactory();
            factory.setHttpClient(httpClient);
            factory.setConnectTimeout(60000);
            factory.setReadTimeout(60000);
            keycloakRestTemplate = new KeycloakRestTemplate(factory);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public ResponseEntity fetchEntity(HttpMethod method, URI uri, Object entity) throws KeyCloakRestTemplateException {
        try {
            return keycloakRestTemplate.exchange(uri, method, new HttpEntity<>(entity, headers), String.class);
        } catch (Exception e) {
            LOGGER.warning("Error [" + method.name() + "] entity: " + e.getMessage());
            throw new KeyCloakRestTemplateException("Error [" + method.name() + "] entity: ", e);
        }
    }

    public ResponseEntity postEntity(URI uri, Object entity) throws KeyCloakRestTemplateException {
        return fetchEntity(HttpMethod.POST, uri, entity);
    }

    public ResponseEntity patchEntity(URI uri, Object entity) throws KeyCloakRestTemplateException {
        return fetchEntity(HttpMethod.PATCH, uri, entity);
    }

    public ResponseEntity deleteEntity(URI uri, Object entity) throws KeyCloakRestTemplateException {
        return fetchEntity(HttpMethod.DELETE, uri, entity);
    }

    public <T> ResponseEntity getEntity(URI uri, TypeReference<T> reference) throws KeyCloakRestTemplateException {
        try {
            ResponseEntity exchange = keycloakRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), Object.class);
            if (exchange.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return ResponseEntity.ok().body(mapper.convertValue(exchange.getBody(), reference));
            }
            return exchange;
        } catch (Exception e) {
            LOGGER.warning("Error get entity: " + e.getMessage());
            throw new KeyCloakRestTemplateException("Error get entity: ", e);
        }
    }
}


