package br.com.pnipapi.service;

import br.com.pnipapi.dto.*;
import br.com.pnipapi.dto.documentosAPI.*;
import br.com.pnipapi.exception.BadRequestException;
import br.com.pnipapi.model.*;
import br.com.pnipapi.repository.*;
import br.com.pnipapi.service.storage.StorageObject;
import br.com.pnipapi.service.storage.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TRService {

    ApiDocumentosRest apiDocumentosRest;

    Logger LOGGER = LoggerFactory.getLogger(TRService.class);

    StorageService storageService;

    StatusRepository statusRepository;

    ArquivoRepository arquivoRepository;

    ArquivoSolicitarHabilitacaoRepository arquivoSolicitarHabilitacaoRepository;

    EmbarcacaoRepository embarcacaoRepository;
    EmbarcacaoSolicitarHabilitacaoRepository embarcacaoSolicitarHabilitacaoRepository;
    SolicitarHabilitacaoRepository solicitarHabilitacaoRepository;
    StatusSolicitarHabilitacaoRepository statusSolicitarHabilitacaoRepository;

    public TRService(ApiDocumentosRest apiDocumentosRest, SolicitarHabilitacaoRepository solicitarHabilitacaoRepository,
        StatusRepository statusRepository, StatusSolicitarHabilitacaoRepository statusSolicitarHabilitacaoRepository,
        EmbarcacaoRepository embarcacaoRepository, EmbarcacaoSolicitarHabilitacaoRepository embarcacaoSolicitarHabilitacaoRepository,
        StorageService storageService, ArquivoSolicitarHabilitacaoRepository arquivoSolicitarHabilitacaoRepository,
        ArquivoRepository arquivoRepository) {
        this.statusRepository = statusRepository;
        this.apiDocumentosRest = apiDocumentosRest;
        this.embarcacaoRepository = embarcacaoRepository;
        this.solicitarHabilitacaoRepository = solicitarHabilitacaoRepository;
        this.statusSolicitarHabilitacaoRepository = statusSolicitarHabilitacaoRepository;
        this.embarcacaoSolicitarHabilitacaoRepository = embarcacaoSolicitarHabilitacaoRepository;
        this.storageService = storageService;
        this.arquivoSolicitarHabilitacaoRepository = arquivoSolicitarHabilitacaoRepository;
        this.arquivoRepository = arquivoRepository;
    }

    public List<SolicitarHabilitacao> findAll() {
        return solicitarHabilitacaoRepository.findAll();
    }

    public SolicitarHabilitacao findSolicitacaoByUuid(UUID uuid) {
        return solicitarHabilitacaoRepository.findByUuid(uuid).get();
    }

    public List<SolicitarHabilitacao> findSolicitacoesByStatus(String status) {
        return solicitarHabilitacaoRepository.findSolicitacoesByStatus(status);
    }

    @Transactional
    public String solicitarHabilitacao(HabilitarTRDTO habilitarTRDTO) {
        try {

            List<ArquivoAnexoSolicitacaoDTO> anexos = extrairAnexosDaSolicitacao(habilitarTRDTO);

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

            SolicitarHabilitacaoDTO solicitarHabilitacaoDTO = new SolicitarHabilitacaoDTO();
            solicitarHabilitacaoDTO.setDespachoDTO(despachoDTO);
            solicitarHabilitacaoDTO.setHabilitarTRDTO(habilitarTRDTO);

            SolicitarHabilitacao solicitarHabilitacao = new SolicitarHabilitacao();
            solicitarHabilitacao.setSolicitante(habilitarTRDTO.getNome());

            Status status = statusRepository.findByDescricao(StatusSolicitacao.EM_ANALISE.name()).get();
            solicitarHabilitacao.setStatus(status.getDescricao());

            solicitarHabilitacao.setMetadado(convertJsonToString(solicitarHabilitacaoDTO));
            solicitarHabilitacao.setIdUsuario(1L); // TODO REMOVER
            solicitarHabilitacao.setUuidSolicitacao(UUID.fromString(solicitacao.getUuid()));
            solicitarHabilitacao.setDataSolicitacao(new Date(solicitacao.getCriadoEm().getTime()));
            // TODO REVER
            solicitarHabilitacao.setProtocolo(Long.toString(new java.util.Date().getTime()));

            // Salva solicitação
            SolicitarHabilitacao solicitarHabilitacaoSalvo = solicitarHabilitacaoRepository.saveAndFlush(solicitarHabilitacao);

            // Upload dos anexo
            uploadAnexoSolicitacao(solicitarHabilitacaoSalvo, anexos);

            // Salva status solicitação
            statusSolicitarHabilitacaoRepository.saveAndFlush(new StatusSolicitarHabilitacao(status.getId(),
                solicitarHabilitacaoSalvo.getId()));

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
                result[3].toString(),
                result[4].toString()
            )).collect(Collectors.toList());
    }

    public HabilitarTRDTO detalhesSolicitacao(String uuid) {
        // TODO: id do usuario logado
        Long idUsuario = 1L;
        SolicitarHabilitacao solicitacao = solicitarHabilitacaoRepository.findSolicitacaoByIdUsuarioAndUid(idUsuario, UUID.fromString(uuid));
        if (solicitacao != null) {
            SolicitarHabilitacaoDTO solicitarHabilitacaoDTO = convertStringToJson(solicitacao.getMetadado());
            return solicitarHabilitacaoDTO.getHabilitarTRDTO();
        }
        return null;
    }

    private List<ArquivoAnexoSolicitacaoDTO> extrairAnexosDaSolicitacao(HabilitarTRDTO dto) {
        List<ArquivoAnexoSolicitacaoDTO> anexos = new ArrayList<>();
        if (StringUtils.hasText(dto.getCopiaHabilitacao()) && StringUtils.hasText(dto.getCopiaHabilitacaoBase64())) {
            String nomeUpload = String.format("%s.pdf", UUID.randomUUID());
            anexos.add(ArquivoAnexoSolicitacaoDTO.builder()
                .tipoAnexo(ArquivoAnexoSolicitacaoDTO.TipoAnexo.COPIA_HABILITACAO)
                .nomeUpload(nomeUpload)
                .nomeOriginal(dto.getCopiaHabilitacao())
                .base64(dto.getCopiaHabilitacaoBase64())
                .build());
            dto.setCopiaHabilitacao(nomeUpload);
            dto.setCopiaHabilitacaoBase64(null);
        }
        if (StringUtils.hasText(dto.getDiplomaCertificacao()) && StringUtils.hasText(dto.getDiplomaCertificacaoBase64())) {
            String nomeUpload = String.format("%s.pdf", UUID.randomUUID());
            anexos.add(ArquivoAnexoSolicitacaoDTO.builder()
                .tipoAnexo(ArquivoAnexoSolicitacaoDTO.TipoAnexo.DIPLOMA_CERTIFICADO)
                .nomeUpload(nomeUpload)
                .nomeOriginal(dto.getDiplomaCertificacao())
                .base64(dto.getDiplomaCertificacaoBase64())
                .build());
            dto.setDiplomaCertificacao(nomeUpload);
            dto.setDiplomaCertificacaoBase64(null);
        }
        if (!CollectionUtils.isEmpty(dto.getEmbarcacoes())) {
            dto.getEmbarcacoes().stream().forEach(e -> {
                String nomeUpload = String.format("%s.pdf", UUID.randomUUID());
                anexos.add(ArquivoAnexoSolicitacaoDTO.builder()
                    .tipoAnexo(ArquivoAnexoSolicitacaoDTO.TipoAnexo.EMBARCACAO)
                    .nomeUpload(nomeUpload)
                    .nomeOriginal(e.getDeclaracaoProprietario())
                    .base64(e.getDeclaracaoProprietarioBase64())
                    .idEmbarcacao(e.getId())
                    .build());
                e.setDeclaracaoProprietario(nomeUpload);
                e.setDeclaracaoProprietarioBase64(null);
            });
        }
        return anexos;
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

    private String convertJsonToString(SolicitarHabilitacaoDTO solicitarHabilitacao) {
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

    private SolicitarHabilitacaoDTO convertStringToJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(json, SolicitarHabilitacaoDTO.class);
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public String finalizarSolicitacao(FinalizarSolicitacaoDTO finalizarSolicitacaoDTO) {
        try {

            SolicitarHabilitacao solicitacao = findByUuid(finalizarSolicitacaoDTO.getUuidSolicitacao());

            String statusRequest = "deferir".equals(finalizarSolicitacaoDTO.getStatusSolicitacao())?
                StatusSolicitacao.DEFERIDA.name(): StatusSolicitacao.INDEFERIDA.name();

            solicitacao.setStatus(statusRepository.findByDescricao(statusRequest).get().getDescricao());
            solicitacao.setObservacao(finalizarSolicitacaoDTO.getMsgSolicitacao());

            solicitarHabilitacaoRepository.saveAndFlush(solicitacao);

            // vincula as embarcações aprovadas ou reprovadas (manter histórico)
            finalizarSolicitacaoDTO.getEmbarcacoes().forEach(embarcacao -> {
                embarcacaoSolicitarHabilitacaoRepository.saveAndFlush(new EmbarcacaoSolicitarHabilitacao(embarcacao.getId(),
                    solicitacao.getId(), embarcacao.getAprovado()));
            });

            return "Ação realizada com sucesso";
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn(e.getMessage());
            return e.getMessage();
        }
    }

    private void uploadAnexoSolicitacao(SolicitarHabilitacao solicitacao, List<ArquivoAnexoSolicitacaoDTO> anexos) {
        anexos.forEach(anexo -> {
            String filepath = String.format("solicitacao/%s/anexo/%s", solicitacao.getUuidSolicitacao().toString(), anexo.getNomeUpload());
            Path path = Paths.get(filepath);
            storageService.save(path, getBytesFromBase64(anexo.getBase64()));

            Arquivo arquivo = new Arquivo();
            arquivo.setNome(anexo.getNomeUpload());
            arquivo.setOrigemArquivo(anexo.getNomeOriginal());
            arquivo.setCaminho(filepath);
            arquivo.setTipo("application/pdf");
            arquivo = arquivoRepository.saveAndFlush(arquivo);

            arquivoSolicitarHabilitacaoRepository.saveAndFlush(new ArquivoSolicitarHabilitacao(
                arquivo.getId(), solicitacao.getId(), anexo.getIdEmbarcacao()));
        });
    }

    private byte[] getBytesFromBase64(String base64) {
        String prefix = "data:application/pdf;base64,";
        if (base64.startsWith(prefix)) {
            base64 = base64.substring(prefix.length());
        }
        return Base64.getDecoder().decode(base64);
    }

    public byte[] downloadAnexo(String uuid, String nome) {
        SolicitarHabilitacao solicitarHabilitacao = findByUuid(UUID.fromString(uuid));
        if (Strings.isNotBlank(nome) && Strings.isNotEmpty(nome)) {
            Optional<Arquivo> arquivo = arquivoRepository.findByIdSolicitacaoNome(solicitarHabilitacao.getId(), nome);
            if (arquivo.isPresent()) {
                return readFile(arquivo.get().getCaminho());
            }
        }
        return null;
    }

    private byte[] readFile(String caminho) {
        try {
            Path path = Paths.get(caminho);
            Optional<StorageObject> file = storageService.read(path);
            if (file.isPresent()) {
                return file.get().toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn(e.getMessage());
        }
        return null;
    }

    private SolicitarHabilitacao findByUuid(UUID uuid) {
        Optional<SolicitarHabilitacao> solicitacaoExist = solicitarHabilitacaoRepository.findByUuid(uuid);
        if (!solicitacaoExist.isPresent()) {
            LOGGER.warn("Solicitação não encontrada. UUID: " + uuid);
            throw new BadRequestException("Solicitação não encontrada. UUID: "  + uuid);
        }
        return solicitacaoExist.get();
    }

}
