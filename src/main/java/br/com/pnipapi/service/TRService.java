package br.com.pnipapi.service;

import br.com.pnipapi.dto.*;
import br.com.pnipapi.dto.documentosAPI.*;
import br.com.pnipapi.exception.BadRequestException;
import br.com.pnipapi.exception.NotFoundException;
import br.com.pnipapi.model.*;
import br.com.pnipapi.repository.*;
import br.com.pnipapi.service.storage.StorageObject;
import br.com.pnipapi.service.storage.StorageService;
import br.com.pnipapi.utils.Base64Util;
import br.com.pnipapi.utils.User;
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

import java.nio.file.AccessDeniedException;
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

    EnderecoRepository enderecoRepository;

    UsuarioRepository usuarioRepository;

    PermissaoRepository permissaoRepository;

    UsuarioService usuarioService;

    ArquivoRepository arquivoRepository;

    EmbarcacaoRepository embarcacaoRepository;

    EmbarcacaoTRRepository embarcacaoTRRepository;

    ArquivoSolicitarHabilitacaoRepository arquivoSolicitarHabilitacaoRepository;
    SolicitarHabilitacaoRepository solicitarHabilitacaoRepository;
    StatusSolicitarHabilitacaoRepository statusSolicitarHabilitacaoRepository;

    Long ID_USUARIO;

    public Long getID_USUARIO() {
        if (User.getIdCurrentUser() != null) {
            return User.getIdCurrentUser();
        }
        return ID_USUARIO;
    }

    public void setID_USUARIO(Long ID_USUARIO) {
        this.ID_USUARIO = ID_USUARIO;
    }

    public TRService(ApiDocumentosRest apiDocumentosRest, SolicitarHabilitacaoRepository solicitarHabilitacaoRepository,
        StatusRepository statusRepository, StatusSolicitarHabilitacaoRepository statusSolicitarHabilitacaoRepository,
        StorageService storageService, ArquivoSolicitarHabilitacaoRepository arquivoSolicitarHabilitacaoRepository,
        ArquivoRepository arquivoRepository, UsuarioRepository usuarioRepository, UsuarioService usuarioService,
        PermissaoRepository permissaoRepository, EnderecoRepository enderecoRepository,
        EmbarcacaoRepository embarcacaoRepository,
        EmbarcacaoTRRepository embarcacaoTRRepository) {
        this.statusRepository = statusRepository;
        this.apiDocumentosRest = apiDocumentosRest;
        this.solicitarHabilitacaoRepository = solicitarHabilitacaoRepository;
        this.statusSolicitarHabilitacaoRepository = statusSolicitarHabilitacaoRepository;
        this.storageService = storageService;
        this.arquivoSolicitarHabilitacaoRepository = arquivoSolicitarHabilitacaoRepository;
        this.arquivoRepository = arquivoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.permissaoRepository = permissaoRepository;
        this.enderecoRepository = enderecoRepository;
        this.embarcacaoRepository = embarcacaoRepository;
        this.embarcacaoTRRepository = embarcacaoTRRepository;
    }

    public List<SolicitarHabilitacao> findAll() {
        return solicitarHabilitacaoRepository.findAll();
    }

    public SolicitarHabilitacao findSolicitacaoByUuid(UUID uuid) {
        return solicitarHabilitacaoRepository.findByUuid(uuid).get();
    }

    public List<SolicitarHabilitacao> findSolicitacoesByStatus(List<String> status) {
        return solicitarHabilitacaoRepository.findSolicitacoesByStatus(status);
    }

    @Transactional
    public String solicitarHabilitacao(HabilitarTRDTO habilitarTRDTO) {

        Long idUsuario = criarUsuarioConvidado(habilitarTRDTO);
        this.setID_USUARIO(idUsuario);

        try {
            validarSolicitacaoHabilitacao();
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        List<ArquivoAnexoSolicitacaoDTO> anexos = extrairAnexosDaSolicitacao(habilitarTRDTO);

        // TODO DESCOMENTAR QUANDO O GERAR TOKEN E SALVAR NOVO USUARIO NO KEYCLOAK ESTIVER OK
        //ProcessoDTO solicitacao = apiDocumentosRest.criarSolicitacaoHabilitacaoTR();
        ProcessoDTO solicitacao = new ProcessoDTO();
        solicitacao.setCriadoEm(new Timestamp(System.currentTimeMillis()));
        solicitacao.setUuid(UUID.randomUUID().toString());

        if (Objects.isNull(solicitacao)) {
            throw new BadRequestException("Falha ao criar a solicitação - API DOCUMENTOS");
        }

        // TODO DESCOMENTAR QUANDO O GERAR TOKEN E SALVAR NOVO USUARIO NO KEYCLOAK ESTIVER OK
        //DespachoDTO despachoDTO = apiDocumentosRest.salvarDespacho(buildDespacho(habilitarTRDTO),UUID.fromString(solicitacao.getUuid()));
        DespachoDTO despachoDTO = buildDespacho(habilitarTRDTO);

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
        solicitarHabilitacao.setIdUsuario(this.getID_USUARIO());
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
    }

    private Long criarUsuarioConvidado(HabilitarTRDTO habilitarTRDTO) {
        if (habilitarTRDTO != null && Strings.isNotEmpty(habilitarTRDTO.getCpf()) && Strings.isNotBlank(habilitarTRDTO.getCpf())) {
            Optional<Usuario> usuario = usuarioRepository.findUsuarioByCpf(habilitarTRDTO.getCpf());
            if (!usuario.isPresent()) {
                Endereco endereco = new Endereco(habilitarTRDTO.getLogradouro(), habilitarTRDTO.getCep(),
                    habilitarTRDTO.getNumero(), habilitarTRDTO.getComplemento(), habilitarTRDTO.getMunicipio(),
                    habilitarTRDTO.getUf());

                endereco = enderecoRepository.saveAndFlush(endereco);

                Usuario newUsuario = new Usuario();
                newUsuario.setSenha(User.generatePasswordBCrypt("123456"));
                newUsuario.setCpf(habilitarTRDTO.getCpf().replaceAll("[^0-9]", "")); // remove (./-)
                newUsuario.setNome(habilitarTRDTO.getNome());
                newUsuario.setEmail(habilitarTRDTO.getEmail());
                newUsuario.setAtivo(Boolean.TRUE);
                newUsuario.setEndereco(endereco);
                newUsuario.setPermissoes(new ArrayList<>(List.of(permissaoRepository.findPermissaoByName("convidado"))));

                newUsuario = usuarioRepository.saveAndFlush(newUsuario);
                return newUsuario.getId();
            }
        }
        return null;
    }

    private void validarSolicitacaoHabilitacao() throws AccessDeniedException {
        String status = findStatusUltimaSolicatacao();
        if (StringUtils.hasText(status) && "EM_ANALISE".equals(status) || "DEFERIDA".equals(status)) {
            throw new AccessDeniedException("Status não permitido para solicitar habilitação.");
        }
    }

    public List<SolicitacaoHabilitacaoDTO> minhasSolicitacoes() {
        Long idUsuario = this.getID_USUARIO();
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
        Long idUsuario = this.getID_USUARIO();
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
        return anexos;
    }

    private DespachoDTO buildDespacho(HabilitarTRDTO habilitarTRDTO) {
        DespachoDTO despachoDTO = new DespachoDTO();
        despachoDTO.setUsuarioAcao(this.getID_USUARIO().intValue());
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
        SolicitarHabilitacao solicitacao = findByUuid(finalizarSolicitacaoDTO.getUuidSolicitacao());

        String statusRequest = "deferir".equals(finalizarSolicitacaoDTO.getStatusSolicitacao())?
            StatusSolicitacao.DEFERIDA.name(): StatusSolicitacao.INDEFERIDA.name();

        solicitacao.setStatus(statusRepository.findByDescricao(statusRequest).get().getDescricao());
        solicitacao.setObservacao(finalizarSolicitacaoDTO.getMsgSolicitacao());

        solicitarHabilitacaoRepository.saveAndFlush(solicitacao);

        // criar data de vinculação e desvinculação
        Usuario usuario = usuarioRepository.getById(solicitacao.getIdUsuario());
        List<Object[]> result = embarcacaoRepository.findEmbarcacoesByCpfTR(usuario.getCpf());
        if (!CollectionUtils.isEmpty(result) && statusRequest.equals(StatusSolicitacao.DEFERIDA.name())) {
            result.forEach(object -> {
                UUID uuid = UUID.randomUUID();
                EmbarcacaoTR embarcacaoTR = EmbarcacaoTR.builder()
                    .uuid(uuid)
                    .embarcacao(embarcacaoRepository.getById(Long.parseLong(object[0].toString())))
                    .usuario(usuario)
                    .ativo(true)
                    .build();
                embarcacaoTRRepository.saveAndFlush(embarcacaoTR);
            });
        }
        return "Ação realizada com sucesso";
    }

    private void uploadAnexoSolicitacao(SolicitarHabilitacao solicitacao, List<ArquivoAnexoSolicitacaoDTO> anexos) {
        anexos.forEach(anexo -> {
            String filepath = String.format("solicitacao/%s/anexo/%s", solicitacao.getUuidSolicitacao().toString(), anexo.getNomeUpload());
            Path path = Paths.get(filepath);
            storageService.save(path, Base64Util.getBytesFromBase64(anexo.getBase64()));

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

    public String findStatusUltimaSolicatacao() {
        Long idUsuario = this.getID_USUARIO();
        Optional<Object> status = solicitarHabilitacaoRepository.findStatusByLastSolicitacao(idUsuario);
        if (status.isPresent() && StringUtils.hasText(status.get().toString())) {
            return status.get().toString();
        }
        return null;
    }

    public String desvincularEmbarcacao(String uuidVinculoEmbarcacao) {
        Optional<EmbarcacaoTR> embarcacaoTR = embarcacaoTRRepository.findByUuid(UUID.fromString(uuidVinculoEmbarcacao));
        if (!embarcacaoTR.isPresent()) {
            throw new NotFoundException("Vínculo não encontrado. Vínculo UUID: "  + embarcacaoTR.get().getEmbarcacao().getId());
        }
        embarcacaoTR.get().setAtivo(Boolean.FALSE);
        embarcacaoTRRepository.saveAndFlush(embarcacaoTR.get());

        return "Operação realizada com sucesso";
    }

}
