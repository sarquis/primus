package com.effugium.core.controller.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.effugium.core.controller.MsgViewUtil;
import com.effugium.core.util.EffugiumHttpSession;

@Named
@ViewScoped
public class PrincipalBean implements Serializable {

    private static final long serialVersionUID = 7473697717365295155L;

    public String getEmailUsuario() {
	return new EffugiumHttpSession().obterUsuarioDaSessao().getEmail();
    }

    public String getSenhaUsuario() {
	try {
	    return new EffugiumHttpSession().obterUsuarioDaSessao().getSenha();
	} catch (Exception e) {
	    MsgViewUtil.showErro(e.getMessage(), e);
	    return null;
	}
    }

}
