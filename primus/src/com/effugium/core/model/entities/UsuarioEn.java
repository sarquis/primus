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
import javax.persistence.UniqueConstraint;

import org.apache.commons.validator.routines.EmailValidator;

import com.effugium.core.model.Auditoria;
import com.effugium.core.model.EffugiumEntity;
import com.effugium.core.model.EntityFilter;
import com.effugium.core.util.EffugiumCrypto;
import com.effugium.core.util.EffugiumExcepion;
import com.effugium.core.util.EffugiumExcepion.TipoException;

@Entity
@Table(name = "tb_usuario", schema = "core", uniqueConstraints = @UniqueConstraint(name = "uk_usuario_email", columnNames = {
	"email" }))
public class UsuarioEn implements EffugiumEntity {

    private static final long serialVersionUID = 9175102169571789879L;

    /*
     * O SEQUENCE, inicia no id 3, por que o id 1 e id 2 são para admin e consultor.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_usuario_generator")
    @SequenceGenerator(name = "sq_usuario_generator", sequenceName = "core.sq_usuario", allocationSize = 1, initialValue = 3)
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

    /**
     * E-mail é utilizado como login.
     */
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    /**
     * Senha criptografada.
     */
    @Column(name = "senha", nullable = false, length = 50)
    private String senha;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo_usuario", nullable = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario_grupo_usuario"))
    private GrupoUsuarioEn grupoUsuarioEn;

    /*
     * GET's e SET's:
     */

    /**
     * Utilizar nas telas: identificação do usuário.
     */
    @Override
    public String toString() {
	return nome + " (" + email + ")";
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) throws EffugiumExcepion {
	email = email.toLowerCase();
	if (EmailValidator.getInstance().isValid(email)) {
	    this.email = email;
	} else {
	    throw new EffugiumExcepion("emailInvalido", TipoException.VALIDACAO_DADOS);
	}
    }

    public String getSenha() throws Exception {
	return (senha == null ? null : EffugiumCrypto.decrypt(senha));
    }

    public void setSenha(String senha) throws Exception {
	this.senha = EffugiumCrypto.encrypt(senha);
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome.toUpperCase();
    }

    public GrupoUsuarioEn getGrupoUsuarioEn() {
	return grupoUsuarioEn;
    }

    public void setGrupoUsuarioEn(GrupoUsuarioEn grupoUsuarioEn) {
	this.grupoUsuarioEn = grupoUsuarioEn;
    }

}
