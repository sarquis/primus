package com.effugium.core.controller;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class LocaleManagerBean implements Serializable {

    private static final long serialVersionUID = -8768662836567361963L;

    private Locale locale;

    private String localeSelectOneMenuSelected;

    private static Map<String, Locale> idiomas;

    static {
	idiomas = new LinkedHashMap<String, Locale>();
	idiomas.put("Português-BR", new Locale("pt", "BR"));
	idiomas.put("English", Locale.ENGLISH);
    }

    @PostConstruct
    public void init() {
	locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    public Locale getLocale() {
	return locale;
    }

    public void setLanguage(String language) {
	locale = new Locale(language);
	FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public void alterarIdioma(ValueChangeEvent e) {
	String newLocaleValue = e.getNewValue().toString();
	for (Map.Entry<String, Locale> entry : idiomas.entrySet()) {
	    if (entry.getValue().toString().equals(newLocaleValue)) {
		locale = entry.getValue();
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	    }
	}
    }

    public Map<String, Locale> getIdiomas() {
	return idiomas;
    }

    public String getLocaleSelectOneMenuSelected() {
	if (localeSelectOneMenuSelected == null) {
	    for (Map.Entry<String, Locale> entry : idiomas.entrySet()) {
		if (entry.getValue().toString().equals(locale.toString())) {
		    localeSelectOneMenuSelected = entry.getKey();
		    break;
		}
	    }
	}
	return localeSelectOneMenuSelected;
    }

    public void setLocaleSelectOneMenuSelected(String localeSelectOneMenuSelected) {
	this.localeSelectOneMenuSelected = localeSelectOneMenuSelected;
    }

}