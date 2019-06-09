package com.effugium.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.effugium.core.model.entities.UsuarioEn;

/**
 * Classe utilitária para as entidades, prover campos de auditoria do registro.
 * 
 * OBS: Os atributos da classe se referem ao registro (linha, tubla) da tabela.
 * Ex: data da alteração do registro; data inclusão do registro, se o registro
 * está ativo; alteração do registro ativo (ou seja quando o usuário alterou a
 * linha para ativa = false, ou ativa = true);
 */
@Embeddable
public class Auditoria implements Serializable {

    private static final long serialVersionUID = -4204009141093903742L;

    /*
     * Campos referentes a inclusão do registro:
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_inclusao", nullable = false, updatable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_auditoria_usuario_inclusao"))
    private UsuarioEn usuarioInclusao;

    @Column(name = "data_inclusao", nullable = false, updatable = false, columnDefinition = "timestamp default current_timestamp")
    private Date dataInclusao;

    @Column(name = "registro_situacao_ativo", nullable = false, columnDefinition = "boolean default true")
    private Boolean registroAtivo;

    /*
     * Campos referentes a alteração do registro:
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_alteracao", insertable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_auditoria_usuario_alteracao"))
    private UsuarioEn usuarioAlteracao;

    @Column(name = "data_alteracao", insertable = false)
    private Date dataAlteracao;

    /*
     * Campos referentes a alteração da situação do registro ATIVO/INATIVO:
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_alteracao_registro_ativo", insertable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_auditoria_usuario_alteracao_registro_ativo"))
    private UsuarioEn usuarioAlteracaoSituacao;

    @Column(name = "data_alteracao_registro_ativo", insertable = false)
    private Date dataAlteracaoRegistroAtivo;

    /*
     * GET's e SET's:
     */

    public UsuarioEn getUsuarioInclusao() {
	return usuarioInclusao;
    }

    public void setUsuarioInclusao(UsuarioEn usuarioInclusao) {
	this.usuarioInclusao = usuarioInclusao;
    }

    public Date getDataInclusao() {
	return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
	this.dataInclusao = dataInclusao;
    }

    public Boolean getRegistroAtivo() {
	return registroAtivo;
    }

    public void setRegistroAtivo(Boolean registroAtivo) {
	this.registroAtivo = registroAtivo;
    }

    public UsuarioEn getUsuarioAlteracao() {
	return usuarioAlteracao;
    }

    public void setUsuarioAlteracao(UsuarioEn usuarioAlteracao) {
	this.usuarioAlteracao = usuarioAlteracao;
    }

    public Date getDataAlteracao() {
	return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
	this.dataAlteracao = dataAlteracao;
    }

    public UsuarioEn getUsuarioAlteracaoSituacao() {
	return usuarioAlteracaoSituacao;
    }

    public void setUsuarioAlteracaoSituacao(UsuarioEn usuarioAlteracaoSituacao) {
	this.usuarioAlteracaoSituacao = usuarioAlteracaoSituacao;
    }

    public Date getDataAlteracaoRegistroAtivo() {
	return dataAlteracaoRegistroAtivo;
    }

    public void setDataAlteracaoRegistroAtivo(Date dataAlteracaoRegistroAtivo) {
	this.dataAlteracaoRegistroAtivo = dataAlteracaoRegistroAtivo;
    }

}
