package br.com.pnipapi.service;

import br.com.pnipapi.config.ApiProperties;
import br.com.pnipapi.utils.helper.KeyCloakRestTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class ApiPlanilhasRest {

    KeyCloakRestTemplate restTemplateHelper;

    ApiProperties apiProperties;

    public ApiPlanilhasRest(KeyCloakRestTemplate restTemplateHelper, ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
        this.restTemplateHelper = restTemplateHelper;
    }

    private URI getURI(String url) {
        return restTemplateHelper.getURI(apiProperties.getPlanilhasBaseUrl(), url);
    }

}
