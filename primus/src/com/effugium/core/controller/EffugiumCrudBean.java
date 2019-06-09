package com.effugium.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.List;

import javax.faces.event.ActionEvent;

import com.effugium.core.enumeration.AcoesCrudEnum;
import com.effugium.core.model.EffugiumDao;
import com.effugium.core.model.EffugiumEntity;
import com.effugium.core.util.Erb;

public abstract class EffugiumCrudBean<E extends EffugiumEntity, S extends EffugiumDao<E>> {
    private E entity;
    private S service;
    private List<E> resultSet;
    private AcoesCrudEnum acaoCrud;
    private String tituloPagina;

    public EffugiumCrudBean(Class<?> beanClass, E entity, S service) {
	this.entity = entity;
	this.service = service;
	this.acaoCrud = AcoesCrudEnum.CONSULTAR;
	this.tituloPagina = Erb.getStr("titulo" + beanClass.getSimpleName());
    }

    public String getTituloPagina() {
	return tituloPagina += " - " + acaoCrud.getSituacao();
    }

    public boolean getAcaoCrudConsultar() {
	return (acaoCrud == AcoesCrudEnum.CONSULTAR);
    }

    public boolean getAcaoCrudVisualizar() {
	return (acaoCrud == AcoesCrudEnum.VISUALIZAR);
    }

    public boolean getAcaoCrudCadastrar() {
	return (acaoCrud == AcoesCrudEnum.CADASTRAR);
    }

    public boolean getAcaoCrudAlterar() {
	return (acaoCrud == AcoesCrudEnum.ALTERAR);
    }

    public void voltarParaConsulta(ActionEvent event) {
	acaoCrud = AcoesCrudEnum.CONSULTAR;
    }

    /*
     * VISUALIZAR
     */

    protected void preVisualizar() {
	// For use of a custom code...
    }

    public void visualizar() {
	try {
	    preVisualizar();
	    acaoCrud = AcoesCrudEnum.VISUALIZAR;
	    entity = service.uniqueResultById(entity.getId(), service.getViewUpdateJoin());
	} catch (Exception e) {
	    MsgViewUtil.showErro(e.getMessage(), e);
	}
    }

    /*
     * ALTERAR
     */

    protected void preAlterar() {
	// For use of a custom code...
    }

    public void alterar() {
	try {
	    preAlterar();
	    acaoCrud = AcoesCrudEnum.ALTERAR;
	    entity = service.uniqueResultById(entity.getId(), service.getViewUpdateJoin());
	} catch (Exception e) {
	    MsgViewUtil.showErro(e.getMessage(), e);
	}
    }

    /*
     * CADASTRAR
     */

    protected void preCadastrar() {
	// For use of a custom code...
    }

    @SuppressWarnings("unchecked")
    public void cadastrar(ActionEvent event) {
	try {
	    preCadastrar();
	    acaoCrud = AcoesCrudEnum.CADASTRAR;
	    entity = ((E) entity.getClass().getDeclaredConstructor().newInstance());
	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		| NoSuchMethodException | SecurityException e) {
	    MsgViewUtil.showErro(e.getMessage(), e);
	}
    }

    /*
     * SALVAR E SALVAR ALTERACAO
     */

    protected void preSalvarOuSalvarAlteracoes() {
	// For use of a custom code...
    }

    public void salvar(ActionEvent event) {
	try {
	    preSalvarOuSalvarAlteracoes();
	    entity = service.saveOrUpdate(entity);
	    MsgViewUtil.showInfo(Erb.getStr("registroCadastroSucesso"));
	    visualizar();
	} catch (Exception e) {
	    MsgViewUtil.showErro(e.getMessage(), e);
	}
    }

    public void salvarAlteracoes(ActionEvent event) {
	try {
	    preSalvarOuSalvarAlteracoes();
	    entity = service.saveOrUpdate(entity);
	    MsgViewUtil.showInfo(Erb.getStr("registroAlteradoSucesso"));
	    visualizar();
	} catch (Exception e) {
	    MsgViewUtil.showErro(e.getMessage(), e);
	}
    }

    /*
     * CONSULTAR
     */

    protected void preConsultar() {
	// For use of a custom code...
    }

    public void consultar(ActionEvent event) {
	try {
	    preConsultar();
	    resultSet = service.list(entity, service.getSearchJoin());
	    MsgViewUtil.showInfo(MessageFormat.format(Erb.getStr("retornoConsulta"), resultSet.size()));
	} catch (Exception e) {
	    MsgViewUtil.showErro(e.getMessage(), e);
	}
    }

    /*
     * OUTROS
     */

    public E getEntity() {
	return entity;
    }

    public void setEntity(E entity) {
	this.entity = entity;
    }

    public List<E> getResultSet() {
	return resultSet;
    }

    public void setResultSet(List<E> resultSet) {
	this.resultSet = resultSet;
    }
}
