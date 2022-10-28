package com.code.generator.ms.model;

import java.util.ArrayList;
import java.util.List;

import com.code.generator.ms.base.BaseClass;
import com.code.generator.ms.util.Constants;
import com.google.common.collect.Lists;

//@Slf4j
public class Model extends BaseClass {
	
	private List<ClassField<?>> classfields = Lists.newArrayList();
	private ClassField<?> idField;
	private String idFieldType;
	private String entityType;
	private String dateDeclared;
	private String setDeclared;
	private String listDeclared;
	private String tableName;
	private String generatorId;
	private Boolean pkuuidDeclared;
	private String contractPackageName;
	private String childCollectionWithStringPk;
	private String classNameVariable;
	private String idColumnName;
	private String idFieldName;
	
	private String aggregateWithStringPk;
	
	public String getDateDeclared() {
		return this.dateDeclared;
	}
	public String getSetDeclared() {
		return this.setDeclared;
	}
	public String getListDeclared() {
		return this.listDeclared;
	}
	public List<ClassField<?>> getClassfields() {
		return classfields;
	}
	public ClassField<?> getIdField() {
		return idField;
	}
	public String getIdFieldType() {
		return idFieldType;
	}
	public String getEntityType() {
		return this.entityType;
	}
	public String getTableName() {
		return this.tableName;
	}
	public String getGeneratorId() {
		return this.generatorId;
	}

	public String getContractPackageName() {
		return this.contractPackageName;
	}
	public String getAggregateDefined() {
		if(this.entityType.equalsIgnoreCase(Constants.AGGREGATE) || 
				this.entityType.equalsIgnoreCase(Constants.REF_AGGREGATE)) {
			return "Aggregate";
		}
		else {
			return null;
		}
	}
	public String getChildCollectionWithStringPk() {
		return this.childCollectionWithStringPk;
	}
	public String getAggregateWithStringPk() {
		return this.aggregateWithStringPk;
	}
	
	public Boolean getPkuuidDeclared() {
		return this.pkuuidDeclared;
	}
	
	public String getClassNameVariable() {
		return this.classNameVariable;
	}
	public String getIdColumnName() {
		return this.idColumnName;
	}
	
	public String getIdFieldName() {
		return this.idFieldName;
	}
	
	public static class Builder {
		private String resourceName;
		private String className;
		private String name;
		private String entityType;
		
		private String codeGenDirPath;
		private String packageName;
		private String contractPackageName;
		
		private String templateDir;
		private String templateName;
		
		private List<ClassField<?>> classfields = Lists.newArrayList();
		private ClassField<?> idField;
		private String idFieldType;
		
		private String fileExtension;
		
		private Class<?> pkClazz;
		private boolean childCollection;
		
		private String classNameVariable;
		private String idColumnName;
		private String idFieldName;
		
		public Builder(final String modelName) {
			this.resourceName = modelName;
			this.fileExtension = ".java";
		}
		
		public Builder withPackageName(final String packageName) {
			this.packageName = packageName;
			return this;
		}
		public Builder withContractPackageName(final String contractPackageName) {
			this.contractPackageName = contractPackageName;
			return this;
		}
		
		public Builder withCodeGenDirPath(final String codeGenDirPath) {
			this.codeGenDirPath = codeGenDirPath;
			return this;
		}
		
		public Builder withTemplateDir(String templateDir) {
			this.templateDir = templateDir;
			return this;
		}
		
		public Builder withTemplateName(String templateName) {
			this.templateName = templateName;
			return this;
		}
		
		public Builder withClassName(String className){
			this.className = className;
			this.name = className;
			return this;
		}
		public Builder withEntityType(String entityType){
			this.entityType = entityType;
			return this;
		}
		public Builder withClassFields(List<ClassField<?>> fields) {
			this.classfields = fields;
			return this;
		}
		public Builder withIdField(ClassField<?> idField) {
			this.idField = idField;
			if(null != idField) {
				this.idFieldType = idField.getDatatypeClass().getSimpleName();
			}
			return this;
		}
		
		public Builder withPkClazz(Class<?> pkClazz){
			this.pkClazz = pkClazz;
			return this;
		}
		
		public Builder withChildCollection(boolean childCollection) {
			this.childCollection = childCollection;
			return this;
		}
		
		public Builder withIdCloumnName(String idColumnName){
			this.idColumnName = idColumnName;
			return this;
		}
		public Builder withClassNameVariable(String classNameVariable){
			this.classNameVariable = classNameVariable;
			return this;
		}
		public Builder withIdFieldName(String idFieldName){
			this.idFieldName = idFieldName;
			return this;
		}
		
		public Model build() {
			Model model = new Model();
			
			if(null == this.className) {
				this.withClassName(resourceName);
			}
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			
			if(null == this.templateName) {
				this.withTemplateName("Model.template");
			}
			
			model.className = this.className;
			model.name = this.name;
			model.entityType = this.entityType;
			model.templateDir = this.templateDir;
			model.templateName = this.templateName;
			model.codeGenDirPath = this.codeGenDirPath;
			model.packageName = this.packageName;
			model.contractPackageName = this.contractPackageName;
			model.classfields = this.classfields;
			model.idField = this.idField;
			model.idFieldType = this.idFieldType;
			model.fileExtension = this.fileExtension;
			model.bindingName = "model";
			model.dateDeclared = this.getDateDeclared(this.classfields);
			model.setDeclared = this.getSetDeclared(this.classfields);
			model.listDeclared = this.getListDeclared(this.classfields);
			model.tableName = this.generateTableName(this.name);
			
			if(this.entityType.equalsIgnoreCase(Constants.AGGREGATE) || 
					this.entityType.equalsIgnoreCase(Constants.REF_AGGREGATE)) {
				if(pkClazz.equals(String.class)) {
					model.generatorId = "implements GeneratedStringId";
					model.pkuuidDeclared = Boolean.valueOf(true);
				}
				else {
					model.generatorId = "implements GeneratedNumberId";
					model.pkuuidDeclared = null;
				}
			} else {
				model.generatorId = null;
				model.pkuuidDeclared = null;
			}
			
			if(this.childCollection && 
					null != pkClazz &&
					pkClazz.equals(String.class)) {
				model.childCollectionWithStringPk = "ChildEntityWithStringPk";
			}
			else if((this.entityType.equalsIgnoreCase(Constants.AGGREGATE) ||
					this.entityType.equalsIgnoreCase(Constants.REF_AGGREGATE)) && 
					pkClazz.equals(String.class)) {
				
				model.aggregateWithStringPk = "AggregateEntityWithStringPk";
			}
			else {
				model.childCollectionWithStringPk = null;
				model.aggregateWithStringPk = null;
			}
			model.classNameVariable = this.classNameVariable;
			model.idColumnName = this.idColumnName;
			model.idFieldName = idFieldName;
			
			return model;
		}
		
		private String getDateDeclared(List<ClassField<?>> fields) {
			String dateDeclared = null;
			for(ClassField<?> classField:fields) {
				if(classField.getDatatype().equalsIgnoreCase("Date")) {
					dateDeclared = "Date";
					break;
				}
			}
			return dateDeclared;
		}
		private String getSetDeclared(List<ClassField<?>> fields) {
			String setDeclared = null;
			for(ClassField<?> classField:fields) {
				if(classField.getDatatypeClass().getName().contains("Set")) {
					setDeclared = "Set";
					break;
				}
			}
			return setDeclared;
		}
		private String getListDeclared(List<ClassField<?>> fields) {
			String setDeclared = null;
			for(ClassField<?> classField:fields) {
				if(classField.getDatatypeClass().getName().contains("List")) {
					setDeclared = "List";
					break;
				}
			}
			return setDeclared;
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
		
//		private String getClassNameVariable(String className) {
//	        String result = "";
//	        if(null != className && className.length() > 0) {
//	        	// Append first character(in lower case)
//	            char c = className.charAt(0);
//	            result = Character.toLowerCase(c)+ className.substring(1);
//	            
//	        }
//	        else {
//	        	result = "model";
//	        }
//	        log.info("ClassNameVariable as::"+result);
//	        return result;
//		}
//		
//		private String generateIdColumnName(String name) {
//			String[] words = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
//			StringBuilder sbIdColumnName = new StringBuilder();
//			for(String word : words) {
//				sbIdColumnName.append(word.toLowerCase()+"_");
//			}
//			String idColumnName = sbIdColumnName.toString().substring(0, sbIdColumnName.length()-1);
//			idColumnName = idColumnName+"_id";
//		    return idColumnName;
//		}
		
	}
	
	public List<String> getReferenceFields(){
		List<String> referenceFieldNames = new ArrayList<>();
		for(ClassField<?> classField:classfields) {
			if(!this.isPrimitive(classField.getDatatype())) {
				referenceFieldNames.add(classField.getDatatype());
			}
		}
		return referenceFieldNames;
	}
	private boolean isPrimitive(String datatype) {
		return Constants.primitiveDatatypes.contains(datatype.toLowerCase());
	}
}
