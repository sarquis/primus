package com.effugium.core.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.effugium.core.database.EffugiumSessionFactory;
import com.effugium.core.util.EffugiumExcepion;
import com.effugium.core.util.EffugiumExcepion.TipoException;
import com.effugium.core.util.EffugiumHttpSession;

public abstract class EffugiumDao<E extends EffugiumEntity> {

    private Class<E> entityClassType;
    private List<String> columnNamesListEntity;

    protected EffugiumDao(Class<E> entityClassType) {
	this.entityClassType = entityClassType;
	setupColumnNamesList();
    }

    private void setupColumnNamesList() {
	columnNamesListEntity = setupColumnNamesList(entityClassType.getDeclaredFields());
    }

    private List<String> setupColumnNamesList(Field[] fields) {
	List<String> namesList = new ArrayList<String>();
	for (Field field : fields) {
	    Column column = field.getAnnotation(Column.class);
	    if (column != null) {
		namesList.add(field.getName());
	    }
	}
	return namesList;
    }

    private void exigirIdDoRegistro(E entity) throws EffugiumExcepion {
	if (entity.getId() == null) {
	    throw new EffugiumExcepion("idRegistroAusente", TipoException.ERRO_IMPLEMENTACAO);
	}
    }

    private void exigirAuditoriaDoRegistro(E entity) throws EffugiumExcepion {
	if (entity.getAuditoria() == null) {
	    throw new EffugiumExcepion("auditoriaAusente", TipoException.ERRO_IMPLEMENTACAO);
	}
    }

    private boolean isNullOrBlank(Object valor) {
	if (valor == null) {
	    return true;
	}
	if (valor instanceof String && ((String) valor).isBlank()) {
	    return true;
	}
	return false;
    }

    private Object ifDateTrunc(Object object) throws ParseException {
	if (object instanceof Date) {
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    String dateString = format.format((Date) object);
	    object = format.parse(dateString);
	}
	return object;
    }

    private String getSelectQuery() {
	return " SELECT e FROM " + entityClassType.getSimpleName() + " e ";
    }

    protected String getAuditoriaJoins() {
	String queryJoins = "";
	queryJoins += " inner join fetch e.auditoria.usuarioInclusao ";
	queryJoins += " left join fetch e.auditoria.usuarioAlteracao ";
	queryJoins += " left join fetch e.auditoria.usuarioAlteracaoSituacao ";
	return queryJoins;
    }

    /*
     * ##############################
     * 
     * Ativar e desativar o registro:
     * 
     * ##############################
     */

    public E ativarRegistro(E entity) throws Exception {
	return alterarSituacaoDoRegistro(entity, true);
    }

    public E inativarRegistro(E entity) throws Exception {
	return alterarSituacaoDoRegistro(entity, false);
    }

    public E ativarRegistro(E entity, Session session) throws Exception {
	return alterarSituacaoDoRegistro(entity, true, session);
    }

    public E inativarRegistro(E entity, Session session) throws Exception {
	return alterarSituacaoDoRegistro(entity, false, session);
    }

    private E alterarSituacaoDoRegistro(E entity, boolean ativo) throws Exception {
	exigirIdDoRegistro(entity);
	try (Session session = EffugiumSessionFactory.getSession()) {
	    Transaction tx = null;
	    try {
		tx = session.beginTransaction();
		tratarAuditoriaAlterarSituacao(entity);
		entity.getAuditoria().setRegistroAtivo(ativo);
		session.saveOrUpdate(entity);
		tx.commit();
		return entity;
	    } catch (Exception e) {
		EffugiumSessionFactory.rollbackTransactionQuietly(tx);
		throw e;
	    }
	}
    }

    private E alterarSituacaoDoRegistro(E entity, boolean ativo, Session session) throws Exception {
	exigirIdDoRegistro(entity);
	tratarAuditoriaAlterarSituacao(entity);
	entity.getAuditoria().setRegistroAtivo(ativo);
	session.saveOrUpdate(entity);
	return entity;
    }

    private void tratarAuditoriaAlterarSituacao(E entity) throws EffugiumExcepion {
	exigirAuditoriaDoRegistro(entity);
	Auditoria auditoria = entity.getAuditoria();
	auditoria.setUsuarioAlteracaoSituacao(new EffugiumHttpSession().obterUsuarioDaSessao());
	auditoria.setDataAlteracaoRegistroAtivo(new Date());
	entity.setAuditoria(auditoria);
    }

    /*
     * ###################
     * 
     * Salvar e atualizar:
     * 
     * ###################
     */

    public E saveOrUpdate(E entity) throws Exception {
	return saveOrUpdateAuditoriaFlex(entity, true);
    }

    public E saveOrUpdate(E entity, Session session) throws Exception {
	return saveOrUpdateFinal(entity, session, true);
    }

    public E saveOrUpdateSemAuditoria(E entity) throws Exception {
	return saveOrUpdateAuditoriaFlex(entity, false);
    }

    private E saveOrUpdateFinal(E entity, Session session, boolean usarAuditoria) throws Exception {
	if (usarAuditoria) {
	    tratarAuditoriaSaveOrUpdate(entity);
	}
	session.saveOrUpdate(entity);
	return entity;
    }

    private E saveOrUpdateAuditoriaFlex(E entity, boolean usarAuditoria) throws Exception {
	try (Session session = EffugiumSessionFactory.getSession()) {
	    Transaction tx = null;
	    try {
		tx = session.beginTransaction();
		saveOrUpdateFinal(entity, session, usarAuditoria);
		tx.commit();
		return entity;
	    } catch (Exception e) {
		EffugiumSessionFactory.rollbackTransactionQuietly(tx);
		throw e;
	    }
	}
    }

    private void tratarAuditoriaSaveOrUpdate(E entity) throws EffugiumExcepion {
	boolean novoRegistro = (entity.getId() == null);
	if (!novoRegistro) {
	    exigirAuditoriaDoRegistro(entity);
	}
	Auditoria auditoria = entity.getAuditoria();
	if (novoRegistro) {
	    auditoria = new Auditoria();
	    auditoria.setRegistroAtivo(true);
	    auditoria.setUsuarioInclusao(new EffugiumHttpSession().obterUsuarioDaSessao());
	    auditoria.setDataInclusao(new Date());
	} else {
	    auditoria.setUsuarioAlteracao(new EffugiumHttpSession().obterUsuarioDaSessao());
	    auditoria.setDataAlteracao(new Date());
	}
	entity.setAuditoria(auditoria);
    }

    /*
     * ##########
     * 
     * Consultas:
     * 
     * ##########
     */

    public E uniqueResultById(Long entityId) throws Exception {
	return uniqueResultById(entityId, null);
    }

    public E uniqueResultById(Long entityId, List<Enum<?>> joins) throws Exception {
	try (Session session = EffugiumSessionFactory.getSession()) {

	    String querySelect = getSelectQuery();
	    String queryJoins = (joins == null ? "" : getJoins(joins));
	    String queryWhere = " where e.id = " + entityId;

	    return session.createQuery(querySelect + queryJoins + queryWhere, entityClassType).uniqueResult();
	} catch (Exception e) {
	    throw e;
	}
    }

    public E uniqueResult(E entity) throws Exception {
	return uniqueResult(entity, null);
    }

    public E uniqueResult(E entity, List<Enum<?>> joins) throws Exception {
	try (Session session = EffugiumSessionFactory.getSession()) {
	    Query<E> query = createQuery(entity, joins, session);
	    return query.uniqueResult();
	} catch (Exception e) {
	    throw e;
	}
    }

    public List<E> list(E entity) throws Exception {
	return list(entity, null);
    }

    public List<E> list(E entity, List<Enum<?>> joins) throws Exception {
	try (Session session = EffugiumSessionFactory.getSession()) {
	    Query<E> query = createQuery(entity, joins, session);
	    return query.list();
	} catch (Exception e) {
	    throw e;
	}
    }

    /*
     * #########################
     * 
     * Auxiliares das Consultas:
     * 
     * #########################
     */

    public abstract List<Enum<?>> getViewUpdateJoin();

    public abstract List<Enum<?>> getSearchJoin();

    protected abstract String getJoins(List<Enum<?>> joins);

    protected abstract Map<String, String> getCustomFilters();

    private Query<E> createQuery(E entity, List<Enum<?>> joins, Session session) throws IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException, ParseException, EffugiumExcepion {

	String querySelect = getSelectQuery();
	String queryJoins = (joins == null ? "" : getJoins(joins));
	String queryWhere = createHqlWhere(entity);

	Query<E> query = session.createQuery(querySelect + queryJoins + queryWhere, entityClassType);
	query = setAllParameters(query, entity);

	return query;
    }

    private Query<E> setAllParameters(Query<E> query, E entity)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException {

	for (String column : columnNamesListEntity) {
	    Object entityColumnValue = PropertyUtils.getProperty(entity, column);
	    if (!isNullOrBlank(entityColumnValue)) {
		query.setParameter(column, ifDateTrunc(entityColumnValue));
	    }
	}

	if (entity.getFilter() != null) {
	    for (EntityFilter filter : entity.getFilter()) {
		if (!(filter.getValue() instanceof NoBindValue)) {
		    query.setParameter(filter.getName(), ifDateTrunc(filter.getValue()));
		}
	    }
	}

	return query;
    }

    private String createHqlWhere(E entity)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, EffugiumExcepion {
	String hqlWhere = "";

	String hqlEntityFilters = readEntityFilters(entity);
	String hqlCustomFilters = readCustomFilters(entity, hqlEntityFilters.isBlank());

	if (!hqlEntityFilters.isBlank() || !hqlCustomFilters.isBlank()) {
	    hqlWhere = " WHERE " + hqlEntityFilters + hqlCustomFilters;
	}
	return hqlWhere;
    }

    private String readEntityFilters(E entityFilter)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	String hql = "";

	for (String column : columnNamesListEntity) {

	    Object entityColumnValue = PropertyUtils.getProperty(entityFilter, column);

	    if (isNullOrBlank(entityColumnValue)) {
		continue;
	    }

	    hql += (hql.isBlank() ? "" : " AND ");

	    if (entityColumnValue instanceof Date) {
		hql += " date_trunc('day', e." + column + ") = :" + column;
	    } else {
		hql += " e." + column + " = :" + column;
	    }
	}
	return hql;
    }

    private String readCustomFilters(E entity, boolean isBlankLastHql) throws EffugiumExcepion {
	String hql = "";

	if (entity.getFilter() == null) {
	    return hql;
	}

	Map<String, String> filterMapDao = getCustomFilters();

	for (EntityFilter filter : entity.getFilter()) {

	    String hqlFilterDao = filterMapDao.get(filter.getName());

	    if (hqlFilterDao == null) {
		// Caso não encontre um filtro correspondente no getCustomFilters do DAO.
		throw new EffugiumExcepion("customFiltersAusenteIsNull", TipoException.ERRO_IMPLEMENTACAO);
	    }

	    hql += (hql.isBlank() && isBlankLastHql ? "" : " AND ");

	    if (filter.getValue() instanceof NoBindValue) {
		hql += " " + hqlFilterDao + " ";
	    } else {
		hql += " " + hqlFilterDao + " :" + filter.getName();
	    }
	}
	return hql;
    }

}
