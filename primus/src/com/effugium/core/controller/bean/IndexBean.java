package com.effugium.core.controller.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.effugium.core.controller.EffugiumRequestFilter;
import com.effugium.core.controller.MsgViewUtil;
import com.effugium.core.model.entities.UsuarioEn;
import com.effugium.core.util.EffugiumHttpSession;
import com.effugium.core.util.Erb;

@Named
@ViewScoped
public class IndexBean implements Serializable {

    private static final long serialVersionUID = 4965749825241560918L;

    private String emailUsuario;
    private String senhaUsuario;

    /**
     * Executa toda vez que a view é chamada. (preRenderView)
     */
    public void init() {
	boolean ocorreuProblema = false;

	EffugiumHttpSession httpSession = new EffugiumHttpSession();

	String filterMsgErro = httpSession.obterStringDaSessao(EffugiumRequestFilter.FILTER_MSG_ERRO_SESSION_KEY);
	Object filterMsg = httpSession.obterObjetoDaSessao(EffugiumRequestFilter.FILTER_MSG_SESSION_KEY);

	if (filterMsgErro != null) {
	    ocorreuProblema = true;
	    MsgViewUtil.showErro(filterMsgErro, null);
	}
	if (filterMsg != null) {
	    ocorreuProblema = true;
	    if (filterMsg.equals(EffugiumRequestFilter.FilterMsg.LOGIN_NECESSARIO)) {
		MsgViewUtil.showInfo(Erb.getStr("msgLoginNecessario"));
	    } else if (filterMsg.equals(EffugiumRequestFilter.FilterMsg.USUARIO_INVALIDO)) {
		MsgViewUtil.showAviso(Erb.getStr("msgUsuarioInvalido"));
	    } else {
		MsgViewUtil.showErro("mensagem do filter desconhecida", null);
	    }
	}

	if (ocorreuProblema) {
	    httpSession.removerDaSessao(EffugiumHttpSession.USUARIO_SESSION_KEY);
	    httpSession.removerDaSessao(EffugiumRequestFilter.FILTER_MSG_ERRO_SESSION_KEY);
	    httpSession.removerDaSessao(EffugiumRequestFilter.FILTER_MSG_SESSION_KEY);
	}
    }

    public String login() {
	try {
	    UsuarioEn usuario = new UsuarioEn();
	    usuario.setEmail(emailUsuario);
	    usuario.setSenha(senhaUsuario);
	    new EffugiumHttpSession().salvarUsuarioNaSessao(usuario);
	    return "/secure/principal.xhtml?faces-redirect=true";
	} catch (Exception e) {
	    MsgViewUtil.showErro(e.getMessage(), e);
	    return null;
	}
    }

    public String logout() {
	new EffugiumHttpSession().limparSessao();
	return "logout";
    }

    public String getEmailUsuario() {
	return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
	this.emailUsuario = emailUsuario;
    }

    public String getSenhaUsuario() {
	return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
	this.senhaUsuario = senhaUsuario;
    }

}
