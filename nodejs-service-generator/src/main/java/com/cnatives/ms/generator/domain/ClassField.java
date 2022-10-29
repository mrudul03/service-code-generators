package com.cnatives.ms.generator.domain;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;

import com.cnatives.ms.contract.Field;
import com.cnatives.ms.generator.base.Constants;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

//@Data
@Slf4j
public class ClassField {
	
	@Getter
	private String datatype;
	
	@Getter
	private String name;
	
	@Getter
	private String maxlength;
	
	@Getter
	private String required;
	
	@Getter
	private String relation;
	
	@Getter
	private String datatypeClassName;
	
	@Getter
	private Boolean isCollection;
	
	@Getter
	private String pkType;
	
	@Getter
	private Boolean isCustom;
	
	@Getter
	private Boolean isValidDatatype;
	
	
	private String columnName;
	public String getColumnName() {
		if(null != this.getRelation()) {
			log.info("Reation::"+this.getRelation());
			
			return null;
		}
		else {
			return this.columnName;
		}
	}
	
	@Getter
	private Reference reference;
	public void setReference(Reference reference) {
		this.reference = reference;
	}
	
	private ClassField() {}
	
	public String getCapitalizedName() {
		return StringUtils.capitalize(this.name);
	}
	
	
	@Override
	public String toString() {
		return "Name:"+name+
				" Maxlength:"+maxlength+
				" Datatype:"+datatype+
				" Required:"+ required+
				" Relation:" + relation+
				" datatypeClass:"+ datatypeClassName;
	}
	
	private void updateClassField(Field field, final String pkClazzName, final String databaseType) {
		this.datatype = field.getDatatype();
		this.name = field.getName();
		this.maxlength = field.getMaxlength();
		this.required = field.getRequired();
		this.relation = field.getRelation();
		//this.pkClazz = pkClazz;
		
		if(databaseType.equals("postgres") || 
				databaseType.equals("postgressql") ||
				databaseType.equals("mysql")) {
			this.columnName = this.generateColumnName(this.name);
		}
		else {
			this.columnName = null;
		}
		
		this.datatypeClassName = this.getDatatypeClassName(this.datatype);
		
		if(null!= this.relation && 
				this.relation.equalsIgnoreCase(Constants.ONETOMANY)) {
			this.isCollection = Boolean.valueOf(true);
		}
		else {
			this.isCollection = null;
		}
		
		if(null!= this.relation && 
				this.relation.equalsIgnoreCase(Constants.ONETOONE)) {
			this.isCustom = Boolean.valueOf(true);
		}
		else {
			this.isCustom = null;
		}
		
		if(pkClazzName.toLowerCase().equals(Constants.STRING)) {
			this.pkType = "String";
		}
		else {
			this.pkType = null;
		}
		log.info("::this.name::"+this.name);
		log.info("::this.isCollection::"+this.isCollection);
		log.info("::this.isCustom::"+this.isCustom);
		if(null == this.isCollection && null == this.isCustom) {
			isValidDatatype = Boolean.valueOf(true);
		}
		else {
			isValidDatatype = null;
		}
	}
	
	private String getDatatypeClassName(String datatype){
		if(datatype.toLowerCase().equalsIgnoreCase(Constants.INT) || 
				datatype.toLowerCase().equalsIgnoreCase(Constants.INTEGER) ||
				datatype.toLowerCase().equalsIgnoreCase(Constants.SHORT) ||
				datatype.toLowerCase().equalsIgnoreCase(Constants.NUMBER)) {
			return "INTEGER";
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.LONG)) {
			return (Long.class.getSimpleName());
		} 
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.FLOAT)) {
			return "FLOAT";
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.DOUBLE)) {
			return "DOUBLE";
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.STRING)) {
			return "STRING";
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.BYTE)) {
			return "STRING.BINARY";
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.CHAR)) {
			return "STRING";
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.DATE)) {
			return "DATE";
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.BOOLEAN)) {
			return "BOOLEAN";
		}
//		else if(null!= this.relation) {
//				
//			if(this.relation.equalsIgnoreCase(Constants.ONETOMANY)) {
//				return "List<"+this.datatype+">";
//			}
//			else {
//				return datatype;
//			}
//		} 
		else {
			return datatype;
		}
	}
	
	private String generateColumnName(String name) {
		String[] words = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
		StringBuilder sbCloumnName = new StringBuilder();
		for(String word : words) {
			sbCloumnName.append(word.toLowerCase()+"_");
		}
		String columnName = sbCloumnName.toString().substring(0, sbCloumnName.length()-1);
	    return columnName;
	}
	
	public static ClassFieldBuilder builder() {
		return new ClassFieldBuilder();
	}
	
	public static class ClassFieldBuilder {
		
		public ClassField buildFrom(final Field field, final String pkClazzName, final String databaseType) {
			ClassField classField = new ClassField();
			classField.updateClassField(field, pkClazzName, databaseType);
			return classField;
		}
		
		public ClassField createIdField(final String pkClazzName, String idColumnName){
			ClassField classField = new ClassField();
			classField.datatype = (this.getPkDataType(pkClazzName));
			classField.datatypeClassName = this.getPkDataType(pkClazzName);
			if(pkClazzName.toLowerCase().equals(Constants.STRING)) {
				classField.maxlength = ("36");
			}
			classField.name = idColumnName+"Id";
			classField.required = ("true");
			return classField;
		}
		
		private String getPkDataType(final String clazzName) {
			if(clazzName.toLowerCase().equals(Constants.STRING)) {
				//return "varchar";
				return "Strig";
			} else {
				//return "int";
				return "Long";
			}
		}
	}
}
