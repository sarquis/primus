package com.effugium.core.util;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * Effugium Resource Bundle
 * 
 * Busca as Strings do arquivo de properties.
 */
public class Erb {

    private static final ResourceBundle bundle = FacesContext.getCurrentInstance().getApplication()
	    .getResourceBundle(FacesContext.getCurrentInstance(), "msg");

    public static String getStr(String key) {
	return bundle.getString(key);
    }
}
