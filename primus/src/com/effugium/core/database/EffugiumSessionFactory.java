package com.effugium.core.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.persistence.Entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import com.effugium.core.util.EffugiumCrypto;
import com.effugium.core.util.EffugiumUtil;

public class EffugiumSessionFactory {

    private static final SessionFactory SESSION_FACTORY;
    private static final Properties DB_PROPERTIES;

    /*
     * Configuração de banco:
     */
    static {
	DB_PROPERTIES = new Properties();
	try (InputStream stream = new FileInputStream(EffugiumUtil.PATH_DATABASE_CONFIG)) {
	    DB_PROPERTIES.load(stream);
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new ExceptionInInitializerError(e);
	}
    }

    /*
     * Configuração do Hibernate:
     */
    static {
	try {
	    Configuration configuration = getConfiguration();
	    Metadata metaData = getMetadataSources(configuration).getMetadataBuilder().build();
	    SESSION_FACTORY = metaData.getSessionFactoryBuilder().build();
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new ExceptionInInitializerError(e);
	}
    }

    @SuppressWarnings("resource")
    static MetadataSources getMetadataSources(Configuration configuration) {
	StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
	StandardServiceRegistry standardRegistry = builder.applySettings(configuration.getProperties()).build();
	return addAnnotatedClass(new MetadataSources(standardRegistry));
    }

    static Configuration getConfiguration() throws Exception {
	Configuration configuration = new Configuration();
	configuration.addProperties(DB_PROPERTIES);
	decryptConnPassward(configuration);
	return configuration;
    }

    private static MetadataSources addAnnotatedClass(MetadataSources metadataSources) {
	new Reflections("com.effugium.core.model.entities").getTypesAnnotatedWith(Entity.class)
		.forEach(metadataSources::addAnnotatedClass);
	return metadataSources;
    }

    private static void decryptConnPassward(Configuration configuration) throws Exception {
	String password = configuration.getProperty("hibernate.connection.password");
	configuration.setProperty("hibernate.connection.password", EffugiumCrypto.decrypt(password));
    }

    public static Session getSession() {
	return SESSION_FACTORY.openSession();
    }

    public static void rollbackTransactionQuietly(Transaction tx) {
	if (tx != null) {
	    tx.rollback();
	}
    }
}
