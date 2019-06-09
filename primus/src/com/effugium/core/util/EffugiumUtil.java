package com.effugium.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import com.effugium.core.database.EffugiumInstanceEnum;

public class EffugiumUtil {

    private static final String SYS_NAME = "effugium";
    private static final String SYS_CONFIG_PATH = getPath("/" + SYS_NAME + "/config");
    private static final String DATABASE_CONFIG_FILE = "database.properties";

    public static final String PATH_DATABASE_CONFIG = SYS_CONFIG_PATH + DATABASE_CONFIG_FILE;
    public static final EffugiumInstanceEnum EFFUGIUM_INSTANCE;

    static {
	Properties properties = new Properties();
	try (InputStream stream = new FileInputStream(PATH_DATABASE_CONFIG)) {
	    properties.load(stream);
	    String token = EffugiumCrypto.decrypt(properties.get("effugium.token").toString());
	    EFFUGIUM_INSTANCE = EffugiumInstanceEnum.valueOf(token);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new ExceptionInInitializerError(e);
	}
    }

    public static String getPath(String path) {
	return Paths.get(path).toString() + File.separator.toString();
    }
}
