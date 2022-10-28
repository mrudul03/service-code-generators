package com.code.generator.ms.repository;

import java.sql.Timestamp;
import java.util.Date;

import com.code.generator.ms.model.ClassField;
import com.code.generator.ms.util.Constants;

public class ColumnField<T> {
	
	private static final String CAMELCASE_SPLIT_PATTERN = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
	private static final String LONG = "long";
	private static final String INT = "int";
	private static final String FLOAT = "float";
	private static final String SHORT = "short";
	private static final String DOUBLE = "double";
	private static final String BOOLEAN = "boolean";
	private static final String BYTE = "byte";
	private static final String CHAR = "char";
	private static final String STRING = "string";
	private static final String DATE = "date";
	private static final String ID = "id";
	
	private String tableName;
	private String name;
	private String datatype;
	private String computedDatatype;
	private boolean isNullable;
	private int maxLength;
	private boolean isPrimaryKey;
	private String referenceTable;
	private String referenceColumn;
	private boolean isReferencing;
	private Class<T> datatypeClass;
	private Class<?> collectionClazz;
	
	public ColumnField(Class<T> datatypeClass) {
		this.datatypeClass = datatypeClass;
	}
	
	public void initializeColumnField(final ClassField<?> classField, 
			final String tableName) {
		this.tableName = tableName;
		this.name = classField.getColumnName();
		this.datatype = computeDatatype(classField.getDatatype());
		this.computedDatatype = computedDatatype(classField.getDatatype());
		
		this.isNullable = isFieldNullable(classField.getRequired());
		if(computedDatatype.equalsIgnoreCase(Constants.VARCHAR)) {
			this.maxLength = Integer.valueOf(classField.getMaxlength());
		}
		this.isPrimaryKey = isFieldPrimaryKey(classField.getName());
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getComputedDatatype() {
		return computedDatatype;
	}
	public void setComputedDatatype(String computedDatatype) {
		this.computedDatatype = computedDatatype;
	}
	public Class<T> getDatatypeClass() {
		return datatypeClass;
	}
	public void setDatatypeClass(Class<T> datatypeClass) {
		this.datatypeClass = datatypeClass;
	}

	public boolean isNullable() {
		return isNullable;
	}
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public String getReferenceTable() {
		return referenceTable;
	}
	public void setReferenceTable(String referenceTable) {
		this.referenceTable = referenceTable;
	}
	public String getReferenceColumn() {
		return referenceColumn;
	}
	public void setReferenceColumn(String referenceColumn) {
		this.referenceColumn = referenceColumn;
	}
	public boolean isReferencing() {
		return isReferencing;
	}
	public void setReferencing(boolean isReferencing) {
		this.isReferencing = isReferencing;
	}
	public Class<?> getCollectionClazz() {
		return collectionClazz;
	}
	public void setCollectionClazz(Class<?> collectionClazz) {
		this.collectionClazz = collectionClazz;
	}
	
	private String computedDatatype(final String datatype) {
		String computedDataType = "";
		
		if(null == this.datatypeClass) {
			computedDataType = datatype;
		}
		else if(this.datatypeClass.equals(String.class)) {
			computedDataType = Constants.VARCHAR;
		}
		else if(this.datatypeClass.equals(Character.class)) {
			computedDataType = Constants.CHAR;;
		}
		else if(this.datatypeClass.equals(Short.class)) {
			computedDataType = Constants.INT;
		}
		else if(this.datatypeClass.equals(Integer.class)) {
			computedDataType = Constants.INT;
		}
		else if(this.datatypeClass.equals(Long.class)) {
			computedDataType = Constants.INT;
		}
		else if(this.datatypeClass.equals(Float.class)) {
			computedDataType = Constants.FLOAT8;
		}
		else if(this.datatypeClass.equals(Double.class)) {
			computedDataType = Constants.FLOAT8;
		}
		else if(this.datatypeClass.equals(Boolean.class)) {
			computedDataType = Constants.BOOLEAN;
		}
		else if(this.datatypeClass.equals(Date.class) || 
				this.datatypeClass.equals(Timestamp.class)) {
			computedDataType = Constants.TIMESTAMP;
		}
		else if(this.datatypeClass.equals(Byte.class)) {
			computedDataType = Constants.BYTEA;
		}
		return computedDataType;
	}
	
	private String computeDatatype(String datatype) {
		if(datatype.toLowerCase().equalsIgnoreCase(LONG) ||
				datatype.toLowerCase().equalsIgnoreCase(INT) ||
				datatype.toLowerCase().equalsIgnoreCase(FLOAT) ||
				datatype.toLowerCase().equalsIgnoreCase(SHORT) ||
				datatype.toLowerCase().equalsIgnoreCase(DOUBLE)) {
			return "int";
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(STRING) ||
				datatype.toLowerCase().equalsIgnoreCase(BOOLEAN) ||
				datatype.toLowerCase().equalsIgnoreCase(BYTE) ||
				datatype.toLowerCase().equalsIgnoreCase(CHAR)) {
			return "varchar";
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(DATE)) {
			//return "date";
			return "timestamp";
		}
		else {
			return datatype;
		}
	}
	
	private boolean isFieldNullable(String isRequired) {
		if(isRequired.equalsIgnoreCase("true")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private boolean isFieldPrimaryKey(String fieldName) {
		return fieldName.equalsIgnoreCase(ID);
	}
	
	public String getColumnSscript(String databaseType) {
		ScriptGenerator scriptGenerator = null;
		if(databaseType.equalsIgnoreCase("mysql")) {
			scriptGenerator = new MysqlScriptGenerator<T>(this);
		}
		else {
			scriptGenerator = new PostgresScriptGenerator<T>(this);
		}
		return scriptGenerator.generateColumnScript();
	}
	
	protected String getComputedColumnName() {
		StringBuilder sbCloumnName = new StringBuilder();
		for (String word : this.name.split(CAMELCASE_SPLIT_PATTERN)) {
			sbCloumnName.append(word.toLowerCase()+"_");
		}
		String columnName = "";
		if(sbCloumnName.length()>0) {
			columnName = sbCloumnName.toString().substring(0, sbCloumnName.length()-1);
		}
		return columnName;
	}
}
