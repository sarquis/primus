package com.effugium.core.model.service;

import com.effugium.core.model.EffugiumService;
import com.effugium.core.model.dao.GrupoUsuarioDao;

public class GrupoUsuarioService extends GrupoUsuarioDao implements EffugiumService {

    private static GrupoUsuarioService uniqueInstance;

    private GrupoUsuarioService() {
    }

    public static GrupoUsuarioService getInstance() {
	if (uniqueInstance == null)
	    if (isNullInstanceSync())
		uniqueInstance = new GrupoUsuarioService();
	return uniqueInstance;
    }

    private static synchronized boolean isNullInstanceSync() {
	return (uniqueInstance == null);
    }

}
