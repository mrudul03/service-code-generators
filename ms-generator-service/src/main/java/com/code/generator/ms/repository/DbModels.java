package com.code.generator.ms.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.code.generator.ms.base.BaseClass;

public class DbModels extends BaseClass {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DbModels.class);
	
	private List<DbModel> dbModelList;
	private String schemaName;
	private DbModels() {}
	
	public List<DbModel> getDbModelList() {
		return dbModelList;
	}
	
	public String getSchemaName() {
		return this.schemaName;
	}

	public static class Builder {
		private String fileExtension;
		private String codeGenDirPath;
		private String resourceName;
		private List<DbModel> dbModelList;
		
		private String templateDir;
		private String templateName;
		
		private String schemaName;

		public Builder(final List<DbModel> dbModelList, 
				final String resourceName,
				final String schemaName) {
			this.fileExtension = ".sql";
			this.dbModelList = dbModelList;
			this.resourceName = resourceName;
			this.schemaName = schemaName;
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

		public DbModels build() {
			DbModels dbModels = new DbModels();
			dbModels.dbModelList = this.dbModelList;
			dbModels.codeGenDirPath = this.codeGenDirPath;
			dbModels.fileExtension = this.fileExtension;
			dbModels.templateName = this.templateName;
			dbModels.templateDir = this.templateDir;
			dbModels.className = "V1.0__create_"+this.resourceName+"_table";
			dbModels.schemaName = this.schemaName;
			dbModels.bindingName = "dbModels";
			LOGGER.info("Built DBModel");
			return dbModels;
		}
	}

//	protected Map<String, ? extends BaseModel> getModelBinding() {
//		Map<String, DbModels> bindings = new HashMap<>();
//		bindings.put("dbModels", this);
//		LOGGER.info("rendering dbmodels");
//		return bindings;
//	}
}
