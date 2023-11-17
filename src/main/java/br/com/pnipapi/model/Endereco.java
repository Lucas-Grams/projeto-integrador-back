package br.com.pnipapi.model;

import lombok.Data;

@Data
public class Endereco {

    private int id;
    private String uf;
    private String rua;
    private String cep;
    private String pais;
    private String bairro;
    private String cidade;
    private String numero;
    private String latitude;
    private String longitude;
    private String complemento;

}
