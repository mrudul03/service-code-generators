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
		this.codeGenDirPath = metaData.getServiceCodeGenDirPath(domainModel.getParententity())+DOMAIN+"/";
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = REPOSITORY.toLowerCase();
	}
	
	private void updateRepositoryDetails(
			final CodeGenerationRequest request,
			final DomainModel domainModel,
			final GeneratorMetaData metaData) {
		
		this.packageName = metaData.getBasePackageName()+"."+domainModel.getParententity().toLowerCase()+"."+DOMAIN;
		this.className = domainModel.getName()+REPOSITORY;
		this.domainClassName = domainModel.getName();
		this.domainEntityClassName = domainModel.getName()+"Entity";
		this.pkDatatype = metaData.getPkDataType();
		this.repositoryInterfaceName = "PanacheMongoRepository";
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
