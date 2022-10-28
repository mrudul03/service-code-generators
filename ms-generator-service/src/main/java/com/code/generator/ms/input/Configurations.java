package com.code.generator.ms.input;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.ToString;

@ToString
public class Configurations {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Configurations.class);
	private String persistence;
	private String servicePort;
	private String serviceName;
	private String dbUrl;
	private String dbusername;
	private String dbpassword;
	private String dbDriver;
	private String schemaName;
	private String resourceName;
	private String pkType;
	private String collectionType;
	private String databaseType;
	
	public String getPersistence() {
		return persistence;
	}
	public void setPersistence(String persistence) {
		this.persistence = persistence;
	}
	public String getServicePort() {
		return servicePort;
	}
	public void setServicePort(String servicePort) {
		this.servicePort = servicePort;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getDbusername() {
		return dbusername;
	}
	public void setDbusername(String dbusername) {
		this.dbusername = dbusername;
	}
	public String getDbpassword() {
		return dbpassword;
	}
	public void setDbpassword(String dbpassword) {
		this.dbpassword = dbpassword;
	}
	public String getDbDriver() {
		return dbDriver;
	}
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getPkType() {
		return pkType;
	}
	public void setPkType(String pkType) {
		this.pkType = pkType;
	}
	public String getCollectionType() {
		return collectionType;
	}
	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}
	public String getDatabaseType() {
		return databaseType;
	}
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	
	public static Configurations loadConfiguration(String configFilePath) {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Configurations configurations = null;
		try {
			configurations = mapper.readValue(new File(configFilePath), Configurations.class);
            LOGGER.info("Got Configurations:"+configurations);
        } catch (Exception e) {
        	LOGGER.error("Error parsing configurations YAML", e);
        }
		return configurations;
	}

}
