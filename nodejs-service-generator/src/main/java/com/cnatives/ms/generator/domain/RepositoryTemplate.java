package com.cnatives.ms.generator.domain;

import com.cnatives.ms.contract.CodeGenerationRequest;
import com.cnatives.ms.contract.DomainModel;
import com.cnatives.ms.generator.base.BaseClass;
import com.cnatives.ms.generator.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper=true)
public class RepositoryTemplate extends BaseClass {
	
	private static final String TEMPLATE_NAME = "repository.template";
	private static final String DOMAIN = "domain";
	private static final String REPOSITORY = "Repository";
	private static final String FILE_EXTENSION = ".java";
	
	@Getter
	private String domainClassName;
	
	@Getter
	private String pkDatatype;
	
	private RepositoryTemplate() {}
	
	private void updateTemplateDetails(final GeneratorMetaData metaData) {
		this.templateName = TEMPLATE_NAME;
		this.codeGenDirPath = metaData.getCodeGenDirPath()+DOMAIN+"/";
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = REPOSITORY.toLowerCase();
	}
	
	private void updateRepositoryDetails(
			final CodeGenerationRequest request,
			final DomainModel domainModel,
			final GeneratorMetaData metaData) {
		
		this.packageName = metaData.getBasePackage()+"."+metaData.getDomainName().toLowerCase()+"."+DOMAIN;
		this.className = domainModel.getName()+REPOSITORY;
		this.domainClassName = domainModel.getName();
		this.pkDatatype = metaData.getPkDataType();
	}
	
	public static RepositoryTemplateBuilder builder() {
		return new RepositoryTemplateBuilder();
	}

	public static class RepositoryTemplateBuilder{
		
//		private String templateDir;
//		private String templateName;
//		private String codeGenDirPath;
//		private String className;
//		private String fileExtension;
//		private String bindingName;
//		private String packageName;
//		
//		private String domainClassName;
//		private String pkDatatype;
		
		public RepositoryTemplate buildFrom(
				final CodeGenerationRequest request,
				final DomainModel domainModel,
				final GeneratorMetaData metaData) {
			
			RepositoryTemplate template = new RepositoryTemplate();
			template.updateRepositoryDetails(request, domainModel, metaData);
			template.updateTemplateDetails(metaData);
			return template;
		}
		
//		public RepositoryTemplate build() {
//			RepositoryTemplate template = new RepositoryTemplate();
//			template.bindingName = this.bindingName;
//			template.className = this.className;
//			template.codeGenDirPath = this.codeGenDirPath;
//			template.templateDir = this.templateDir;
//			template.templateName = this.templateName;
//			template.fileExtension = this.fileExtension;
//			template.packageName = this.packageName;
//			
//			template.domainClassName = this.domainClassName;
//			template.pkDatatype = this.pkDatatype;
//			
//			return template;
//		}
		
	}
}
