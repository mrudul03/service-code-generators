package com.code.generator.ms.model;

import java.util.ArrayList;
import java.util.List;

import com.code.generator.ms.base.BaseClass;
import com.code.generator.ms.util.Constants;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	private Boolean isPkUuid;
	
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
	public Boolean isPkUuid() {
		return this.isPkUuid;
	}
	
	public static class Builder {
		private String resourceName;
		private String className;
		private String name;
		private String entityType;
		
		private String codeGenDirPath;
		private String packageName;
		
		private String templateDir;
		private String templateName;
		
		private List<ClassField<?>> classfields = Lists.newArrayList();
		private ClassField<?> idField;
		private String idFieldType;
		
		private String fileExtension;
		
		private Class<?> pkClazz;
		
		public Builder(final String modelName) {
			this.resourceName = modelName;
			this.fileExtension = ".java";
		}
		
		public Builder withPackageName(final String packageName) {
			this.packageName = packageName;
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
			model.classfields = this.classfields;
			model.idField = this.idField;
			model.idFieldType = this.idFieldType;
			model.fileExtension = this.fileExtension;
			model.bindingName = "model";
			model.dateDeclared = this.getDateDeclared(this.classfields);
			model.setDeclared = this.getSetDeclared(this.classfields);
			model.listDeclared = this.getListDeclared(this.classfields);
			model.tableName = this.generateTableName(this.name);
			
			if(this.entityType.equalsIgnoreCase(Constants.AGGREGATE)) {
				if(pkClazz.equals(String.class)) {
					model.generatorId = "implements GeneratedStringId";
					model.isPkUuid = Boolean.valueOf(true);
				}
				else {
					model.generatorId = "implements GeneratedNumberId";
					model.isPkUuid = null;
				}
			} else {
				model.generatorId = null;
				model.isPkUuid = null;
			}
			log.info("Built Model");
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
