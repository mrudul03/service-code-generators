package com.cnatives.ms.generator.msservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cnatives.ms.generator.mscontroller.DomainModel;
import com.cnatives.ms.generator.mscontroller.Field;
import com.cnatives.ms.generator.msservice.base.BaseClass;
import com.cnatives.ms.generator.msservice.base.Constants;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper=true)
public class DomainTemplate extends BaseClass {
	
	private static final String DOMAIN_TEMPLATE = "domain.template";
	private static final String DOMAIN = "domain";
	private static final String FILE_EXTENSION = ".java";
	
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
	
	private DomainTemplate() {}
	
	private void updateTemplateDetails(final GeneratorMetaData metaData, DomainModel domainModel) {
		this.templateName = DOMAIN_TEMPLATE;
		//this.codeGenDirPath = metaData.getCodeGenDirPath()+DOMAIN+"/";
		this.codeGenDirPath = metaData.getServiceCodeGenDirPath(domainModel.getDomainName())+DOMAIN+"/";
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = DOMAIN;
	}
	
	private void updateClassDetails(
			final List<DomainModel> domainModels,
			final DomainModel domainModel,
			final GeneratorMetaData metaData) {
	
		//this.packageName = metaData.getBasePackage()+"."+metaData.getDomainName().toLowerCase()+"."+DOMAIN;
		//this.packageName = metaData.getBasePackage()+"."+metaData.getServiceBaseName()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
		if(metaData.getServiceBaseName().equalsIgnoreCase(domainModel.getDomainName())) {
			this.packageName = metaData.getBasePackage()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
		}
		else {
			this.packageName = metaData.getBasePackage()+"."+metaData.getServiceBaseName()+"."+domainModel.getDomainName().toLowerCase()+"."+DOMAIN;
		}
		
		this.classfields = this.createClassFields(domainModel, metaData);
		this.className = domainModel.getName();
		this.classNameVariable = this.getClassNameVariable(domainModel.getName());
		this.entityType = domainModel.getEntitytype();
		
		this.dateDeclared = this.getDateDeclared(this.classfields);
		this.setDeclared = this.getSetDeclared(this.classfields);
		this.listDeclared = this.getListDeclared(this.classfields);
		
		this.idField = this.getIdField(domainModels, domainModel, metaData.getPkClazzName());
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
			final List<DomainModel> domainModels,
			final DomainModel domainModel, 
			final String pkClazzName){
		
		ClassField classField = null;
		
		if(domainModel.getEntitytype().equalsIgnoreCase(Constants.AGGREGATE) ||
				domainModel.getEntitytype().equalsIgnoreCase(Constants.REF_AGGREGATE)) {
			classField = ClassField.builder().createIdField(pkClazzName, idColumnName);
		}
		else {
			// check if child entity's relation is onetomany
			// get aggregate domain 
			List<DomainModel> aggrDomainModels = domainModels.stream()
					.filter(z -> z.getEntitytype().equalsIgnoreCase(Constants.AGGREGATE))
					.collect(Collectors.toList());
			
			for(DomainModel aggrDomainModel:aggrDomainModels) {
				for(Field field:aggrDomainModel.getFields()) {
					
					if(field.getDatatype().equalsIgnoreCase(domainModel.getName()) &&
							null != field.getRelation() &&
							field.getRelation().equalsIgnoreCase(Constants.RELATION_ONETOMANY)) {
						
						classField = ClassField.builder().createIdField(pkClazzName, domainModel.getName());
						break;
					}
				}
				
			}
		}
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
	
	public static DomainTemplateBuilder builder() {
		return new DomainTemplateBuilder();
	}
	
	public static class DomainTemplateBuilder{
		
		public DomainTemplate buildFrom(
				final List<DomainModel> domainModels,
				final DomainModel domainModel,
				final GeneratorMetaData metaData) {
			
			DomainTemplate template = new DomainTemplate();
			template.updateClassDetails(domainModels, domainModel, metaData);
			template.updateTemplateDetails(metaData, domainModel);
			return template;
		}
	}
}
