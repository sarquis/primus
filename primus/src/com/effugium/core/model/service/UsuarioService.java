package com.effugium.core.model.service;

import com.effugium.core.model.EffugiumService;
import com.effugium.core.model.dao.UsuarioDao;

public class UsuarioService extends UsuarioDao implements EffugiumService {

    private static UsuarioService uniqueInstance;

    private UsuarioService() {
    }

    public static UsuarioService getInstance() {
	if (uniqueInstance == null)
	    if (isNullInstanceSync())
		uniqueInstance = new UsuarioService();
	return uniqueInstance;
    }

    private static synchronized boolean isNullInstanceSync() {
	return (uniqueInstance == null);
    }

}
