package com.code.generator.ms.application;

import com.code.generator.ms.base.BaseClass;

public class Pom extends BaseClass{
	
	private String postgresDb;
	private String mysqlDb;
	
	private Pom() {}
	
	public String getPostgresDb() {
		return this.postgresDb;
	}
	public String getMysqlDb() {
		return this.mysqlDb;
	}
	
	public static class Builder {
		private String resourceName;
		private String serviceName;
		
		private String templateDir;
		private String templateName;
		private String codeGenDirPath;
		private String className;
		private String fileExtension;
		private String databaseType;
		
		public Builder(final String resourceName) {
			this.resourceName = resourceName;
			this.className = "pom";
			this.fileExtension = ".xml";
		}
		
		public Builder withCodeGenDirPath(final String codeGenDirPath) {
			this.codeGenDirPath = codeGenDirPath;
			return this;
		}
		public Builder withTemplateDir(String templateDir) {
			this.templateDir = templateDir;
			return this;
		}
		public Builder withTemplateName(String templateName) {
			this.templateName = templateName;
			return this;
		}
		public Builder withServiceName(String serviceName) {
			this.serviceName = serviceName;
			return this;
		}
		public Builder withDatabaseType(String databaseType) {
			this.databaseType = databaseType;
			return this;
		}
		
		
		public Pom build() {
			
			Pom pom = new Pom();
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			if(null == this.templateName) {
				this.withTemplateName("pom.template");
			}
			pom.resourceName = this.resourceName;
			pom.codeGenDirPath = this.codeGenDirPath;
			pom.templateDir = this.templateDir;
			pom.templateName = this.templateName;
			pom.serviceName = this.serviceName;
			pom.className = this.className;
			pom.fileExtension = this.fileExtension;
			pom.bindingName = "pom";
			
			if(null != this.databaseType &&
					this.databaseType.equalsIgnoreCase("mysql")) {
				pom.mysqlDb = "mysql";
			}
			
			if(null != this.databaseType &&
					(this.databaseType.equalsIgnoreCase("postgres") || 
					this.databaseType.equalsIgnoreCase("postgressql"))) {
				pom.postgresDb = "postgres";
			}
			return pom;
		}
	}
	
}
