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
	
	private final String MYSQL_DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	private final String POSTGRES_DB_DRIVER = "org.postgresql.Driver";
	private String servicePort;
	private String serviceName;
	private String dbUrl;
	private String dbusername;
	private String dbpassword;
	private String schemaName;
	private String resourceName;
	private String pkType;
	private String databaseType;
	
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
		if(null != this.getDatabaseType() &&
				this.getDatabaseType().equalsIgnoreCase("mysql")) {	
			return MYSQL_DB_DRIVER;
		}
		else if(null != this.getDatabaseType() &&
				(this.getDatabaseType().equalsIgnoreCase("postgres") ||
				this.getDatabaseType().equalsIgnoreCase("postgressql"))) {
			return POSTGRES_DB_DRIVER;
		}
		else {
			return "";
		}
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
	
	public void initialize() {
		
		if(null == this.servicePort || this.servicePort.isBlank() || this.servicePort.isEmpty()) {
			this.servicePort = "8080";
		}
		
		if(null == this.serviceName || this.serviceName.isBlank() || this.serviceName.isEmpty()) {
			this.serviceName = "test-service";
		}
		
		if(null == this.dbUrl || this.dbUrl.isBlank() || this.dbUrl.isEmpty()) {
			this.dbUrl = "jdbc:mysql://127.0.0.1:3306/testdb";
		}
		
		if(null == this.dbusername || this.dbusername.isBlank() || this.dbusername.isEmpty()) {
			this.dbusername = "testusername";
		}
		if(null == this.dbpassword || this.dbpassword.isBlank() || this.dbpassword.isEmpty()) {
			this.dbpassword = "ChangeMe";
		}
		if(null == this.schemaName || this.schemaName.isBlank() || this.schemaName.isEmpty()) {
			this.schemaName = "testschema";
		}
		if(null == this.resourceName || this.resourceName.isBlank() || this.resourceName.isEmpty()) {
			this.resourceName = "TestResource";
		}
		if(null == this.pkType || 
				this.pkType.isBlank() || 
				this.pkType.isEmpty() || 
				this.pkType.equalsIgnoreCase("number")) {
			this.pkType = "Long";
		}
		if(null == this.databaseType || this.databaseType.isBlank() || this.databaseType.isEmpty()) {
			this.databaseType = "mysql";
		}
	}

}
