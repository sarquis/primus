package com.effugium.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.effugium.core.database.EffugiumSessionFactory;
import com.effugium.core.database.EffugiumUpdateDatabase;
import com.effugium.core.database.EffugiumUpdateDatabase.Resultado;
import com.effugium.core.model.dao.UsuarioDao;
import com.effugium.core.model.entities.DatabaseUpdateEn;
import com.effugium.core.model.entities.UsuarioEn;
import com.effugium.core.model.service.UsuarioService;

public class Test {

    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
	try (Session session = EffugiumSessionFactory.getSession()) {
	    Date date = new Date();
	    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    // String dateString = format.format(new Date());
	    // Date date = format.parse(dateString);

	    UsuarioEn entity = new UsuarioEn();
	    // entity.setNome("consultor");
	    // entity.setEmail("consultor@effugium.com");
	    // List<Enum<?>> joins = new ArrayList<Enum<?>>();
	    // joins.add(UsuarioDao.Join.AUDITORIA);
	    // entity.setFilter(new HashSet<EffugiumFilter>());
	    // entity.getFilter().add(new EffugiumFilter("dataInclusao", date));
	    entity = UsuarioService.getInstance().uniqueResult(entity);

	    // List<UsuarioEn> a = UsuarioService.getInstance().list(entity);
	    // for (UsuarioEn entity : a) {
	    System.out.println(entity);
	    // }
	}

	System.out.println("fim");
    }

    @SuppressWarnings("unused")
    private static void testeFiltroComJoin() throws Exception {
	UsuarioEn usuario = UsuarioService.getInstance().uniqueResultById(1L);
	System.out.println(usuario.toString());
	System.out.println(usuario.getAuditoria().getUsuarioInclusao().toString());

	List<Enum<?>> joins = new ArrayList<Enum<?>>();
	joins.add(UsuarioDao.Join.AUDITORIA);
	usuario = UsuarioService.getInstance().uniqueResultById(2L, joins);
	System.out.println(usuario.toString());
	System.out.println(usuario.getAuditoria().getUsuarioInclusao().toString());

    }

    @SuppressWarnings("unused")
    private static void callDatabaseUpdate() {
	EffugiumUpdateDatabase updateDatabase = new EffugiumUpdateDatabase();
	Resultado resultado = updateDatabase.updateDatabase();
	System.out.println(resultado.updateResult.name());
	System.out.println(resultado.erroMsg);
    }

    @SuppressWarnings("unused")
    private void testeAtualizarBanco() {
	for (int i = 0; i < 10; i++) {
	    MyThread myClass = new MyThread(String.valueOf(i));
	    myClass.start();
	}
    }

    @SuppressWarnings("unused")
    private static void testeInserirRegistro() throws Exception {
	for (int i = 0; i < 5; i++) {
	    DatabaseUpdateEn e = new DatabaseUpdateEn();
	    e.setArquivo("00" + i);
	    e.setAtividade("testeAtivdade " + i);
	    e.setDataInclusao(new Date());
	    e.setInstancia("teste");
	    e.setLinha((long) i);
	    e.setScriptSql("select " + i);
	    // DatabaseUpdateService.getInstance().saveOrUpdateSemAuditoria(e);
	    System.out.println(e.getId());
	}
    }

    private class MyThread extends Thread {
	String name;

	public MyThread(String name) {
	    super();
	    this.name = name;
	}

	@Override
	public void run() {
	    System.out.println("MyThread " + name);
	    EffugiumUpdateDatabase updateDatabase = new EffugiumUpdateDatabase();
	    Resultado resultado = updateDatabase.updateDatabase();
	    System.out.println("MyThread " + name + " " + resultado.updateResult.name());
	}
    }
}
