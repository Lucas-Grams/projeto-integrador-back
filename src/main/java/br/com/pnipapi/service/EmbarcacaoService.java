package br.com.pnipapi.service;

import br.com.pnipapi.dto.EmbarcacaoDTO;
import br.com.pnipapi.dto.EmbarcacaoTRDTO;
import br.com.pnipapi.dto.EmbarcacaoTRFormDTO;
import br.com.pnipapi.exception.BadRequestException;
import br.com.pnipapi.exception.ConflictException;
import br.com.pnipapi.model.*;
import br.com.pnipapi.repository.ArquivoRepository;
import br.com.pnipapi.repository.EmbarcacaoRepository;
import br.com.pnipapi.repository.EmbarcacaoTRRepository;
import br.com.pnipapi.repository.UsuarioRepository;
import br.com.pnipapi.service.storage.StorageObject;
import br.com.pnipapi.service.storage.StorageService;
import br.com.pnipapi.utils.Base64Util;
import br.com.pnipapi.utils.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmbarcacaoService {

    EmbarcacaoRepository embarcacaoRepository;
    EmbarcacaoTRRepository embarcacaoTRRepository;
    UsuarioRepository usuarioRepository;
    ArquivoRepository arquivoRepository;
    StorageService storageService;

    public EmbarcacaoService(EmbarcacaoRepository embarcacaoRepository, EmbarcacaoTRRepository embarcacaoTRRepository,
                             UsuarioRepository usuarioRepository, ArquivoRepository arquivoRepository,
                             StorageService storageService) {
        this.embarcacaoRepository = embarcacaoRepository;
        this.embarcacaoTRRepository = embarcacaoTRRepository;
        this.usuarioRepository = usuarioRepository;
        this.arquivoRepository = arquivoRepository;
        this.storageService = storageService;
    }

    public List<EmbarcacaoDTO> findAllEmbarcacaoByRgpTieNome(String filter) {
        List<Embarcacao> embarcacoes = embarcacaoRepository.findAllEmbarcacaoByRgpTieNome(filter);

        return embarcacoes.stream().map(embarcacao -> {
            String frota = embarcacaoRepository.findFrotaByIdEmbarcacao(embarcacao.getId());

            return new EmbarcacaoDTO(embarcacao.getId(), embarcacao.getNome(), embarcacao.getNumMarinhaTie(),
                embarcacao.getNumMarinha(), embarcacao.getNumRgp(), embarcacao.getUf(), embarcacao.getPais(),
                embarcacao.getAnoConstrucao(), embarcacao.getHp(), embarcacao.getComprimento(),
                embarcacao.getPetrecho(), embarcacao.getCodigoIn(), frota, null);
        }).toList();

    }

    @Transactional
    public String cadastrarEmbarcacao(List<EmbarcacaoTRFormDTO> embarcacoesDTO) {
        validarEmbarcacoesTR(embarcacoesDTO);
        embarcacoesDTO.forEach(dto -> {
            UUID uuid = UUID.randomUUID();
            Arquivo arquivoDeclaracaoProprietario = uploadDeclaracaoProprietario(uuid.toString(),
                dto.declaracaoProprietario(), dto.declaracaoProprietarioBase64());
            EmbarcacaoTR embarcacaoTR = EmbarcacaoTR.builder()
                .uuid(uuid)
                .embarcacao(embarcacaoRepository.getById(dto.idEmbarcacao()))
                .usuario(usuarioRepository.getById(User.getIdCurrentUser()))
                .nomeProprietario(dto.nomeProprietario())
                .cpfProprietario(dto.cpfProprietario())
                .emailProprietario(dto.emailProprietario())
                .declaracaoProprietario(arquivoDeclaracaoProprietario)
                .mercadoAtuacao(dto.mercadoAtuacao())
                .tempoMedioPesca(dto.tempoMedioPesca())
                .tipoConservacao(dto.tipoConservacao())
                .capacidadeTotal(dto.capacidadeTotal())
                .capacidadePescado(dto.capacidadePescado())
                .ativo(true)
                .build();
            embarcacaoTRRepository.save(embarcacaoTR);
        });
        return "Salvo com sucesso.";
    }

    public List<EmbarcacaoTRDTO> minhasEmbarcacoes() {
        Long idUsuario = User.getIdCurrentUser();
        List<Object[]> results = embarcacaoTRRepository.minhasEmbarcacoes(idUsuario);
        if (CollectionUtils.isEmpty(results)) return null;
        return results.stream().map(result ->
            EmbarcacaoTRDTO.builder()
                .id(Long.parseLong(result[0].toString()))
                .uuid(result[1].toString())
                .ativo(Boolean.parseBoolean(result[2].toString()))
                .dataCriacao(result[3].toString())
                .embarcacaoDTO(EmbarcacaoDTO.builder()
                    .id(Long.parseLong(result[7].toString()))
                    .numRgp(result[4].toString())
                    .nome(result[5].toString())
                    .frota(result[6].toString())
                    .tipoCertificacao(embarcacaoRepository.findCertificadoByIdEmbarcacao(Long.parseLong(result[7].toString())))
                    .build())
                .build()
        ).collect(Collectors.toList());
    }

    public EmbarcacaoTRDTO detalhesEmbarcacao(String uuid) {
        EmbarcacaoTR e = embarcacaoTRRepository.findByUuid(UUID.fromString(uuid))
            .orElseThrow(() -> new BadRequestException("Embarcação não encontrada."));
        return EmbarcacaoTRDTO.builder()
            .id(e.getId())
            .uuid(e.getUuid().toString())
            .embarcacaoDTO(EmbarcacaoDTO.builder()
                .numRgp(e.getEmbarcacao().getNumRgp())
                .nome(e.getEmbarcacao().getNome())
                .frota(embarcacaoRepository.findFrotaByIdEmbarcacao(e.getEmbarcacao().getId()))
                .build())
            .nomeProprietario(e.getNomeProprietario())
            .cpfProprietario(e.getCpfProprietario())
            .emailProprietario(e.getEmailProprietario())
            .mercadoAtuacao(e.getMercadoAtuacao())
            .tempoMedioPesca(e.getTempoMedioPesca())
            .tipoConservacao(e.getTipoConservacao())
            .capacidadeTotal(e.getCapacidadeTotal())
            .capacidadePescado(e.getCapacidadePescado())
            .build();
    }

    public byte[] downloadDeclaracaoProprietario(String uuid) {
        EmbarcacaoTR embarcacaoTR = embarcacaoTRRepository.findByUuid(UUID.fromString(uuid))
            .orElseThrow(() -> new BadRequestException("Embarcação não encontrada."));

        Arquivo arquivo = arquivoRepository.findById(embarcacaoTR.getDeclaracaoProprietario().getId())
            .orElseThrow(() -> new BadRequestException("Arquivo não encontrada."));

        Path path = Paths.get(arquivo.getCaminho());
        StorageObject file = storageService.read(path)
            .orElseThrow(() -> new BadRequestException("Declaração do proprietario não encontrada."));
        return file.toByteArray();
    }

    private Arquivo uploadDeclaracaoProprietario(String uuid, String nomeOriginalArquivo, String base64) {
        String nomeArquivo = "declaracao.proprietario.pdf";
        String filepath = String.format("embarcacao/%s/anexo/%s", uuid, nomeArquivo);
        Path path = Paths.get(filepath);
        storageService.save(path, Base64Util.getBytesFromBase64(base64));

        Arquivo arquivo = new Arquivo();
        arquivo.setNome(nomeArquivo);
        arquivo.setOrigemArquivo(nomeOriginalArquivo);
        arquivo.setCaminho(filepath);
        arquivo.setTipo("application/pdf");
        return arquivoRepository.saveAndFlush(arquivo);
    }

    private void validarEmbarcacoesTR(List<EmbarcacaoTRFormDTO> embarcacoesDTO) throws ConflictException {
        embarcacoesDTO.forEach(e -> {
            Long idEmbarcacao = e.idEmbarcacao();
            Optional<EmbarcacaoTR> embarcacaoTR = embarcacaoTRRepository.findByIdEmbarcacao(idEmbarcacao);
            if (embarcacaoTR.isPresent()) {
                Embarcacao embarcacao = embarcacaoRepository.getById(idEmbarcacao);
                throw new ConflictException(String.format("Embarcação %s vínculada em outra solicitação.", embarcacao.getNome()));
            }
        });
    }

}
