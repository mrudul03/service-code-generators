package com.code.generator.ms.controller;

import java.util.List;

import com.code.generator.ms.base.BaseClass;
import com.code.generator.ms.model.ClassField;
import com.google.common.collect.Lists;

public class Contract extends BaseClass{
	
	private List<ClassField<?>> classfields = Lists.newArrayList();
	private ClassField<?> idField;
	private String idFieldType;
	private String entityType;
	private String dateDeclared;
	private String setDeclared;
	private String listDeclared;
	private String modelName;
	private String modelPackageName;
	private String idColumnName;
	private String classNameVariable;
	private String idFieldName;
	
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
		return entityType;
	}
	public String getDateDeclared() {
		return this.dateDeclared;
	}
	public String getSetDeclared() {
		return this.setDeclared;
	}
	public String getListDeclared() {
		return this.listDeclared;
	}
	public String getModelName() {
		return this.modelName;
	}
	public String getModelPackageName() {
		return this.modelPackageName;
	}
	public String getModelNameVariable() {
        String result = "";
        if(null != this.modelName && this.modelName.length() > 0) {
        	// Append first character(in lower case)
            char c = this.modelName.charAt(0);
            result = Character.toLowerCase(c)+this.modelName.substring(1);
            
        }
        else {
        	result = "model";
        }
        //log.info("ClassNameVariable as::"+result);
        return result;
	}
	
	public String getIdColumnName() {
		return this.idColumnName;
	}
	public String getClassNameVariable() {
		return this.classNameVariable;
	}
	public String getIdFieldName() {
		return this.idFieldName;
	}
	public String getCapIdFieldName() {
		String result = "";
	    if(null != className && className.length() > 0) {
	    	char c = idFieldName.charAt(0);
	    	result = Character.toUpperCase(c)+ idFieldName.substring(1);
	    }
		
		return result;
	}

	public static class Builder {
		private String modelName;
		private String className;
		private String name;
		private String entityType;
		
		private String codeGenDirPath;
		private String packageName;
		
		private String templateDir;
		private String templateName;
		
		//private List<Field> fields;
		private List<ClassField<?>> classfields = Lists.newArrayList();
		private ClassField<?> idField;
		private String idFieldType;
		private String fileExtension;
		private String modelPackageName;
		private String idColumnName;
		private String classNameVariable;
		private String idFieldName;
		
		public Builder(final String modelName) {
			this.modelName = modelName;
			this.fileExtension = ".java";
		}
		
		public Builder withPackageName(final String packageName) {
			this.packageName = packageName;
			return this;
		}
		public Builder withModelPackageName(final String modelPackageName) {
			this.modelPackageName = modelPackageName;
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
		
		public Builder withEntityType(String entityType) {
			this.entityType = entityType;
			return this;
		}
		
		public Builder withClassName(String className){
			this.className = className;
			this.name = className;
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
		
		public Contract build() {
			Contract contract = new Contract();
			
			if(null == this.className) {
				this.withClassName(modelName+"Request");
			}
			if(null == this.templateDir) {
				this.withTemplateDir("templates");
			}
			
			if(null == this.templateName) {
				this.withTemplateName("Contract.template");
			}
			
			contract.className = this.className;
			contract.name = this.name;
			contract.entityType = this.entityType;
			contract.templateDir = this.templateDir;
			contract.templateName = this.templateName;
			contract.codeGenDirPath = this.codeGenDirPath;
			contract.packageName = this.packageName;
			contract.classfields = this.classfields;
			contract.idField = this.idField;
			contract.idFieldType = this.idFieldType;
			contract.fileExtension = this.fileExtension;
			contract.bindingName = "contract";
			contract.dateDeclared = this.getDateDeclared(this.classfields);
			contract.setDeclared = this.getSetDeclared(this.classfields);
			contract.listDeclared = this.getListDeclared(this.classfields);
			contract.modelName = this.modelName;
			contract.modelPackageName = this.modelPackageName;
			contract.idColumnName = this.idColumnName;
			contract.classNameVariable = this.classNameVariable;
			contract.idFieldName = idFieldName;
			return contract;
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
		
		
	}
}
