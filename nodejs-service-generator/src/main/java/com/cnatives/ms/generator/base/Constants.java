package com.cnatives.ms.generator.base;

import java.util.ArrayList;
import java.util.List;

public class Constants {
	
	public static final String TEMPLATES_DIR = "templates";
	public static final String PATH_SEPARATOR = "/";
	public static final String PACKAGE_SEPARATOR = ".";
	
	public static final String REQUEST = "Request";
	public static final String RESPONSE = "Response";
	public static final String ID = "id";
	
	public static final String LONG = "long";
	public static final String INT = "int";
	public static final String INTEGER = "integer";
	public static final String NUMBER = "number";
	public static final String FLOAT = "float";
	public static final String DOUBLE = "double";
	public static final String SHORT = "short";
	public static final String BOOLEAN = "boolean";
	public static final String BYTE = "byte";
	public static final String CHAR = "char";
	public static final String STRING = "string";
	public static final String TEXT = "text";
	public static final String DATE = "date";
	
	public static final String VARCHAR = "varchar";
	public static final String FLOAT8 = "float8";
	public static final String TIMESTAMP = "timestamp";
	public static final String BYTEA= "bytea";
	
	public static final String ONETOMANY = "onetomany";
	public static final String ONETOONE = "onetoone";
	public static final String AGGREGATE = "Aggregate";
	public static final String REF_AGGREGATE = "ReferenceAggregate";
	public static final String RELATION_ONETOONE = "onetoone";
	public static final String RELATION_ONETOMANY = "onetomany";
	
	public static final String ENTITY_AGGREGATE = "aggregate";
	public static final String ENTITY_ENTITY = "entity";
	public static final String ENTITY_CHILDENTITY = "childentity";
	public static final String PK_COLUMN_NAME = "id";
	public static final String PK_DATATYPE = "varchar";
	public static final int PK_MAXLENGTH = 36;
	
	public static final String COLLECTION_SET = "Set";
	public static final String COLLECTION_LIST = "List";
	
	public static final List<String> collectionTypes = new ArrayList<String>() {
		private static final long serialVersionUID = 5213247982104526418L;

		{
			add(COLLECTION_SET);
			add(COLLECTION_LIST);
		}
	};
	
	public static final List<String> primitiveDatatypes = new ArrayList<String>() {
		private static final long serialVersionUID = 5002598444488939276L;
	{
        add(LONG);
        add(INT);
        add(INTEGER);
        add(FLOAT);
        add(DOUBLE);
        add(SHORT);
        add(BOOLEAN);
        add(BYTE);
        add(CHAR);
        add(STRING);
        add(DATE);
    }};

}
