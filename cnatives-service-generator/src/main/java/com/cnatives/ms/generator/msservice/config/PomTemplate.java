package com.cnatives.ms.generator.msservice.config;

import com.cnatives.ms.generator.msservice.base.BaseClass;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper=true)
public class PomTemplate extends BaseClass{
	
	private static final String TEMPLATE_DIR = "templates";
	private static final String TEMPLATE_NAME = "pom.template";
	private static final String POM = "pom";
	private static final String FILE_EXTENSION = ".xml";
	
	@Getter
	private String mysqlDb;
	
	@Getter
	private String postgresDb;
	
	private PomTemplate() {}
	
	private void updatePomDetails(final GeneratorMetaData metaData) {
		this.templateDir = TEMPLATE_DIR;
		this.templateName = TEMPLATE_NAME;
		//this.codeGenDirPath = metaData.getBasePath()+"/"+metaData.getServiceName()+"/";
		this.codeGenDirPath = metaData.getPomFilePath();
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = POM.toLowerCase();
		this.className = POM;
		
		this.serviceName = metaData.getServiceName();
		if(null != metaData.getDatabaseType() &&
				metaData.getDatabaseType().equalsIgnoreCase("mysql")) {
			this.mysqlDb = "mysql";
		}
		
		if(null != metaData.getDatabaseType() &&
				(metaData.getDatabaseType().equalsIgnoreCase("postgres") || 
						metaData.getDatabaseType().equalsIgnoreCase("postgressql"))) {
			this.postgresDb = "postgres";
		}
	}
	
	public static PomTemplateBuilder builder() {
		return new PomTemplateBuilder();
	}
	
	public static class PomTemplateBuilder {
		
		public PomTemplate buildFrom(
				final GeneratorMetaData metaData
				) {
			
			PomTemplate template = new PomTemplate();
			template.updatePomDetails(metaData);
			return template;
		}
	}

}
