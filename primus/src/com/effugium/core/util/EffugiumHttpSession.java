package com.effugium.core.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.effugium.core.model.entities.UsuarioEn;

public class EffugiumHttpSession {

    public static final String USUARIO_SESSION_KEY = "sessionUserObject";

    private HttpSession getSession() {
	return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
    }

    public void salvarUsuarioNaSessao(UsuarioEn UsuarioEn) {
	getSession().setAttribute(USUARIO_SESSION_KEY, UsuarioEn);
    }

    public UsuarioEn obterUsuarioDaSessao() {
	return (UsuarioEn) getSession().getAttribute(USUARIO_SESSION_KEY);
    }

    public String obterStringDaSessao(String sessionKey) {
	Object msg = obterObjetoDaSessao(sessionKey);
	return (msg == null ? null : (String) msg);
    }

    public Object obterObjetoDaSessao(String sessionKey) {
	return getSession().getAttribute(sessionKey);
    }

    public void removerDaSessao(String sessionKey) {
	getSession().removeAttribute(sessionKey);
    }

    public void limparSessao() {
	getSession().invalidate();
    }
}
