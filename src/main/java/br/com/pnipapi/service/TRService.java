package br.com.pnipapi.service;

import br.com.pnipapi.dto.HabilitarTRDTO;
import br.com.pnipapi.dto.SolicitacaoHabilitacaoDTO;
import br.com.pnipapi.dto.documentosAPI.*;
import br.com.pnipapi.exception.BadRequestException;
import br.com.pnipapi.model.SolicitarHabilitacao;
import br.com.pnipapi.repository.SolicitarHabilitacaoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TRService {

    ApiDocumentosRest apiDocumentosRest;

    Logger LOGGER = LoggerFactory.getLogger(TRService.class);

    SolicitarHabilitacaoRepository solicitarHabilitacaoRepository;

    public TRService(ApiDocumentosRest apiDocumentosRest, SolicitarHabilitacaoRepository solicitarHabilitacaoRepository) {
        this.apiDocumentosRest = apiDocumentosRest;
        this.solicitarHabilitacaoRepository = solicitarHabilitacaoRepository;
    }

    @Transactional
    public String solicitarHabilitacao(HabilitarTRDTO habilitarTRDTO) {
        try {

            ProcessoDTO solicitacao = apiDocumentosRest.criarSolicitacaoHabilitacaoTR();
            if (Objects.isNull(solicitacao)) {
                throw new BadRequestException("Falha ao criar a solicitação - API DOCUMENTOS");
            }

            DespachoDTO despachoDTO = apiDocumentosRest.salvarDespacho(buildDespacho(habilitarTRDTO),
                UUID.fromString(solicitacao.getUuid()));
            if (Objects.isNull(despachoDTO)) {
                throw new BadRequestException("Falha ao criar despacho - API DOCUMENTOS");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);


            SolicitarHabilitacao solicitarHabilitacao = new SolicitarHabilitacao();
            solicitarHabilitacao.setMetadado(convertJsonToString(habilitarTRDTO));
            solicitarHabilitacao.setIdUsuario(1L); // TODO REMOVER
            solicitarHabilitacao.setUuidSolicitacao(UUID.fromString(solicitacao.getUuid()));
            solicitarHabilitacao.setDataSolicitacao(new Date(solicitacao.getCriadoEm().getTime()));
            solicitarHabilitacaoRepository.saveAndFlush(solicitarHabilitacao);

            return "Solicitação realizada com sucesso";
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn(e.getMessage());
            return e.getMessage();
        }
    }

    public List<SolicitacaoHabilitacaoDTO> minhasSolicitacoes() {
        // TODO: id do usuario logado
        Long idUsuario = 1L;
        List<Object[]> results = solicitarHabilitacaoRepository.findAllSolicitacoesByIdUsuario(idUsuario);
        if (CollectionUtils.isEmpty(results)) return null;
        return results.stream().map(result ->
            new SolicitacaoHabilitacaoDTO(
                Long.parseLong(result[0].toString()),
                Long.parseLong(result[1].toString()),
                result[2].toString(),
                result[3].toString()
            )).collect(Collectors.toList());
    }

    public HabilitarTRDTO detalhesSolicitacao(String uuid) {
        // TODO: id do usuario logado
        Long idUsuario = 1L;
        SolicitarHabilitacao solicitacao = solicitarHabilitacaoRepository.findSolicitacaoByIdUsuarioAndUid(idUsuario, UUID.fromString(uuid));
        if (solicitacao != null) {
            return convertStringToJson(solicitacao.getMetadado());
        }
        return null;
    }

    private DespachoDTO buildDespacho(HabilitarTRDTO habilitarTRDTO) {
        DespachoDTO despachoDTO = new DespachoDTO();
        despachoDTO.setUsuarioAcao(1); // TODO REMOVER USUARIO ADMIN
        despachoDTO.setCriadoEm(Timestamp.from(Instant.now()));
        despachoDTO.setCodigoDescricao("HABILITACAO_TR");
        despachoDTO.setKind("TRAMITACAO");

        InclusaoDTO inclusaoDados = new InclusaoDTO();
        //inclusaoDados.setArquivosAnexos();

        Map<String, Object> dados = new HashMap<>();
        dados.put("dadosSolicitacao", habilitarTRDTO);
        inclusaoDados.setMetadados(dados);

        despachoDTO.setInclusao(inclusaoDados);

        TramitacaoDTO tramitacaoDIP = new TramitacaoDTO();
        tramitacaoDIP.setMensagem("Solicitação tramitada para o DIP");

        StatusTramitacaoDTO status = new StatusTramitacaoDTO();
        status.setCodigo("1");
        status.setDescricao("TRAMITADO");

        tramitacaoDIP.setStatusTramitacao(status);
        tramitacaoDIP.setUnidadeDestino(1); // // TODO ID DO MPA

        despachoDTO.setTramitacao(tramitacaoDIP);

        return despachoDTO;
    }

    private String convertJsonToString(HabilitarTRDTO solicitarHabilitacao) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(solicitarHabilitacao);
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private HabilitarTRDTO convertStringToJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(json, HabilitarTRDTO.class);
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
