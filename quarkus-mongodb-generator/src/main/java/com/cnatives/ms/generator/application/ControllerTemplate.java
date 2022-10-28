package com.cnatives.ms.generator.application;

import java.util.ArrayList;
import java.util.List;

import com.cnatives.ms.contract.DomainModel;
import com.cnatives.ms.contract.DomainModelOperation;
import com.cnatives.ms.generator.base.BaseClass;
import com.cnatives.ms.generator.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class ControllerTemplate extends BaseClass {
	
	private static final String TEMPLATE_DIR = "templates";
	private static final String CONTROLLER = "Controller";
	private static final String TEMPLATE = "controller.template";
	private static final String FILE_EXTENSION = ".java";
	
	private static final String SERVICE = "Service";
	private static final String DOMAIN = "domain";
	private static final String CONTRACT = "contract";
	private static final String ID = "Id";
	private static final String LONG = "Long";
	
	@Getter
	private String domainImports;
	
	@Getter
	private String contractImports;
	
	@Getter
	private String idType;
	
	@Getter
	private String idDatatype;
	
	@Getter
	private String serviceVariableName;
	
	@Getter
	private List<ControllerOperation> operations = new ArrayList<>();
	
	private void updateTemplateDetails(GeneratorMetaData metaData, DomainModel domainModel) {
		this.templateName = TEMPLATE;
		this.templateDir = TEMPLATE_DIR;
		this.bindingName = CONTROLLER.toLowerCase();
		this.fileExtension = FILE_EXTENSION;
		//this.codeGenDirPath = metaData.getCodeGenDirPath() +CONTROLLER.toLowerCase()+ "/";
		this.codeGenDirPath = metaData.getServiceCodeGenDirPath(domainModel.getDomainName())+CONTROLLER.toLowerCase()+ "/";
	}
	
	private void updateClassImports(GeneratorMetaData metaData, DomainModel domainModel) {
		this.domainImports = metaData.getBasePackageName() + "." + domainModel.getDomainName().toLowerCase()+ "."+DOMAIN+".*";
		this.contractImports = metaData.getBasePackageName() + "." + domainModel.getDomainName().toLowerCase()+ "."+CONTRACT+".*";
	}
	
	private void updateClassDetails(GeneratorMetaData metaData, DomainModel domainModel) {
		
		this.packageName = metaData.getBasePackageName() +"." + domainModel.getDomainName().toLowerCase()+ "."+CONTROLLER.toLowerCase();
//		if(metaData.getServiceBaseName().equalsIgnoreCase(domainModel.getDomainName())) {
//			this.packageName = metaData.getBasePackage() + "." + domainModel.getDomainName().toLowerCase()+ "."+CONTROLLER.toLowerCase();
//		}
//		else {
//			this.packageName = metaData.getBasePackage() +"."+metaData.getServiceBaseName()+ "." + domainModel.getDomainName().toLowerCase()+ "."+CONTROLLER.toLowerCase();
//		}
		
		this.serviceName = domainModel.getDomainName() + SERVICE;
		this.serviceVariableName = this.createVariable(this.serviceName);
		this.className = domainModel.getName() + CONTROLLER;
		this.idType = this.createVariable(metaData.getDomainName()) + ID;
		this.idDatatype = LONG;

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
	
	private ControllerTemplate() {
	}

	public static ControllerTemplateBuilder builder() {
		return new ControllerTemplateBuilder();
	}

	public static class ControllerTemplateBuilder {

		public ControllerTemplate buildFrom(GeneratorMetaData metaData, DomainModel domainModel) {
			ControllerTemplate template = new ControllerTemplate();
			template.updateTemplateDetails(metaData, domainModel);
			template.updateClassImports(metaData, domainModel);
			template.updateClassDetails(metaData, domainModel);
			return template;
		}
	}

}
