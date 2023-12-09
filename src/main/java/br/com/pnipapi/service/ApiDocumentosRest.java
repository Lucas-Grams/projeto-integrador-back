package br.com.pnipapi.service;

import br.com.pnipapi.config.ApiProperties;
import br.com.pnipapi.dto.ResponseDTO;
import br.com.pnipapi.dto.documentosAPI.DespachoDTO;
import br.com.pnipapi.dto.documentosAPI.ProcessoDTO;
import br.com.pnipapi.exception.BadRequestException;
import br.com.pnipapi.utils.helper.KeyCloakRestTemplate;
import br.com.pnipapi.utils.helper.KeyCloakRestTemplateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.*;

@Service
public class ApiDocumentosRest {

    Logger LOGGER = LoggerFactory.getLogger(ApiDocumentosRest.class);

    KeyCloakRestTemplate restTemplateHelper;

    ApiProperties apiProperties;

    public ApiDocumentosRest(KeyCloakRestTemplate restTemplateHelper, ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
        this.restTemplateHelper = restTemplateHelper;
    }

    private URI getURI(String url) {
        return restTemplateHelper.getURI(apiProperties.getProcessosBaseUrl(), url);
    }

    @Transactional
    public ProcessoDTO criarSolicitacaoHabilitacaoTR() {
        try {
            URI url = restTemplateHelper.getURI(apiProperties.getProcessosBaseUrl(), "processos/");
            ResponseEntity entity = restTemplateHelper.postEntity(url,
                new ProcessoDTO("HABILITACAO_TR", 1)); // TODO REMOVER ID USARIO ADMIN

            if (entity == null || entity.getStatusCode() != HttpStatus.OK) return null;
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(entity.getBody().toString(), ProcessoDTO.class);
        } catch (KeyCloakRestTemplateException e) {
            e.printStackTrace();
            LOGGER.warn(e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    @Transactional
    public DespachoDTO salvarDespacho(DespachoDTO despachoDTO, UUID uuid) {
        try {
            URI url = restTemplateHelper.getURI(apiProperties.getProcessosBaseUrl(), "despachos/" + uuid);

            ResponseEntity entity = restTemplateHelper.postEntity(url, despachoDTO);
            if (entity == null || entity.getStatusCode() != HttpStatus.OK) return null;
            ObjectMapper mapper = new ObjectMapper();
            DespachoDTO response = mapper.readValue(entity.getBody().toString(), DespachoDTO.class);

            return response;
        } catch (KeyCloakRestTemplateException e) {
            e.printStackTrace();
            LOGGER.warn(e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<String> findAllDespachos() {
        try {
            URI url = restTemplateHelper.getURI(apiProperties.getProcessosBaseUrl(), "despachos/");

            ResponseEntity entity = restTemplateHelper.getEntity(url, new TypeReference<ResponseDTO>(){});
            if (entity == null || entity.getStatusCode() != HttpStatus.OK) return new ArrayList<>();
            ResponseDTO response = (ResponseDTO) entity.getBody();
            return (List<String>) response.transform(new TypeReference<List<String>>() {});
        } catch (KeyCloakRestTemplateException e) {
            e.printStackTrace();
            LOGGER.warn("Falha ao buscar no endpoint - findAllDespachos (DOCUMENTOS_API)");
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn("Falha ao buscar no endpoint - findAllDespachos (DOCUMENTOS_API)");
            throw new BadRequestException(e.getMessage());
        }
    }

}
