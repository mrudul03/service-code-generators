package com.cnatives.ms.generator.config;

import com.cnatives.ms.generator.base.BaseClass;
import com.cnatives.ms.generator.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class PomTemplate extends BaseClass{
	
	private static final String TEMPLATE_DIR = "templates";
	private static final String TEMPLATE_NAME = "pom.template";
	private static final String POM = "pom";
	private static final String FILE_EXTENSION = ".xml";
	
	private PomTemplate() {}
	
	private void updatePomDetails(final GeneratorMetaData metaData) {
		this.templateDir = TEMPLATE_DIR;
		this.templateName = TEMPLATE_NAME;
		this.codeGenDirPath = metaData.getPomFilePath();
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = POM.toLowerCase();
		this.className = POM;
		this.serviceName = metaData.getServiceName();
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
