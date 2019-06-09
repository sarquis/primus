package com.effugium.core.model.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.effugium.core.model.Auditoria;
import com.effugium.core.model.EffugiumEntity;
import com.effugium.core.model.EntityFilter;

@Entity
@Table(name = "tb_database_update", schema = "core")
public class DatabaseUpdateEn implements EffugiumEntity {

    private static final long serialVersionUID = 3832321876022386222L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_database_update")
    @SequenceGenerator(name = "sq_database_update", sequenceName = "core.sq_database_update", allocationSize = 1, initialValue = 1)
    private Long id;

    /*
     * common:
     */

    @Override
    public Long getId() {
	return id;
    }

    @Override
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * Não utilizado, sem função.
     */
    @Override
    public Auditoria getAuditoria() {
	return null;
    }

    /**
     * Não utilizado, sem função.
     */
    @Override
    public void setAuditoria(Auditoria auditoria) {
    }

    /**
     * Não utilizado, sem função.
     */
    @Override
    public Set<EntityFilter> getFilter() {
	return null;
    }

    /**
     * Não utilizado, sem função.
     */
    @Override
    public void setFilter(Set<EntityFilter> filter) {
    }

    /*
     * intrinsic:
     */

    @Column(length = 10)
    private String arquivo;

    @Column
    private Long linha;

    @Column(length = 30)
    private String instancia;

    @Column
    private boolean executado;

    @Column(length = 4000)
    private String scriptSql;

    @Column(length = 300)
    private String atividade;

    @Column(name = "data_inclusao", nullable = false, updatable = false, columnDefinition = "timestamp default current_timestamp")
    private Date dataInclusao;

    /*
     * GET's e SET's:
     */

    public String getArquivo() {
	return arquivo;
    }

    public void setArquivo(String arquivo) {
	this.arquivo = arquivo;
    }

    public Long getLinha() {
	return linha;
    }

    public void setLinha(Long linha) {
	this.linha = linha;
    }

    public String getInstancia() {
	return instancia;
    }

    public void setInstancia(String instancia) {
	this.instancia = instancia;
    }

    public String getAtividade() {
	return atividade;
    }

    public void setAtividade(String atividade) {
	this.atividade = atividade;
    }

    public Date getDataInclusao() {
	return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
	this.dataInclusao = dataInclusao;
    }

    public String getScriptSql() {
	return scriptSql;
    }

    public void setScriptSql(String scriptSql) {
	this.scriptSql = scriptSql;
    }

    public boolean isExecutado() {
	return executado;
    }

    public void setExecutado(boolean executado) {
	this.executado = executado;
    }

}
