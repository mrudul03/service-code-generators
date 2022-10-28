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
	private String domainEntityClassName;
	
	@Getter
	private String pkDatatype;
	
	@Getter
	private String repositoryInterfaceName;
	
	private RepositoryTemplate() {}
	
	private void updateTemplateDetails(final GeneratorMetaData metaData, final DomainModel domainModel) {
		this.templateName = TEMPLATE_NAME;
		//this.codeGenDirPath = metaData.getCodeGenDirPath()+DOMAIN+"/";
		this.codeGenDirPath = metaData.getServiceCodeGenDirPath(domainModel.getDomainName())+DOMAIN+"/";
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = REPOSITORY.toLowerCase();
	}
	
	private void updateRepositoryDetails(
			final CodeGenerationRequest request,
			final DomainModel domainModel,
			final GeneratorMetaData metaData) {
		
		//this.packageName = metaData.getBasePackageName()+"."+metaData.getServiceBaseName()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
		this.packageName = metaData.getBasePackageName()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
//		if(metaData.getServiceBaseName().equalsIgnoreCase(domainModel.getDomainName())) {
//			this.packageName = metaData.getBasePackage()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
//		}
//		else {
//			this.packageName = metaData.getBasePackage()+"."+metaData.getServiceBaseName()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
//		}
		
		this.className = domainModel.getName()+REPOSITORY;
		this.domainClassName = domainModel.getName();
		this.domainEntityClassName = domainModel.getName()+"Entity";
		this.pkDatatype = metaData.getPkDataType();
		if(metaData.getDatabaseType().equalsIgnoreCase("mongo") ||
				metaData.getDatabaseType().equalsIgnoreCase("mongodb")) {
			this.repositoryInterfaceName = "PanacheMongoRepository";
		}
		else {
			this.repositoryInterfaceName = "PanacheRepository";
		}
	}
	
	public static RepositoryTemplateBuilder builder() {
		return new RepositoryTemplateBuilder();
	}

	public static class RepositoryTemplateBuilder{
		
		public RepositoryTemplate buildFrom(
				final CodeGenerationRequest request,
				final DomainModel domainModel,
				final GeneratorMetaData metaData) {
			
			RepositoryTemplate template = new RepositoryTemplate();
			template.updateRepositoryDetails(request, domainModel, metaData);
			template.updateTemplateDetails(metaData, domainModel);
			return template;
		}
	}
}
