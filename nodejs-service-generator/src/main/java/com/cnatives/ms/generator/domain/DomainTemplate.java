package com.cnatives.ms.generator.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cnatives.ms.contract.DomainModel;
import com.cnatives.ms.contract.DomainModelForm;
import com.cnatives.ms.contract.DomainModelOperation;
import com.cnatives.ms.contract.Field;
import com.cnatives.ms.generator.base.BaseClass;
import com.cnatives.ms.generator.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper=true)
public class DomainTemplate extends BaseClass {
	
	private static final String DOMAIN_TEMPLATE = "domain.template";
	private static final String DOMAIN = "domain";
	private static final String FILE_EXTENSION = ".js";
	
	@Getter
	private List<ClassField> classfields = new ArrayList<>();
	
	@Getter
	private ClassField idField;
	
	@Getter
	private String dateDeclared;
	
	@Getter
	private String setDeclared;
	
	@Getter
	private String listDeclared;
	
	@Getter
	private String classNameVariable;
	
	@Getter
	private String entityType;
	
	@Getter
	private String idFieldType;
	
	@Getter
	private String idFieldName;
	
	@Getter
	private String idColumnName;
	
	@Getter
	private String tableName;
	
	@Getter
	private List<String> postRequestNames = new ArrayList<>();
	
	private DomainTemplate() {}
	
	private void updateTemplateDetails(final GeneratorMetaData metaData) {
		this.templateName = DOMAIN_TEMPLATE;
		this.codeGenDirPath = metaData.getCodeGenDirPath()+"models/";
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = DOMAIN;
	}
	
	private void updateClassDetails(
			final List<DomainModel> domainModels,
			final DomainModel domainModel,
			final GeneratorMetaData metaData) {
		
		//this.packageName = metaData.getBasePackage()+"."+metaData.getDomainName().toLowerCase()+"."+DOMAIN;
		this.classfields = this.createClassFields(domainModel, metaData);
		this.className = domainModel.getName();
		this.classNameVariable = this.getClassNameVariable(domainModel.getName());
		this.entityType = domainModel.getEntitytype();
		
		this.dateDeclared = this.getDateDeclared(this.classfields);
		this.setDeclared = this.getSetDeclared(this.classfields);
		this.listDeclared = this.getListDeclared(this.classfields);
		
		this.idField = this.getIdField(metaData.getPkClazzName());
		this.idColumnName = this.generateIdColumnName(domainModel.getName());
		this.idFieldName = this.getClassNameVariable(domainModel.getName())+"Id";
		if(null != this.idField) {
			this.idFieldType = this.idField.getDatatypeClassName();
		}
		this.tableName = this.generateTableName(domainModel.getName());
	}
	
	private String generateTableName(String name) {
		String[] words = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
		StringBuilder sbTableName = new StringBuilder();
		for(String word : words) {
			sbTableName.append(word.toLowerCase()+"_");
		}
		String tableName = sbTableName.toString().substring(0, sbTableName.length()-1);
	    return tableName;
	}
	
	private String generateIdColumnName(String name) {
		String[] words = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
		StringBuilder sbIdColumnName = new StringBuilder();
		for(String word : words) {
			sbIdColumnName.append(word.toLowerCase()+"_");
		}
		String idColumnName = sbIdColumnName.toString().substring(0, sbIdColumnName.length()-1);
		idColumnName = idColumnName+"_id";
	    return idColumnName;
	}
	
	private List<ClassField> createClassFields(final DomainModel domainModel, GeneratorMetaData metaData){
		List<ClassField> classFields = new ArrayList<>();
		
		for(Field field:domainModel.getFields()) {
			ClassField classField = ClassField.builder().buildFrom(field, metaData.getPkClazzName(), metaData.getDatabaseType());
			classFields.add(classField);
		}
		return classFields;
	}
	
	private ClassField getIdField(
			//final List<DomainModel> domainModels,
			//final DomainModel domainModel, 
			final String pkClazzName){
		
		ClassField classField = ClassField.builder().createIdField(pkClazzName, idColumnName);
		return classField;
	}
	
	private String getClassNameVariable(String className) {
        String result = "";
        if(null != className && className.length() > 0) {
        	// Append first character(in lower case)
            char c = className.charAt(0);
            result = Character.toLowerCase(c)+ className.substring(1);
        }
        else {
        	result = "model";
        }
        //log.info("ClassNameVariable as::"+result);
        return result;
	}
	
	private String getDateDeclared(List<ClassField> fields) {
		String dateDeclared = null;
		for(ClassField classField:fields) {
			if(classField.getDatatype().equalsIgnoreCase("Date")) {
				dateDeclared = "Date";
				break;
			}
		}
		return dateDeclared;
	}
	private String getSetDeclared(List<ClassField> fields) {
		String setDeclared = null;
		for(ClassField classField:fields) {
			if(classField.getDatatypeClassName().contains("Set")) {
				setDeclared = "Set";
				break;
			}
		}
		return setDeclared;
	}
	private String getListDeclared(List<ClassField> fields) {
		String setDeclared = null;
		for(ClassField classField:fields) {
			if(classField.getDatatypeClassName().contains("List")) {
				setDeclared = "List";
				break;
			}
		}
		return setDeclared;
	}
	
	private List<DomainModelOperation> getPostOperations(final DomainModel domainModel){
		List<DomainModelOperation> postOps = domainModel.getOperations()
				.stream()
				.filter(op -> op.getMethod().equalsIgnoreCase("POST")).collect(Collectors.toList());
		return postOps;
	}
	
	private void addPostRequestNames(final DomainModel domainModel,
			final List<DomainModelForm> domainforms) {
		List<DomainModelOperation> postOps = this.getPostOperations(domainModel);
		if(postOps.size() > 0) {
			List<String> postRequestNames = new ArrayList<>();
			for(DomainModelOperation operation: postOps) {
				if(null != operation.getBody()) {
					String requestName = operation.getBody().getType();
					postRequestNames.add(requestName);
				}
			}
			this.postRequestNames.addAll(postRequestNames);
		}
		else {
			// get matching request form
			DomainModelForm domainModelForm = this.getMatchedDomainModelForm(
					domainforms, this.className);
			if(null != domainModelForm) {
				this.postRequestNames.add(domainModelForm.getName());
			}
		}
	}
	
	private DomainModelForm getMatchedDomainModelForm(
			List<DomainModelForm> domainforms,
			String className){
		
		Optional<DomainModelForm> formOptional = domainforms.stream()
				.filter(form -> form.getName().contains(className)).findFirst();
		if(formOptional.isPresent()) {
			return formOptional.get();
		}
		else {
			return null;
		}
		
	}
	
	public static DomainTemplateBuilder builder() {
		return new DomainTemplateBuilder();
	}
	
	public static class DomainTemplateBuilder{
		
		public DomainTemplate buildFrom(
				final List<DomainModel> domainModels,
				final DomainModel domainModel,
				final GeneratorMetaData metaData,
				final List<DomainModelForm> domainforms) {
			
			DomainTemplate template = new DomainTemplate();
			template.updateClassDetails(domainModels, domainModel, metaData);
			template.updateTemplateDetails(metaData);
			template.addPostRequestNames(domainModel, domainforms);
			return template;
		}
	}
	
	
}
