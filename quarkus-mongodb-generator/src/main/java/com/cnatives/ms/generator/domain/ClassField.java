package com.cnatives.ms.generator.domain;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;

import com.cnatives.ms.contract.Field;
import com.cnatives.ms.generator.base.Constants;

import lombok.Getter;

//@Data
//@Slf4j
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
	private String variableName;
	
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
	
	private void updateClassField(Field field, final String pkClazzName) {
		this.datatype = field.getDatatype();
		this.name = field.getName();
		this.maxlength = field.getMaxlength();
		this.required = field.getRequired();
		this.relation = field.getRelation();
		this.datatypeClassName = this.getDatatypeClassName(this.datatype);
		//org.bson.types.ObjectId
		this.pkType = "ObjectId";
		
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
		//this.variableName = this.getVariableName(field.getName());
	}
	
//	private String getVariableName(String name) {
//		String variableName = name;
//		if(null != name) {
//			String firstChar = name.substring(0,1);
//			variableName = firstChar.toUpperCase()+name.substring(1);
//		}
//		else {
//			variableName = "";
//		}
//		return variableName;
//	}
	
	private String getDatatypeClassName(String datatype){
		if(datatype.toLowerCase().equalsIgnoreCase(Constants.INT) || 
				datatype.toLowerCase().equalsIgnoreCase(Constants.INTEGER) ||
				datatype.toLowerCase().equalsIgnoreCase(Constants.SHORT) ||
				datatype.toLowerCase().equalsIgnoreCase(Constants.NUMBER)) {
			return (Integer.class.getSimpleName());
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.LONG)) {
			return (Long.class.getSimpleName());
		} 
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.FLOAT)) {
			return (Float.class.getSimpleName());
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.DOUBLE)) {
			return (Double.class.getSimpleName());
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.STRING)) {
			return (String.class.getSimpleName());
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.BYTE)) {
			return (Byte.class.getSimpleName());
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.CHAR)) {
			return (Character.class.getSimpleName());
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.DATE)) {
			return (Timestamp.class.getSimpleName());
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.BOOLEAN)) {
			return (Boolean.class.getSimpleName());
		}
		else if(null!= this.relation) {
				
			if(this.relation.equalsIgnoreCase(Constants.ONETOMANY)) {
				return "List<"+this.datatype+">";
			}
			else {
				return datatype;
			}
		} 
		else {
			return datatype;
		}
	}
	
	public static ClassFieldBuilder builder() {
		return new ClassFieldBuilder();
	}
	
	public static class ClassFieldBuilder {
		
		public ClassField buildFrom(final Field field, final String pkClazzName) {
			ClassField classField = new ClassField();
			classField.updateClassField(field, pkClazzName);
			return classField;
		}
		
		public ClassField createIdField(final String pkClazzName){
			ClassField classField = new ClassField();
			classField.datatype = "ObjectId";
			classField.datatypeClassName = "ObjectId";
			return classField;
		}
	}
}
