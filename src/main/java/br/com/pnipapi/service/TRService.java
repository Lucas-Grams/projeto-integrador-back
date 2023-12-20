package br.com.pnipapi.service;

import br.com.pnipapi.dto.FinalizarSolicitacaoDTO;
import br.com.pnipapi.dto.HabilitarTRDTO;
import br.com.pnipapi.dto.SolicitacaoHabilitacaoDTO;
import br.com.pnipapi.dto.SolicitarHabilitacaoDTO;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
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
        EmbarcacaoRepository embarcacaoRepository, EmbarcacaoSolicitarHabilitacaoRepository embarcacaoSolicitarHabilitacaoRepository) {
        this.statusRepository = statusRepository;
        this.apiDocumentosRest = apiDocumentosRest;
        this.embarcacaoRepository = embarcacaoRepository;
        this.solicitarHabilitacaoRepository = solicitarHabilitacaoRepository;
        this.statusSolicitarHabilitacaoRepository = statusSolicitarHabilitacaoRepository;
        this.embarcacaoSolicitarHabilitacaoRepository = embarcacaoSolicitarHabilitacaoRepository;
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
            solicitarHabilitacao.setSolicitante(habilitarTRDTO.nome());

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

    // upload certificado e diploma do TR
    public String uploadAnexosSolicitacao(String uuid, List<Long> idsEmbarcacao, List<MultipartFile> file) {
        SolicitarHabilitacao solicitarHabilitacao = findByUuid(UUID.fromString(uuid));

        for (MultipartFile multipartFile : file) {
            String fileName = multipartFile.getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID() + fileExtension;
            String path = "solicitacoes/" + uuid + "/anexos/" + newFileName;

            Path pathToSave = Paths.get(path);

            try {
                storageService.save(pathToSave, multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            Arquivo arquivo = new Arquivo();
            arquivo.setNome(fileName);
            arquivo.setCaminho(path);
            arquivo.setTamanho(multipartFile.getSize());
            arquivo.setTipo(multipartFile.getContentType());

            arquivo = arquivoRepository.saveAndFlush(arquivo);

            if (CollectionUtils.isEmpty(idsEmbarcacao)) {
                arquivoSolicitarHabilitacaoRepository.saveAndFlush(new ArquivoSolicitarHabilitacao(arquivo.getId(), solicitarHabilitacao.getId()));
            } else {
                // arquivos de embarcaçao - pegand pelo nome embarcacao_ID_1
                Arquivo finalArquivo = arquivo;
                idsEmbarcacao.forEach(id -> {
                    if (finalArquivo.getNome().contains("ID_" + id)) {
                        arquivoSolicitarHabilitacaoRepository.saveAndFlush(new ArquivoSolicitarHabilitacao(finalArquivo.getId(), solicitarHabilitacao.getId(), id));
                    }
                });
            }
        }
        return "Arquivos salvos";
    }

    public StreamingResponseBody downloadAnexo(String uuid, String nome, Long idEmbarcacao) {
        SolicitarHabilitacao solicitarHabilitacao = findByUuid(UUID.fromString(uuid));

        if (Strings.isNotBlank(nome) && Strings.isNotEmpty(nome)) {
            Optional<Arquivo> arquivo = arquivoRepository.findByIdSolicitacaoNome(solicitarHabilitacao.getId(), nome);
            if (arquivo.isPresent()) {
                return readFile(arquivo.get().getCaminho());
            }
        }

        if (Objects.nonNull(idEmbarcacao)) {
            Optional<Arquivo> arquivo = arquivoRepository.findByIdSolicitacaoIdEmbarcacao(solicitarHabilitacao.getId(), idEmbarcacao);
            if (arquivo.isPresent()) {
                return readFile(arquivo.get().getCaminho());
            }
        }

        return null;
    }

    private StreamingResponseBody readFile(String caminho) {
        try {
            Path path = Paths.get(caminho);
            Optional<StorageObject> file = storageService.read(path);
            if (file.isPresent()) {
                return file.<StreamingResponseBody>map(storageObject -> outputStream -> {
                    outputStream.write(storageObject.toByteArray());
                }).orElse(null);
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
