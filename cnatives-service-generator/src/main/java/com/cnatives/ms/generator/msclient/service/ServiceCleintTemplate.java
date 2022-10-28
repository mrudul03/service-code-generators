package com.cnatives.ms.generator.msclient.service;

import java.util.ArrayList;
import java.util.List;

import com.cnatives.ms.generator.mscontroller.DomainModel;
import com.cnatives.ms.generator.mscontroller.DomainModelOperation;
import com.cnatives.ms.generator.msservice.application.ContractClassField;
import com.cnatives.ms.generator.msservice.application.ControllerOperation;
import com.cnatives.ms.generator.msservice.base.BaseClass;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper=true)
public class ServiceCleintTemplate extends BaseClass {
	
	private static final String TEMPLATE_DIR = "templates";
	private static final String BINDING_NAME = "serviceclient";
	private static final String TEMPLATE = "serviceclient.template";
	private static final String FILE_EXTENSION = ".java";
	
	@Getter
	private List<ContractClassField> classfields = new ArrayList<>();

	@Getter
	private String dateDeclared;
	
	@Getter
	private String setDeclared;
	
	@Getter
	private String listDeclared;
	
	@Getter
	private String serviceName;
	
	@Getter
	private String serviceUrl;
	
	@Getter
	private String serviceClientName;
	
	@Getter
	private List<ControllerOperation> operations = new ArrayList<>();
	
	private ServiceCleintTemplate() {}
	
	private void updateTemplateDetails(GeneratorMetaData metaData) {
		this.templateName = TEMPLATE;
		this.templateDir = TEMPLATE_DIR;
		this.bindingName = BINDING_NAME;
		this.fileExtension = FILE_EXTENSION;
		this.codeGenDirPath = metaData.getClientGenDirPath();
	}
	
	private void updateClassDetails(GeneratorMetaData metaData, DomainModel domainModel) {
		
		if(null == this.packageName) {
			this.packageName = metaData.getBasePackage()+"."+metaData.getDomainName().toLowerCase()+"."+BINDING_NAME;
		}
		
		this.serviceName = metaData.getServiceName();
		this.serviceUrl = metaData.getServiceUrl();
		this.className = "ServiceClient";
		
		
		List<DomainModelOperation> domainOperations = domainModel.getOperations();
		if (null != domainOperations) {
			log.info("total domain operations ...."+domainOperations.size());
			for (DomainModelOperation dOperation : domainOperations) {
				ControllerOperation cOperation = ControllerOperation.builder()
						.buildFrom(dOperation);
				this.operations.add(cOperation);
			}
			log.info("total operations ...."+this.operations.size());
		}
	}
	
	public static ServiceCleintTemplateBuilder builder() {
		return new ServiceCleintTemplateBuilder();
	}
	
	public static class ServiceCleintTemplateBuilder{
		
		private String packageName;
		public ServiceCleintTemplateBuilder withPackageName(String packageName) {
			this.packageName = packageName;
			return this;
		}
		
		public ServiceCleintTemplate buildFrom(GeneratorMetaData metaData, 
				DomainModel domainModel) {
			
			ServiceCleintTemplate template = new ServiceCleintTemplate();
			template.packageName = this.packageName;
			
			template.updateTemplateDetails(metaData);
			template.updateClassDetails(metaData, domainModel);
			return template;
		}
	}

}
