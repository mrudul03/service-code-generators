package com.cnatives.ms.generator.application;

import java.util.ArrayList;
import java.util.List;

import com.cnatives.ms.contract.DomainModelForm;
import com.cnatives.ms.contract.Field;
import com.cnatives.ms.generator.base.BaseClass;
import com.cnatives.ms.generator.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper=true)
public class ContractTemplate extends BaseClass {
	
	private static final String TEMPLATE_DIR = "templates";
	private static final String CONTRACT = "contract";
	private static final String TEMPLATE = "contract.template";
	private static final String FILE_EXTENSION = ".java";
	
	@Getter
	private List<ContractClassField> classfields = new ArrayList<>();

	@Getter
	private String dateDeclared;
	
	@Getter
	private String setDeclared;
	
	@Getter
	private String listDeclared;
	
	private ContractTemplate() {}
	
	private void updateTemplateDetails(GeneratorMetaData metaData) {
		this.templateName = TEMPLATE;
		this.templateDir = TEMPLATE_DIR;
		this.bindingName = CONTRACT;
		this.fileExtension = FILE_EXTENSION;
		if(null == this.codeGenDirPath) {
			this.codeGenDirPath = metaData.getCodeGenDirPath()+CONTRACT+"/";
		}
		
	}
	
	private void updateClassDetails(GeneratorMetaData metaData, DomainModelForm domainModelform) {
		if(null == this.packageName) {
			this.packageName = metaData.getBasePackage()+"."+metaData.getDomainName().toLowerCase()+"."+CONTRACT;
		}
		
		this.className = domainModelform.getName();
		this.classfields = this.createClassFields(domainModelform, metaData);
		this.dateDeclared = this.getDateDeclared(this.classfields);
		this.setDeclared = this.getSetDeclared(this.classfields);
		this.listDeclared = this.getListDeclared(this.classfields);
	}
	
	private List<ContractClassField> createClassFields(
			final DomainModelForm domainModelform, GeneratorMetaData metaData){
		
		List<ContractClassField> classFields = new ArrayList<>();
		for(Field field:domainModelform.getFields()) {
			ContractClassField classField = ContractClassField.builder().buildFrom(field);
			classFields.add(classField);
		}
		return classFields;
	}
	
	private String getDateDeclared(List<ContractClassField> fields) {
		String dateDeclared = null;
		for(ContractClassField classField:fields) {
			if(classField.getDatatype().equalsIgnoreCase("Date") || 
					classField.getDatatype().equalsIgnoreCase("Timestamp")) {
				dateDeclared = "Date";
				break;
			}
		}
		return dateDeclared;
	}
	private String getSetDeclared(List<ContractClassField> fields) {
		String setDeclared = null;
		for(ContractClassField classField:fields) {
			if(classField.getDatatypeClassName().contains("Set")) {
				setDeclared = "Set";
				break;
			}
		}
		return setDeclared;
	}
	private String getListDeclared(List<ContractClassField> fields) {
		String setDeclared = null;
		for(ContractClassField classField:fields) {
			if(classField.getDatatypeClassName().contains("List")) {
				setDeclared = "List";
				break;
			}
		}
		return setDeclared;
	}
	
	public static ContractTemplateBuilder builder() {
		return new ContractTemplateBuilder();
	}
	
	public static class ContractTemplateBuilder {
		
		private String packageName;
		private String codeGenDirPath;
		
		public ContractTemplateBuilder withPackageName(String packageName) {
			this.packageName = packageName;
			return this;
		}
		
		public ContractTemplateBuilder withCodeGenDirPath(String codeGenDirPath) {
			this.codeGenDirPath = codeGenDirPath;
			return this;
		}
		
		public ContractTemplate buildFrom(GeneratorMetaData metaData, 
				DomainModelForm domainModelform) {
			
			ContractTemplate template = new ContractTemplate();
			template.packageName = this.packageName;
			template.codeGenDirPath = this.codeGenDirPath;
			
			template.updateTemplateDetails(metaData);
			template.updateClassDetails(metaData, domainModelform);
			return template;
		}
	}
}
