package com.effugium.core.controller.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.effugium.core.controller.EffugiumCrudBean;
import com.effugium.core.controller.MsgViewUtil;
import com.effugium.core.model.entities.GrupoUsuarioEn;
import com.effugium.core.model.entities.UsuarioEn;
import com.effugium.core.model.service.GrupoUsuarioService;
import com.effugium.core.model.service.UsuarioService;

@Named
@ViewScoped
public class UsuarioBean extends EffugiumCrudBean<UsuarioEn, UsuarioService> implements Serializable {

    private static final long serialVersionUID = 3569812773474817089L;
    private String filtroGrupoUsuariosEtId;

    public UsuarioBean() {
	super(UsuarioBean.class, new UsuarioEn(), UsuarioService.getInstance());
    }

    public List<GrupoUsuarioEn> getGrupoUsuariosList() {
	try {
	    return GrupoUsuarioService.getInstance().list(new GrupoUsuarioEn());
	} catch (Exception e) {
	    MsgViewUtil.showErro(e.getMessage(), e);
	}
	return null;
    }

    @Override
    protected void preAlterar() {
	if (getEntity().getGrupoUsuarioEn() == null) {
	    getEntity().setGrupoUsuarioEn(new GrupoUsuarioEn());
	}
    }

    @Override
    protected void preSalvarOuSalvarAlteracoes() {
	if (getEntity().getGrupoUsuarioEn().getId() == null) {
	    getEntity().setGrupoUsuarioEn(null);
	}
    }

    public String getFiltroGrupoUsuariosEtId() {
	return filtroGrupoUsuariosEtId;
    }

    public void setFiltroGrupoUsuariosEtId(String filtroGrupoUsuariosEtId) {
	this.filtroGrupoUsuariosEtId = filtroGrupoUsuariosEtId;
    }

}
