package com.effugium.core.model.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.effugium.core.model.Auditoria;
import com.effugium.core.model.EffugiumEntity;
import com.effugium.core.model.EntityFilter;

@Entity
@Table(name = "tb_grupo_usuario", schema = "core", uniqueConstraints = @UniqueConstraint(name = "uk_grupo_usuario_nome", columnNames = {
	"nome" }))
public class GrupoUsuarioEn implements EffugiumEntity {

    private static final long serialVersionUID = 9175102169571789879L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_grupo_usuario_generator")
    @SequenceGenerator(name = "sq_grupo_usuario_generator", sequenceName = "core.sq_grupo_usuario", allocationSize = 1, initialValue = 1)
    private Long id;

    @Embedded
    private Auditoria auditoria;

    @Transient
    private Set<EntityFilter> filter;

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

    @Override
    public Auditoria getAuditoria() {
	return auditoria;
    }

    @Override
    public void setAuditoria(Auditoria auditoria) {
	this.auditoria = auditoria;
    }

    @Override
    public Set<EntityFilter> getFilter() {
	return filter;
    }

    @Override
    public void setFilter(Set<EntityFilter> filter) {
	this.filter = filter;
    }

    /*
     * intrinsic:
     */

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "descricao", nullable = true, length = 200)
    private String descricao;

    @OneToMany(mappedBy = "grupoUsuarioEn", fetch = FetchType.LAZY)
    private Set<UsuarioEn> usuarioEns;

    @OneToMany(mappedBy = "grupoUsuarioEn", fetch = FetchType.LAZY)
    private Set<AcessoGrupoUsuarioEn> acessoGrupoUsuarioEns;

    /*
     * GET's e SET's:
     */

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public String getDescricao() {
	return descricao;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    public Set<UsuarioEn> getUsuarioEns() {
	return usuarioEns;
    }

    public void setUsuarioEns(Set<UsuarioEn> usuarioEns) {
	this.usuarioEns = usuarioEns;
    }

    public Set<AcessoGrupoUsuarioEn> getAcessoGrupoUsuarioEns() {
	return acessoGrupoUsuarioEns;
    }

    public void setAcessoGrupoUsuarioEns(Set<AcessoGrupoUsuarioEn> acessoGrupoUsuarioEns) {
	this.acessoGrupoUsuarioEns = acessoGrupoUsuarioEns;
    }

}
