package com.cnatives.ms.generator.application;

import com.cnatives.ms.generator.base.BaseClass;
import com.cnatives.ms.generator.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class ApplicationTemplate extends BaseClass{
	
	private static final String TEMPLATE_DIR = "templates";
	private static final String TEMPLATE = "application.template";
	private static final String APPLICATION = "Application";
	private static final String FILE_EXTENSION = ".java";
	
	private ApplicationTemplate() {}
	
	private void updateTemplateDetails(GeneratorMetaData metaData) {
		this.templateName = TEMPLATE;
		this.templateDir = TEMPLATE_DIR;
		this.bindingName = APPLICATION.toLowerCase();
		this.fileExtension = FILE_EXTENSION;
		this.codeGenDirPath = metaData.getCodeGenDirPath();
	}
	
	private void updateClassDetails(GeneratorMetaData metaData) {
		this.packageName = metaData.getBasePackage()+"."+metaData.getDomainName().toLowerCase();
		this.className = metaData.getDomainName()+APPLICATION;
	}
	
	public static ApplicationTemplateBuilder builder() {
		return new ApplicationTemplateBuilder();
	}
	
	public static class ApplicationTemplateBuilder {

		public ApplicationTemplate buildFrom(
				final GeneratorMetaData metaData
				) {
			
			ApplicationTemplate template = new ApplicationTemplate();
			template.updateTemplateDetails(metaData);
			template.updateClassDetails(metaData);
			return template;
		}
	}

}
