package com.effugium.core.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.hibernate.exception.ConstraintViolationException;

import com.effugium.core.util.Erb;

public class MsgViewUtil {

    public static void showInfo(String mgs) {
	FacesContext.getCurrentInstance().addMessage(null,
		new FacesMessage(FacesMessage.SEVERITY_INFO, Erb.getStr("informacao"), mgs));
    }

    public static void showAviso(String mgs) {
	FacesContext.getCurrentInstance().addMessage(null,
		new FacesMessage(FacesMessage.SEVERITY_WARN, Erb.getStr("aviso"), mgs));
    }

    public static void showErro(String mgs, Exception e) {
	if (e != null) {
	    if (e instanceof ConstraintViolationException) {
		mgs += " " + ((ConstraintViolationException) e).getSQLException().getMessage();
	    }
	}
	FacesContext.getCurrentInstance().addMessage(null,
		new FacesMessage(FacesMessage.SEVERITY_ERROR, Erb.getStr("erro"), mgs));
    }

    public static void showProibido(String mgs) {
	FacesContext.getCurrentInstance().addMessage(null,
		new FacesMessage(FacesMessage.SEVERITY_FATAL, Erb.getStr("acessoNegado"), mgs));
    }
}
