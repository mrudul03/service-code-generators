package com.cnatives.ms.generator.domain;

import java.util.ArrayList;
import java.util.List;

import com.cnatives.ms.contract.CodeGenerationRequest;
import com.cnatives.ms.contract.DomainModel;
import com.cnatives.ms.contract.DomainModelOperation;
import com.cnatives.ms.generator.base.BaseClass;
import com.cnatives.ms.generator.base.Constants;
import com.cnatives.ms.generator.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper=true)
public class ServiceTemplate extends BaseClass{
	
	private static final String SERVICE_TEMPLATE = "service.template";
	private static final String DOMAIN = "domain";
	private static final String FILE_EXTENSION = ".java";
	private static final String SERVICE = "Service";
	private static final String REPOSITORY = "Repository";

	@Getter
	private String contractImport;
	
	@Getter
	private String exceptionImport;
	
	@Getter
	private String repository;
	
	@Getter
	private String repositoryVariable;
	
	@Getter
	private List<DomainOperation> operations = new ArrayList<>();
	
	private ServiceTemplate() {}
	
	private void updateTemplateDetails(final GeneratorMetaData metaData, final DomainModel domainModel) {
		this.templateDir = Constants.TEMPLATES_DIR;
		this.templateName = SERVICE_TEMPLATE;
		//this.codeGenDirPath = metaData.getCodeGenDirPath()+DOMAIN+Constants.PATH_SEPARATOR;
		this.codeGenDirPath = metaData.getServiceCodeGenDirPath(domainModel.getDomainName())+DOMAIN+Constants.PATH_SEPARATOR;
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = SERVICE.toLowerCase();
	}
	
	private void updateClassDetails(
			final CodeGenerationRequest request,
			final DomainModel domainModel,
			final GeneratorMetaData metaData) {
		
		this.className = domainModel.getName()+SERVICE;
		
//		this.packageName = metaData.getBasePackageName()+"."+metaData.getServiceBaseName()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
		this.packageName = metaData.getBasePackageName()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
//		if(metaData.getServiceBaseName().equalsIgnoreCase(domainModel.getDomainName())) {
//			this.packageName = metaData.getBasePackage()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
//		}
//		else {
//			this.packageName = metaData.getBasePackage()+"."+metaData.getServiceBaseName()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
//		}
		
		this.repository = domainModel.getName()+REPOSITORY;
		this.repositoryVariable = createVariable(this.repository);
		
		this.contractImport = metaData.getBasePackageName()+"."+metaData.getDomainName().toLowerCase()+".contract.*";
		
//		if(metaData.getServiceBaseName().equalsIgnoreCase(domainModel.getDomainName())) {
//			this.contractImport = metaData.getBasePackage()+"."+domainModel.getDomainName().toLowerCase()+".contract.*";
//		}
//		else {
//			this.contractImport = metaData.getBasePackage()+"."+metaData.getServiceBaseName()+"."+metaData.getDomainName().toLowerCase()+".contract.*";
//		}
		this.exceptionImport = metaData.getBasePackageName()+".exception.*";
		
		List<DomainModelOperation> domainOperations = domainModel.getOperations();
		for(DomainModelOperation dOperation:domainOperations) {
			DomainOperation domainOperation = DomainOperation.builder()
					.buildFrom(domainModel, dOperation);
			this.operations.add(domainOperation);
		}
	}
	
	public static ServiceTemplateBuilder builder() {
		return  new ServiceTemplateBuilder();
	}
	
	public static class ServiceTemplateBuilder {
		public ServiceTemplate buildFrom(
				final CodeGenerationRequest request,
				final DomainModel domainModel,
				final GeneratorMetaData metaData) {
			
			ServiceTemplate template = new ServiceTemplate();
			template.updateClassDetails(request, domainModel, metaData);
			template.updateTemplateDetails(metaData, domainModel);
			return template;
		}
	}
}
