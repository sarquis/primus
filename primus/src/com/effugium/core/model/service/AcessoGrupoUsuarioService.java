package com.effugium.core.model.service;

import com.effugium.core.model.EffugiumService;
import com.effugium.core.model.dao.UsuarioDao;

public class AcessoGrupoUsuarioService extends UsuarioDao implements EffugiumService {

    private static AcessoGrupoUsuarioService uniqueInstance;

    private AcessoGrupoUsuarioService() {
    }

    public static AcessoGrupoUsuarioService getInstance() {
	if (uniqueInstance == null)
	    if (isNullInstanceSync())
		uniqueInstance = new AcessoGrupoUsuarioService();
	return uniqueInstance;
    }

    private static synchronized boolean isNullInstanceSync() {
	return (uniqueInstance == null);
    }

}
