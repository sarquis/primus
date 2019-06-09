package com.effugium.core.enumeration;

import com.effugium.core.util.Erb;

public enum AcoesCrudEnum {
    CONSULTAR(1, Erb.getStr("consultar"), Erb.getStr("consulta")),
    CADASTRAR(2, Erb.getStr("cadastrar"), Erb.getStr("cadastro")),
    ALTERAR(3, Erb.getStr("alterar"), Erb.getStr("alteracao")),
    VISUALIZAR(4, Erb.getStr("visualizar"), Erb.getStr("visualizacao"));

    private final int valor;
    private final String descricao;
    private final String situacao;

    AcoesCrudEnum(int valor, String descricao, String situacao) {
	this.valor = valor;
	this.descricao = descricao;
	this.situacao = situacao;
    }

    public int getValor() {
	return valor;
    }

    public String getDescricao() {
	return descricao;
    }

    public String getSituacao() {
	return situacao;
    }
}