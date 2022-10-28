package com.code.generator.ms.model;

import java.awt.List;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClassField<T> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassField.class);
	
	private String datatype;
	private String name;
	private String maxlength;
	private String required;
	private String relation;
	private Class<T> datatypeClass;
	private String columnName;
	private Boolean isCollection;
	private String pkType;
	
	private static final String ONETOMANY = "onetomany";
	private static final String CONTRACT = "Contract";
	
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
	public String getCamelCaseContractDatatype() {
		String camelCaseContractDatatype = StringUtils.capitalize(this.datatype)+Constants.CONTRACT;
		return camelCaseContractDatatype;
	}
	
	public String getDatatypeString() {
		String computedDataType = "";
		if(null == this.datatypeClass) {
			computedDataType = StringUtils.capitalize(this.datatype);
		}
		if(null != this.relation && !this.relation.isEmpty()) {
			String camelCaseDatatype = StringUtils.capitalize(this.datatype);
			computedDataType = this.getRelationBasedDataype(camelCaseDatatype);
		} 
		else {
			computedDataType = datatypeClass.getSimpleName();
		}
		//LOGGER.info("computedDataType:"+computedDataType);
		return computedDataType;
	}
	
	public String getDatatypeStringContract() {
		String computedDataType = "";
		if(null == this.datatypeClass) {
			computedDataType = StringUtils.capitalize(this.datatype);
		}
		if(null != this.relation && !this.relation.isEmpty()) {
			String camelCaseDatatype = StringUtils.capitalize(this.datatype);
			computedDataType = this.getRelationBasedDataypeContract(camelCaseDatatype);
		} 
		else {
			computedDataType = datatypeClass.getSimpleName();
		}
		//LOGGER.info("computedDataType:"+computedDataType);
		return computedDataType;
	}
	
	private String getRelationBasedDataype(String camelCaseDatatype) {
		if(null!= this.relation && this.relation.equalsIgnoreCase(ONETOMANY)) {
			StringBuilder sbColumn = new StringBuilder();
			//sbColumn.append("@Builder.Default"+"\n");
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
	
	private String getRelationBasedDataypeContract(String camelCaseDatatype) {
		if(null!= this.relation && this.relation.equalsIgnoreCase(ONETOMANY)) {
			if(this.datatypeClass.getSimpleName().equals("List")) {
				return this.datatypeClass.getSimpleName()+"<"+camelCaseDatatype+CONTRACT+"> "+this.name+" = new ArrayList<>()";
			}
			else {
				return this.datatypeClass.getSimpleName()+"<"+camelCaseDatatype+CONTRACT+"> "+this.name+" = new HashSet<>()";
			}
		}
		else {
			return camelCaseDatatype+CONTRACT;
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
			log.info("Build Classfield CollectionClazz----------"+this.collectionClazz);
			ClassField<?> classField = this.initialize(this.datatype);
			
			classField.datatype = (this.datatype);
			classField.maxlength = (this.maxlength);
			classField.name = (this.name);
			classField.relation = (this.relation);
			classField.required = (this.required);
			classField.columnName = (this.generateColumnName(this.name));
			if(null!= this.relation && 
					this.relation.equalsIgnoreCase(ONETOMANY)) {
				classField.isCollection = Boolean.valueOf(true);
			}
			else {
				classField.isCollection = null;
			}
			if(this.pkClazz.equals(String.class)) {
				classField.pkType = "String";
			}
			else {
				classField.pkType = null;
			}
			
			LOGGER.info("");
			return classField;
		}
		
		
		private ClassField<?> initialize(String datatype){
			ClassField<?> classField = null;
			
			if(datatype.toLowerCase().equalsIgnoreCase(Constants.INT) || 
					datatype.toLowerCase().equalsIgnoreCase(Constants.SHORT)) {
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
				classField = new ClassField<>(Date.class);
			}
			else if(datatype.toLowerCase().equalsIgnoreCase(Constants.BOOLEAN)) {
				classField = new ClassField<>(Boolean.class);
			}
			else if(null!= this.relation && 
					this.relation.equalsIgnoreCase(ONETOMANY)) {
				
				log.info("ClassField -------------initialize-------collectionClazz.class-------:"+this.collectionClazz.getClass());
				log.info("ClassField -------------initialize-------collectionClazz.name-------:"+this.collectionClazz.getSimpleName());
				if(this.collectionClazz.getSimpleName().equalsIgnoreCase("List")) {
					classField = new ClassField<>(List.class);
					log.info("---------------- matched collection clazz");
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
		
		public ClassField<?> createIdField(final Class<?> pkClazz){
			ClassField<?> classField = new ClassField<>(pkClazz);
			classField.datatype = (this.getPkDataType(pkClazz));
			if(pkClazz.equals(String.class)) {
				classField.maxlength = ("36");
			}
			classField.name = (Constants.ID);
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
