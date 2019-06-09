package com.effugium.core.database;

import java.util.EnumSet;

import org.hibernate.boot.MetadataSources;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;

public class EffugiumDatabaseScriptCreator {

    public static void main(String[] args) throws Exception {
	EffugiumDatabaseScriptCreator.createDatabaseScript(false);
    }

    public static void createDatabaseScript(boolean createNewDatabase) throws Exception {
	Configuration configuration = EffugiumSessionFactory.getConfiguration();
	MetadataSources metadataSources = EffugiumSessionFactory.getMetadataSources(configuration);
	EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.STDOUT);

	if (createNewDatabase) {
	    SchemaExport export = new SchemaExport();
	    export.setDelimiter(";");
	    export.setFormat(false);
	    export.createOnly(targetTypes, metadataSources.buildMetadata());
	} else {
	    SchemaUpdate update = new SchemaUpdate();
	    update.setDelimiter(";");
	    update.setFormat(false);
	    update.execute(targetTypes, metadataSources.buildMetadata());
	}
    }

}
