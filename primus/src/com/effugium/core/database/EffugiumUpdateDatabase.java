package com.effugium.core.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.effugium.core.model.entities.DatabaseUpdateEn;
import com.effugium.core.util.EffugiumUtil;

public class EffugiumUpdateDatabase {

    private static boolean updateStarted = false;

    private static final String SCRIPTS_PATH = "resources/database/";
    private static final int FILE_PADD_SIZE = 3; // 001, 002, 003...
    private static final String FILE_EXT = ".script"; // "001.script"
    private static final String ERRO_FORMACAO_DA_LINHA = "Mal formação da linha de atualização do banco.";
    private static final String ERRO_EXECUCAO_DA_LINHA = "Erro na execução da linha da atualização do banco.";

    public enum UpdateResult {
	SUCESSO,
	EM_AMDAMENTO,
	ERRO
    };

    public class Resultado {
	public UpdateResult updateResult;
	public String erroMsg = "";
    }

    private synchronized boolean isStarted() {
	if (updateStarted) {
	    return true;
	}
	updateStarted = true;
	return false;
    }

    public Resultado updateDatabase() {
	Resultado resultado = new Resultado();

	// Evitando execução simultânea (concorrência).
	if (updateStarted || isStarted()) {
	    // Esse "ou" evita o enfilamento desnecessário das threads.
	    // O método synchronized só é chamado caso "updateStarted = false";
	    // Logo apenas as primeiras threads pegam fila.
	    resultado.updateResult = UpdateResult.EM_AMDAMENTO;
	    return resultado;
	}

	try {

	    DatabaseUpdateEn ultimaAtualizacao = getLastUpdate();
	    executarScripts(ultimaAtualizacao);
	    resultado.updateResult = UpdateResult.SUCESSO;

	} catch (Exception e) {
	    resultado.updateResult = UpdateResult.ERRO;
	    resultado.erroMsg = e.getMessage();
	}

	return resultado;
    }

    private void executarScripts(DatabaseUpdateEn ultimaAtualizacao) throws Exception {
	boolean existemArquivosParaProcessar = true;
	int arquivoParaExecutar;
	int linhaParaExecutar;

	if (ultimaAtualizacao == null) {
	    // First Install
	    arquivoParaExecutar = 1;
	    linhaParaExecutar = 1;
	} else {
	    arquivoParaExecutar = Integer.valueOf(ultimaAtualizacao.getArquivo());
	    linhaParaExecutar = ultimaAtualizacao.getLinha().intValue() + 1;
	}

	while (existemArquivosParaProcessar) {

	    existemArquivosParaProcessar = processarArquivo(arquivoParaExecutar, linhaParaExecutar);

	    if (existemArquivosParaProcessar) {
		arquivoParaExecutar++;
		linhaParaExecutar = 1;
	    }
	}
    }

    /**
     * @return true se processado com sucesso, false se o arquivo não existir.
     * @throws Exception
     */
    private boolean processarArquivo(int arquivoParaExecutar, int linhaParaExecutar) throws Exception {
	String filePath = SCRIPTS_PATH + getCompleteFileName(arquivoParaExecutar);

	if (this.getClass().getClassLoader().getResource(filePath) == null) {
	    return false; // Arquivo não existe.
	}

	try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

	    String strLine;
	    int lineNumber = 0;

	    while ((strLine = br.readLine()) != null) {
		lineNumber++;

		if (lineNumber < linhaParaExecutar) {
		    continue;
		}

		LinhaDoArquivo linha = validarTratarLinhaParaExecucao(strLine, lineNumber, arquivoParaExecutar);

		executarLinha(linha, lineNumber, arquivoParaExecutar);
	    }

	    return true;
	}
    }

    private void executarLinha(LinhaDoArquivo line, int lineNumber, int arquivoParaExecutar) throws Exception {
	String erroMsg = ERRO_EXECUCAO_DA_LINHA + getInfoLinhaArquivo(lineNumber, arquivoParaExecutar);

	final boolean divergenciaNaInstancia;
	if (line.instancia.isEmpty()) {
	    divergenciaNaInstancia = false;
	} else {
	    divergenciaNaInstancia = verificarDivergenciaNaInstancia(line.instancia);
	}

	try (Session session = EffugiumSessionFactory.getSession()) {
	    try {
		session.doWork(new Work() {
		    @Override
		    public void execute(Connection connection) throws SQLException {
			try {
			    if (!divergenciaNaInstancia) {
				try (CallableStatement call = connection.prepareCall(line.scriptSql)) {
				    call.execute();
				}
			    }
			    String scriptAuditoria = montarScriptDeAuditoria(line, divergenciaNaInstancia, lineNumber,
				    arquivoParaExecutar);
			    try (CallableStatement call = connection.prepareCall(scriptAuditoria)) {
				call.execute();
			    }
			    connection.commit();
			} catch (Exception e) {
			    connection.rollback();
			    throw e;
			} finally {
			    connection.close();
			}
		    }

		    private String montarScriptDeAuditoria(LinhaDoArquivo line, boolean divergenciaNaInstancia,
			    int lineNumber, int arquivoParaExecutar) {
			String scriptSql = " INSERT INTO core.tb_database_update ";
			scriptSql += " (id, arquivo, atividade, executado, instancia, linha, scriptsql) ";
			scriptSql += " VALUES ( ";
			scriptSql += " nextval('core.sq_database_update'), ";
			scriptSql += " '" + getFileName(arquivoParaExecutar) + "' ,";
			scriptSql += " '" + line.atividade.replaceAll("'", "''") + "' ,";
			if (divergenciaNaInstancia) {
			    scriptSql += " false, ";
			} else {
			    scriptSql += " true , ";
			}
			scriptSql += " '" + prepareToSave(line.instancia) + "' ,";
			scriptSql += " " + lineNumber + " ,";
			scriptSql += " '" + line.scriptSql.replaceAll("'", "''") + "' ";
			scriptSql += " ) ";
			return scriptSql;
		    }

		    private String prepareToSave(String instancia) {
			if (instancia.length() <= 30) {
			    return instancia;
			}
			return StringUtils.truncate(instancia, 27) + "...";
		    }
		});
	    } catch (Exception e) {
		throw new Exception(erroMsg + " Detalhe: " + e.getMessage());
	    }
	}

    }

    private boolean verificarDivergenciaNaInstancia(String instancias) {
	boolean intanciaAtualEncontrada = false;
	String[] instanciasArray = instancias.split(",");
	for (int i = 0; i < instanciasArray.length; i++) {
	    if (EffugiumUtil.EFFUGIUM_INSTANCE == EffugiumInstanceEnum.valueOf(instanciasArray[i])) {
		intanciaAtualEncontrada = true;
	    }
	}
	return !intanciaAtualEncontrada;
    }

    private LinhaDoArquivo validarTratarLinhaParaExecucao(String strLine, int lineNumber, int arquivoParaExecutar)
	    throws Exception {
	String erroMsg = ERRO_FORMACAO_DA_LINHA + getInfoLinhaArquivo(lineNumber, arquivoParaExecutar);

	strLine = strLine.trim();

	if (!strLine.substring(strLine.length() - 1, strLine.length()).equalsIgnoreCase(";")) {
	    throw new Exception(erroMsg + " Caractere ';' ausente no final.");
	}

	if (StringUtils.countMatches(strLine, ";") != 3) {
	    throw new Exception(erroMsg + " Caractere ';' faltando ou excessivo.");
	}

	LinhaDoArquivo linha = createLinhaDoArquivo(strLine);

	if (linha.scriptSql.length() > 4000) {
	    throw new Exception(erroMsg + " Script SQL com mais de 4000 caracteres; não é possível salvar.");
	}

	if (!linha.instancia.isEmpty()) {
	    try {
		String[] instanciasArray = linha.instancia.split(",");
		for (int i = 0; i < instanciasArray.length; i++) {
		    EffugiumInstanceEnum.valueOf(instanciasArray[i]);
		}
	    } catch (Exception e) {
		throw new Exception(erroMsg + " Instância não reconhecida.");
	    }
	}

	return linha;
    }

    private String getInfoLinhaArquivo(int lineNumber, int arquivoParaExecutar) {
	return " [Linha: " + lineNumber + ", Arquivo: " + getCompleteFileName(arquivoParaExecutar) + "]";
    }

    private LinhaDoArquivo createLinhaDoArquivo(String strLine) {
	LinhaDoArquivo linha = new LinhaDoArquivo();
	String[] lineParts = strLine.split(";");
	linha.instancia = lineParts[0];
	linha.atividade = lineParts[1];
	linha.scriptSql = lineParts[2];
	return linha;
    }

    private String getCompleteFileName(int arquivoParaExecutar) {
	return getFileName(arquivoParaExecutar) + FILE_EXT;
    }

    private String getFileName(int arquivoParaExecutar) {
	return StringUtils.leftPad(String.valueOf(arquivoParaExecutar), FILE_PADD_SIZE, "0");
    }

    private DatabaseUpdateEn getLastUpdate() {
	try (Session session = EffugiumSessionFactory.getSession()) {
	    return session.createQuery("select d from DatabaseUpdateEn d order by d.id desc", DatabaseUpdateEn.class)
		    .setMaxResults(1).uniqueResult();
	}
    }

    private class LinhaDoArquivo {
	private String instancia;
	private String atividade;
	private String scriptSql;
    }
}
