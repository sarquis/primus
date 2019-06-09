package com.effugium.core.controller.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.effugium.core.controller.EffugiumCrudBean;
import com.effugium.core.controller.MsgViewUtil;
import com.effugium.core.model.entities.AcessoGrupoUsuarioEn;
import com.effugium.core.model.entities.GrupoUsuarioEn;
import com.effugium.core.model.service.GrupoUsuarioService;

@Named
@ViewScoped
public class GrupoUsuariosBean extends EffugiumCrudBean<GrupoUsuarioEn, GrupoUsuarioService> implements Serializable {

    private static final long serialVersionUID = 2015246930527847264L;
    private Long[] acessosSelecionados;

    public GrupoUsuariosBean() {
	super(GrupoUsuariosBean.class, new GrupoUsuarioEn(), GrupoUsuarioService.getInstance());
    }

    @Override
    protected void preAlterar() {
	preVisualizar();
    }

    protected void afterSaveOrUpdade() {
	try {
	    // GrupoUsuariosService.getInstance().salvarAcessos(getEntity(),
	    // acessosSelecionados);
	} catch (Exception e) {
	    MsgViewUtil.showErro(e.getMessage(), e);
	}
    }

    public Long[] getAcessosSelecionados() {
	return acessosSelecionados;
    }

    public void setAcessosSelecionados(Long[] acessosSelecionados) {
	this.acessosSelecionados = acessosSelecionados;
    }

    @Override
    protected void preVisualizar() {
	List<Long> acessos = new ArrayList<Long>();
	for (AcessoGrupoUsuarioEn acessoGrupoUsuarioEn : getEntity().getAcessoGrupoUsuarioEns()) {
	    acessos.add(acessoGrupoUsuarioEn.getCodigo());
	}
	acessosSelecionados = new Long[acessos.size()];
	acessosSelecionados = acessos.toArray(acessosSelecionados);
    }

    @Override
    protected void preCadastrar() {
	acessosSelecionados = null;
    }

}
