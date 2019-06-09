package com.effugium.core.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.effugium.core.model.EffugiumDao;
import com.effugium.core.model.entities.GrupoUsuarioEn;

public abstract class GrupoUsuarioDao extends EffugiumDao<GrupoUsuarioEn> {
    public enum Join {
	AUDITORIA
    };

    protected GrupoUsuarioDao() {
	super(GrupoUsuarioEn.class);
    }

    @Override
    protected Map<String, String> getCustomFilters() {
	Map<String, String> filter = new HashMap<String, String>();
	return filter;
    }

    @Override
    protected String getJoins(List<Enum<?>> joins) {
	String queryJoins = "";
	if (joins.contains(Join.AUDITORIA)) {
	    queryJoins += getAuditoriaJoins();
	}
	return queryJoins;
    }

    @Override
    public List<Enum<?>> getViewUpdateJoin() {
	List<Enum<?>> joins = new ArrayList<Enum<?>>();
	joins.add(Join.AUDITORIA);
	return joins;
    }

    @Override
    public List<Enum<?>> getSearchJoin() {
	List<Enum<?>> joins = new ArrayList<Enum<?>>();
	joins.add(Join.AUDITORIA);
	return joins;
    }

}
