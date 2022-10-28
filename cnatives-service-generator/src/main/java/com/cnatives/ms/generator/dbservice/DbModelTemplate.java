package com.cnatives.ms.generator.dbservice;

import java.util.List;

import com.cnatives.ms.generator.msservice.base.BaseClass;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;

import lombok.Getter;

public class DbModelTemplate extends BaseClass {
	
	private static final String TEMPLATE_DIR = "templates";
	private static final String FILE_EXTENSION = ".sql";
	private static final String BINDING_NAME = "dbModels";
	private static final String TENPLATE_NAME = "V1.0__create_table.template";
	
	@Getter
	private List<DbModel> dbModelList;
	
	@Getter
	private String schemaName;
	
	private void updateTemplateDetails(final GeneratorMetaData metaData, 
			final String schemaName,
			final List<DbModel> dbModelList) {
		
		this.codeGenDirPath = metaData.getDbscriptGenDirPath();
		this.templateDir = TEMPLATE_DIR;
		this.templateName = TENPLATE_NAME;
		this.fileExtension = FILE_EXTENSION;
		this.className = "V1.0__create_"+metaData.getDomainName()+"_table";
		this.schemaName = schemaName;
		this.bindingName = BINDING_NAME;
		this.dbModelList = dbModelList;
	}
	
	private DbModelTemplate() {}
	
	public static DbModelTemplateBuilder builder() {
		return new DbModelTemplateBuilder();
	}
	
	public static class DbModelTemplateBuilder {
		
		public DbModelTemplate buildFrom(final List<DbModel> dbModelList, 
				final GeneratorMetaData metaData,
				final String schemaName) {
			
			DbModelTemplate template = new DbModelTemplate();
			template.updateTemplateDetails(metaData, schemaName, dbModelList);
			
			return template;
		}
		
	}

}
