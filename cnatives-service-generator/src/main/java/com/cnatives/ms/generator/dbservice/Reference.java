package com.cnatives.ms.generator.dbservice;

import lombok.ToString;

@ToString
public class Reference {

	private String targetName;
	private String targetColumnName;
	private String columnName;
	private String relation;
	
	public Reference(final String targetName, 
			final String targetColumnName,
			final String columnName,
			final String relation) {
		
		this.targetName = targetName;
		this.targetColumnName = targetColumnName;
		this.columnName = columnName;
		this.relation = relation;
	}
	
	public String getTargetName() {
		return targetName;
	}
	public String getTargetColumnName() {
		return targetColumnName;
	}
	public String getColumnName() {
		return columnName;
	}
	public String getRelation() {
		return relation;
	}
	
	
}
