package com.effugium.core.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.effugium.core.model.entities.UsuarioEn;
import com.effugium.core.model.service.UsuarioService;
import com.effugium.core.util.EffugiumHttpSession;

public class EffugiumRequestFilter implements Filter {

    public static final String FILTER_MSG_SESSION_KEY = "filterMsg";
    public static final String FILTER_MSG_ERRO_SESSION_KEY = "filterMsgErro";

    FilterConfig filterConfig = null;

    public enum FilterMsg {
	LOGIN_NECESSARIO,
	USUARIO_INVALIDO
    };

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
	    throws IOException, ServletException {

	// Obtendo a sessão do request:
	HttpSession httpSession = ((HttpServletRequest) servletRequest).getSession();

	try {
	    // Obtendo obj usuário da sessão:
	    UsuarioEn usuario = (UsuarioEn) httpSession.getAttribute(EffugiumHttpSession.USUARIO_SESSION_KEY);

	    // Tratando o acesso:
	    if (usuario == null) {
		// Login é necessário:
		httpSession.setAttribute(FILTER_MSG_SESSION_KEY, FilterMsg.LOGIN_NECESSARIO);
		goToPaginaInicial(servletRequest, servletResponse);
		return;
	    }

	    usuario = UsuarioService.getInstance().uniqueResult(usuario);
	    if (usuario != null) {
		// Autenticado com sucesso.
		// 1. Atulizando usuário da sessão:
		httpSession.setAttribute(EffugiumHttpSession.USUARIO_SESSION_KEY, usuario);
		// 2. Prosseguindo com o request:
		filterChain.doFilter(servletRequest, servletResponse);
	    } else {
		// Usuário inválido.
		httpSession.setAttribute(FILTER_MSG_SESSION_KEY, FilterMsg.USUARIO_INVALIDO);
		goToPaginaInicial(servletRequest, servletResponse);
	    }
	} catch (Exception e) {
	    httpSession.setAttribute(FILTER_MSG_ERRO_SESSION_KEY, e.getMessage());
	    goToPaginaInicial(servletRequest, servletResponse);
	}
    }

    private void goToPaginaInicial(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
	((HttpServletResponse) servletResponse)
		.sendRedirect(((HttpServletRequest) servletRequest).getContextPath() + "/");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	this.filterConfig = filterConfig;
    }
}
