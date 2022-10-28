package com.cnatives.ms.generator.application;

import java.sql.Timestamp;

import com.cnatives.ms.contract.Field;
import com.cnatives.ms.generator.base.Constants;

import lombok.Getter;


public class ContractClassField {
	
	@Getter
	private String datatype;
	
	@Getter
	private String name;
	
	@Getter
	private String required;
	
	@Getter
	private String relation;
	
	@Getter
	private String datatypeClassName;
	
	private ContractClassField() {}
	
	@Override
	public String toString() {
		return "Name:"+name+
				" Datatype:"+datatype+
				" Required:"+ required+
				" Relation:" + relation+
				" datatypeClass:"+ datatypeClassName;
	}
	
	private void updateClassFieldDetails(final Field field) {
		
		this.datatype = field.getDatatype();
		this.name = field.getName();
		this.required = field.getRequired();
		this.relation = field.getRelation();
		this.datatypeClassName = this.getDatatypeClassName(datatype);
	}
	
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
	
	
	public static ContractClassFieldBuilder builder() {
		return new ContractClassFieldBuilder();
	}
	
	public static class ContractClassFieldBuilder {
		
		public ContractClassField buildFrom(final Field field) {
			
			ContractClassField classField = new ContractClassField();
			classField.updateClassFieldDetails(field);
			return classField;
		}
	}
}
