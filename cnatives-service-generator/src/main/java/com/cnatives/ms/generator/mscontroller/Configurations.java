package com.cnatives.ms.generator.mscontroller;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Configurations {
	
	private final String MYSQL_DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	private final String POSTGRES_DB_DRIVER = "org.postgresql.Driver";
	
	private String servicePort;
	private String serviceName;
	private String serviceUrl;
	private String dbUrl;
	private String dbusername;
	private String dbpassword;
	private String schemaName;
	private String pkType;
	private String databaseType;
	private String contextPath;
	private String serviceBaseName;
	
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
