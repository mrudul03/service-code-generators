package com.code.generator.ms.model;

import java.awt.List;
import java.sql.Timestamp;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.code.generator.ms.util.Constants;

//@Slf4j
public class ClassField<T> {
	
	private String datatype;
	private String name;
	private String maxlength;
	private String required;
	private String relation;
	private Class<T> datatypeClass;
	private String columnName;
	private Boolean isCollection;
	private String pkType;
	private Boolean isCustom;
	
	private ClassField(final Class<T> datatypeClass) {
		this.datatypeClass = datatypeClass;
	}
	
	public String getDatatype() {
		return datatype;
	}
	public String getName() {
		return name;
	}
	public String getMaxlength() {
		return maxlength;
	}
	public String getRequired() {
		return required;
	}
	public Class<T> getDatatypeClass() {
		return datatypeClass;
	}
	public Boolean getIsCollection() {
		return isCollection;
	}
	public Boolean getIsCustom() {
		return isCustom;
	}
	public String getPkType() {
		return pkType;
	}
	public String getRelation() {
		if(null == this.relation ||
				this.relation.isEmpty()) {
			return null;
		}
		else {
			return relation;
		}
	}
	
	public String getCamelCaseDatatype() {
		String camelCaseDatatype = StringUtils.capitalize(this.datatype);
		return camelCaseDatatype;
	}
	public String getCapitalizedName() {
		return StringUtils.capitalize(this.name);
	}

	public String getCamelCaseDatatypeRequest() {
		String camelCaseRequestDatatype = StringUtils.capitalize(this.datatype)+"Request";
		return camelCaseRequestDatatype;
	}
	public String getCamelCaseDatatypeResponse() {
		String camelCaseResponseDatatype = StringUtils.capitalize(this.datatype)+"Response";
		return camelCaseResponseDatatype;
	}
	
	// used in model template for primitive and custom field
	public String getDatatypeString() {
		String computedDataType = "";
		if(null == this.datatypeClass) {
			computedDataType = StringUtils.capitalize(this.datatype);
		}
		else {
			computedDataType = datatypeClass.getSimpleName();
		}
		return computedDataType;
	}
	// used in model template for collection
	public String getModelCollectionDatatype() {
		String camelCaseDatatype = StringUtils.capitalize(this.datatype);
		String computedDataType = this.getRelationBasedDataype(camelCaseDatatype);
		return computedDataType;
	}
	
	// used on contract request and response for primitive and onetoone custom field
	public String getDatatypeStringContract() {
		String computedDataType = "";
		if(null == this.datatypeClass) {
			computedDataType = StringUtils.capitalize(this.datatype);
		}
		else {
			computedDataType = datatypeClass.getSimpleName();
		}
		return computedDataType;
	}
	
	// used for contract collection request 
	public String getCollectionDatatypeRequest() {
		String camelCaseDatatype = StringUtils.capitalize(this.datatype);
		String computedDataType = this.getRelationBasedRequestDataype(camelCaseDatatype);
		return computedDataType;
	}
	
	// used for contract collection response 
	public String getCollectionDatatypeResponse() {
		String camelCaseDatatype = StringUtils.capitalize(this.datatype);
		String computedDataType = this.getRelationBasedResponseDataype(camelCaseDatatype);
		return computedDataType;
	}
	// used for model field
	private String getRelationBasedDataype(String camelCaseDatatype) {
		if(null!= this.relation && this.relation.equalsIgnoreCase(Constants.ONETOMANY)) {
			StringBuilder sbColumn = new StringBuilder();
			if(this.datatypeClass.getSimpleName().equals("List")) {
				sbColumn.append(this.datatypeClass.getSimpleName()+"<"+camelCaseDatatype+"> "+ this.name+" = new ArrayList<>()");
			}
			else {
				sbColumn.append(this.datatypeClass.getSimpleName()+"<"+camelCaseDatatype+"> "+ this.name+" = new HashSet<>()");
			}
			
			return sbColumn.toString();
		}
		else {
			return camelCaseDatatype;
		}
	}
	// used for contract request 
	private String getRelationBasedRequestDataype(String camelCaseDatatype) {
		if(null!= this.relation && this.relation.equalsIgnoreCase(Constants.ONETOMANY)) {
			if(this.datatypeClass.getSimpleName().equals("List")) {
				return this.datatypeClass.getSimpleName()+"<"+camelCaseDatatype+Constants.REQUEST+"> "+this.name+" = new ArrayList<>()";
			}
			else {
				return this.datatypeClass.getSimpleName()+"<"+camelCaseDatatype+Constants.REQUEST+"> "+this.name+" = new HashSet<>()";
			}
		}
		else {
			return camelCaseDatatype+Constants.REQUEST;
		}
	}
	
	// used for contract response
	private String getRelationBasedResponseDataype(String camelCaseDatatype) {
		if(null!= this.relation && this.relation.equalsIgnoreCase(Constants.ONETOMANY)) {
			if(this.datatypeClass.getSimpleName().equals("List")) {
				return this.datatypeClass.getSimpleName()+"<"+camelCaseDatatype+Constants.RESPONSE+"> "+this.name+" = new ArrayList<>()";
			}
			else {
				return this.datatypeClass.getSimpleName()+"<"+camelCaseDatatype+Constants.RESPONSE+"> "+this.name+" = new HashSet<>()";
			}
		}
		else {
			return camelCaseDatatype+Constants.RESPONSE;
		}
	}
	
	public String getColumnName() {
		if(null != this.getRelation()) {
			return null;
		}
		else {
			return this.columnName;
		}
	}
	
	public String getCollectionDataType() {
		String camelCaseDatatype = StringUtils.capitalize(this.datatype);
		StringBuilder sbColumn = new StringBuilder();
			
		if(this.datatypeClass.getSimpleName().equals("List")) {
			sbColumn.append(this.datatypeClass.getSimpleName()+"<"+camelCaseDatatype+"> ");
		}
		else {
			sbColumn.append(this.datatypeClass.getSimpleName()+"<"+camelCaseDatatype+"> ");
		}
		return sbColumn.toString();
	}
	
	@Override
	public String toString() {
		return "Name:"+name+
				" Maxlength:"+maxlength+
				" Datatype:"+datatype+
				" Required:"+ required+
				" Relation:" + relation+
				" datatypeClass:"+ (null!= datatypeClass?datatypeClass.getSimpleName():"");
	}
	
	public static class Builder {
		private String datatype;
		private String name;
		private String maxlength;
		private String required;
		private String relation;
		private Class<?> pkClazz;
		private Class<?> collectionClazz;
		
		public static Builder newInstance() { 
            return new Builder(); 
        } 
		
		public Builder withDatatype(final String datatype) {
			this.datatype = datatype;
			return this;
		}
		
		public Builder withName(final String name) {
			this.name = name;
			return this;
		}
		
		public Builder withMaxlength(final String maxlength) {
			this.maxlength = maxlength;
			return this;
		}
		
		public Builder withRequired(final String required) {
			this.required = required;
			return this;
		}
		
		public Builder withRelation(final String relation) {
			this.relation = relation;
			return this;
		}
		public Builder withPkClazz(final Class<?> pkClazz) {
			this.pkClazz = pkClazz;
			return this;
		}
		public Builder withCollectionClazz(final Class<?> collectionClazz) {
			this.collectionClazz = collectionClazz;
			return this;
		}
		
		public ClassField<?> build(){
			ClassField<?> classField = this.initialize(this.datatype);
			
			classField.datatype = (this.datatype);
			classField.maxlength = (this.maxlength);
			classField.name = (this.name);
			classField.relation = (this.relation);
			classField.required = (this.required);
			classField.columnName = (this.generateColumnName(this.name));
			
			if(null!= this.relation && 
					this.relation.equalsIgnoreCase(Constants.ONETOMANY)) {
				classField.isCollection = Boolean.valueOf(true);
			}
			else {
				classField.isCollection = null;
			}
			
			if(null!= this.relation && 
					this.relation.equalsIgnoreCase(Constants.ONETOONE)) {
				classField.isCustom = Boolean.valueOf(true);
			}
			else {
				classField.isCustom = null;
			}
			
			if(this.pkClazz.equals(String.class)) {
				classField.pkType = "String";
			}
			else {
				classField.pkType = null;
			}
			return classField;
		}
		
		private ClassField<?> initialize(String datatype){
			ClassField<?> classField = null;
			
			if(datatype.toLowerCase().equalsIgnoreCase(Constants.INT) || 
					datatype.toLowerCase().equalsIgnoreCase(Constants.SHORT) ||
					datatype.toLowerCase().equalsIgnoreCase(Constants.NUMBER)) {
				classField = new ClassField<>(Integer.class);
			}
			else if(datatype.toLowerCase().equalsIgnoreCase(Constants.LONG)) {
				classField = new ClassField<>(Long.class);
			} 
			else if(datatype.toLowerCase().equalsIgnoreCase(Constants.FLOAT)) {
				classField = new ClassField<>(Float.class);
			}
			else if(datatype.toLowerCase().equalsIgnoreCase(Constants.DOUBLE)) {
				classField = new ClassField<>(Double.class);
			}
			else if(datatype.toLowerCase().equalsIgnoreCase(Constants.STRING)) {
				classField = new ClassField<>(String.class);
			}
			else if(datatype.toLowerCase().equalsIgnoreCase(Constants.BYTE)) {
				classField = new ClassField<>(Byte.class);
			}
			else if(datatype.toLowerCase().equalsIgnoreCase(Constants.CHAR)) {
				classField = new ClassField<>(Character.class);
			}
			else if(datatype.toLowerCase().equalsIgnoreCase(Constants.DATE)) {
				//classField = new ClassField<>(Date.class);
				classField = new ClassField<>(Timestamp.class);
			}
			else if(datatype.toLowerCase().equalsIgnoreCase(Constants.BOOLEAN)) {
				classField = new ClassField<>(Boolean.class);
			}
			else if(null!= this.relation && 
					this.relation.equalsIgnoreCase(Constants.ONETOMANY)) {
				
				if(this.collectionClazz.getSimpleName().equalsIgnoreCase("List")) {
					classField = new ClassField<>(List.class);
				}
				else {
					classField = new ClassField<>(Set.class);
				}
			} 
			else {
				classField = new ClassField<>(String.class);
			}
			return classField;
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
		
		public ClassField<?> createIdField(final Class<?> pkClazz, String idColumnName){
			ClassField<?> classField = new ClassField<>(pkClazz);
			classField.datatype = (this.getPkDataType(pkClazz));
			if(pkClazz.equals(String.class)) {
				classField.maxlength = ("36");
			}
			//classField.name = (Constants.ID);
			classField.name = idColumnName+"Id";
			classField.required = ("true");
			return classField;
		}
		
		private String getPkDataType(final Class<?> clazz) {
			if(clazz.equals(String.class)) {
				return "varchar";
			} else {
				return "int";
			}
		}
	}
}
