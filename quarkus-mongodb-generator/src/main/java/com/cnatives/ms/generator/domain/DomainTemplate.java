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
import com.cnatives.ms.generator.base.Constants;
import com.cnatives.ms.generator.base.GeneratorMetaData;

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
	
	@Getter
	private List<String> postRequestNames = new ArrayList<>();
	
	@Getter
	private String contractImport;
	
	@Getter
	private String entityName;
	
	@Getter
	private String entityAnnotation;
	
	private DomainTemplate() {}
	
	private void updateTemplateDetails(final GeneratorMetaData metaData, 
			final DomainModel domainModel) {
		this.templateName = DOMAIN_TEMPLATE;
		this.codeGenDirPath = metaData.getServiceCodeGenDirPath(domainModel.getParententity())+DOMAIN+"/";
		this.fileExtension = FILE_EXTENSION;
		this.bindingName = DOMAIN;
	}
	
	private void updateClassDetails(
			final List<DomainModel> domainModels,
			final DomainModel domainModel,
			final GeneratorMetaData metaData) {
		
		this.packageName = metaData.getBasePackageName()+"."+domainModel.getParententity().toLowerCase()+"."+DOMAIN;
		this.classfields = this.createClassFields(domainModel, metaData);
		this.className = domainModel.getName();
		this.classNameVariable = this.getClassNameVariable(domainModel.getName());
		this.entityType = domainModel.getEntitytype();
		
		this.dateDeclared = this.getDateDeclared(this.classfields);
		this.setDeclared = this.getSetDeclared(this.classfields);
		this.listDeclared = this.getListDeclared(this.classfields);
		
		this.idField = this.getIdField(metaData.getPkClazzName());
		this.idFieldName = this.getClassNameVariable(domainModel.getName())+"Id";
		if(null != this.idField) {
			this.idFieldType = "ObjectId";
		}
		this.contractImport = "import "+metaData.getBasePackageName()+"."+domainModel.getParententity().toLowerCase()+"."+"contract.*";
		this.entityName = domainModel.getName()+"Entity";
	}
	
	private List<ClassField> createClassFields(final DomainModel domainModel, GeneratorMetaData metaData){
		List<ClassField> classFields = new ArrayList<>();
		
		for(Field field:domainModel.getFields()) {
			ClassField classField = ClassField.builder().buildFrom(field, metaData.getPkClazzName());
			classFields.add(classField);
		}
		return classFields;
	}
	
	private ClassField getIdField(final String pkClazzName){
		if(this.entityType.equalsIgnoreCase(Constants.AGGREGATE)) {
			ClassField classField = ClassField.builder().createIdField(pkClazzName);
			return classField;
		}
		else {
			return null;
		}
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
			template.updateTemplateDetails(metaData, domainModel);
			template.addPostRequestNames(domainModel, domainforms);
			return template;
		}
	}
}
