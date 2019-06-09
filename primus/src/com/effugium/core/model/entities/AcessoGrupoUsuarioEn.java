package com.effugium.core.model.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.effugium.core.model.Auditoria;
import com.effugium.core.model.EffugiumEntity;
import com.effugium.core.model.EntityFilter;

@Entity
@Table(name = "tb_acesso_grupo_usuario", schema = "core")
public class AcessoGrupoUsuarioEn implements EffugiumEntity {

    private static final long serialVersionUID = 1005599740506909275L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_acesso_grupo_usuario_generator")
    @SequenceGenerator(name = "sq_acesso_grupo_usuario_generator", sequenceName = "core.sq_acesso_grupo_usuario", allocationSize = 1, initialValue = 1)
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

    @Column(name = "codigo", nullable = false)
    private Long codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo_usuario", nullable = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_acesso_grupo_usuario"))
    private GrupoUsuarioEn grupoUsuarioEn;

    /*
     * GET's e SET's:
     */

    public Long getCodigo() {
	return codigo;
    }

    public void setCodigo(Long codigo) {
	this.codigo = codigo;
    }

    public GrupoUsuarioEn getGrupoUsuarioEn() {
	return grupoUsuarioEn;
    }

    public void setGrupoUsuarioEn(GrupoUsuarioEn grupoUsuarioEn) {
	this.grupoUsuarioEn = grupoUsuarioEn;
    }
}
