package com.effugium.core.util;

public class EffugiumExcepion extends Exception {

    private static final long serialVersionUID = -8351146600207828599L;

    public enum TipoException {
	VALIDACAO_DADOS,
	ERRO_IMPLEMENTACAO;
    }

    private TipoException tipoException;

    public EffugiumExcepion(String key, TipoException tipoException) {
	super(Erb.getStr(key));
	this.tipoException = tipoException;
    }

    public TipoException getTipoException() {
	return tipoException;
    }

}
