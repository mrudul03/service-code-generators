package com.cnatives.ms.generator.mscontroller;

public class Field {
	private String datatype;
	private String name;
	private String maxlength;
	private String required;
	private String relation;
	
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

	@Override
	public String toString() {
		return "Name:"+name+
				" Maxlength:"+maxlength+
				" Datatype:"+datatype+
				" Required:"+ required+
				" Relation:" + relation;
	}

}
