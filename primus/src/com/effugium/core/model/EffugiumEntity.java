package com.effugium.core.model;

import java.io.Serializable;
import java.util.Set;

public interface EffugiumEntity extends Serializable {

    public Long getId();

    public void setId(Long id);

    public Auditoria getAuditoria();

    public void setAuditoria(Auditoria auditoria);

    public Set<EntityFilter> getFilter();

    public void setFilter(Set<EntityFilter> filter);

}
