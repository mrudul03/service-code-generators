package com.code.generator.ms.input;

import com.code.generator.ms.util.Constants;

public class Field {
	
	private String datatype;
	private String name;
	private String maxlength;
	private String required;
	private String relation;
	
	private static final String NAME_REQUIRED = "Field should have a name.";
	private static final String DATATYPE_REQUIRED = " field should have a datatype.";
	//private static final String DATATYPE_INVALID = " field should have a valid datatype.";
	private static final String MAXLENGTH_REQUIRED = " field should have a maxlength define.";
	private static final String RELATION_REQUIRED = " field should have a relation (onetoone or onetomany) specified.";
	
	public String getDatatype() {
		return this.datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaxlength() {
		return maxlength;
	}
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
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
	public void setRelation(String relation) {
		this.relation = relation;
	}

	public boolean isPrimitive() {
		return Constants.primitiveDatatypes.contains(this.datatype.toLowerCase());
	}
	
	public ValidationResult validate() {
		this.initialize();

		ValidationResult validationResult = new ValidationResult();
		if(null == this.name || this.name.isEmpty()) {
			validationResult.add(NAME_REQUIRED);
		}
		if(null == this.datatype || this.datatype.isEmpty()) {
			validationResult.add(this.name+ DATATYPE_REQUIRED);
		}
		// check if datatype is not primitive and has not specified relation
		if(!isRelationValid()) {
			validationResult.add(this.name+ RELATION_REQUIRED);
		}
		if(!this.isSizeValid()) {
			validationResult.add(this.name+ MAXLENGTH_REQUIRED);
		}
		return validationResult;
	}
	
	private boolean isSizeValid() {
		if((null != this.datatype && 
				(this.datatype.toLowerCase().equalsIgnoreCase(Constants.STRING) ||
				this.datatype.toLowerCase().equalsIgnoreCase(Constants.BYTE) ||
				this.datatype.toLowerCase().equalsIgnoreCase(Constants.CHAR))) &&
				(null == this.maxlength || this.maxlength.isEmpty())) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private boolean isRelationValid() {
		if(null != this.datatype && 
				!Constants.primitiveDatatypes.contains(this.datatype.toLowerCase()) && 
				(null == this.relation || this.relation.isEmpty())) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public void initialize() {
		this.initializeDataType();
	}
	
	private void initializeDataType() {
		if(null != this.name) {
			this.name = name.trim();
		}
		if(null != this.datatype) {
			this.datatype = datatype.trim();
		}
		if(null != this.maxlength) {
			this.maxlength = maxlength.trim();
		}
		if(null == this.maxlength || this.maxlength.isBlank() || this.maxlength.isEmpty()) {
			this.maxlength = "50";
		}
		if(null != this.required) {
			this.required = required.trim();
		}
		if(null != this.relation) {
			this.relation = relation.trim();
		}
	}
	
	@Override
	public String toString() {
		return "Name:"+name+
				" Maxlength:"+maxlength+
				" Datatype:"+datatype+
				" Required:"+ required+
				" Relation:" + relation;
	}

}
