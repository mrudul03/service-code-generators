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
	private static final String FILE_EXTENSION = ".js";
	
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
	
	@Getter
	private List<DomainDto> domains = new ArrayList<>();
	
	private void updateTemplateDetails(GeneratorMetaData metaData) {
		this.templateName = TEMPLATE;
		this.templateDir = TEMPLATE_DIR;
		this.bindingName = CONTROLLER.toLowerCase();
		this.fileExtension = FILE_EXTENSION;
		this.codeGenDirPath = metaData.getCodeGenDirPath() +CONTROLLER.toLowerCase()+ "/";
	}
	
	private void updateClassImports(GeneratorMetaData metaData) {
		this.domainImports = metaData.getBasePackage() + "." + metaData.getDomainName().toLowerCase()+ "."+DOMAIN+".*";
		this.contractImports = metaData.getBasePackage() + "." + metaData.getDomainName().toLowerCase()+ "."+CONTRACT+".*";
	}
	
	private void updateClassDetails(GeneratorMetaData metaData, DomainModel domainModel, List<DomainDto> domainNames) {
		this.packageName = metaData.getBasePackage() + "." + metaData.getDomainName().toLowerCase()+ "."+CONTROLLER.toLowerCase();
		this.serviceName = metaData.getDomainName() + SERVICE;
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
		this.domains = domainNames;
	}
	
	private ControllerTemplate() {
	}

	public static ControllerTemplateBuilder builder() {
		return new ControllerTemplateBuilder();
	}

	public static class ControllerTemplateBuilder {

		public ControllerTemplate buildFrom(GeneratorMetaData metaData, 
				DomainModel domainModel, List<DomainDto> domainNames) {
			
			ControllerTemplate template = new ControllerTemplate();
			template.updateTemplateDetails(metaData);
			//template.updateClassImports(metaData);
			template.updateClassDetails(metaData, domainModel, domainNames);
			return template;
		}
	}

}
