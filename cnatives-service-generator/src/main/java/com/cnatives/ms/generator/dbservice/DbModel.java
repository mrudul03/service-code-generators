package com.cnatives.ms.generator.dbservice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cnatives.ms.generator.msservice.base.Constants;
import com.cnatives.ms.generator.msservice.domain.ClassField;
import com.cnatives.ms.generator.msservice.domain.DomainTemplate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbModel {
	
	@Getter
	private String name;
	
	@Getter
	private String parentName;
	
	@Getter
	private String entityType;
	
	@Getter
	private Reference reference;
	
	@Getter
	private String tableName;
	
	@Getter
	private List<ColumnField> columnFields = new ArrayList<>();
	
	@Getter
	private String databaseType;
	
	@Getter
	private String idColumnName;
	
	public void setReference(final Reference reference) {
		this.reference = reference;
	}
	
	public DbModel(final DomainTemplate model, String databaseType) {
		this.name = model.getClassName();
		this.idColumnName = model.getIdColumnName();
		this.tableName = this.generateTableName(model.getClassName());
		this.entityType = model.getEntityType();
		this.columnFields = this.createColumnFields(model, this.tableName);
		this.databaseType = databaseType;
	}
	
	private List<ColumnField> createColumnFields(
			final DomainTemplate model,
			final String tableName){
		
		List<ColumnField> columnFields = new ArrayList<>();
		
		List<ClassField> nonRelationFields = model.getClassfields().parallelStream().filter(
				x -> null == x.getRelation() || x.getRelation().isEmpty())
		.collect(Collectors.toList());
		
		for(ClassField classField:nonRelationFields) {
			ColumnField columnField = new ColumnField(classField.getDatatypeClassName());
			columnField.initializeColumnField(classField, tableName);
			columnFields.add(columnField);
		}
		return columnFields;
	}
	
	public String getColumnScript() {
		return this.generateColumnScript();
	}
	
	public void generatePk(final String pkClazzName, String idColumnName) {
		log.info("generatePk:this.name:"+this.name);
		log.info("generatePk:this.entityType:"+this.entityType);
		log.info("generatePk:this.reference:"+this.reference);
		log.info("generatePk:this.reference:relation:"+(null != this.reference?this.reference.getRelation():""));
		
		if(this.entityType.equalsIgnoreCase(Constants.ENTITY_AGGREGATE) ||
				this.entityType.equalsIgnoreCase(Constants.REF_AGGREGATE) ||
				(null != this.reference &&
				this.reference.getRelation().equalsIgnoreCase(Constants.RELATION_ONETOMANY))) {
			
			ColumnField pkField = new ColumnField(pkClazzName);
			//pkField.setName(Constants.PK_COLUMN_NAME);
			pkField.setName(this.generateIdColumnName(idColumnName));
			pkField.setDatatype(this.getPkDataType(pkClazzName));
			pkField.setComputedDatatype(this.getPkDataType(pkClazzName));
			pkField.setMaxLength(Constants.PK_MAXLENGTH);
			pkField.setNullable(false);
			pkField.setPrimaryKey(true);
			pkField.setTableName(this.name);
			this.columnFields.add(0, pkField);
			log.info("Added PK for:"+this.name);
		}
	}
	
	private String generateTableName(String name) {
		String[] words = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
		StringBuilder sbTableName = new StringBuilder();
		for(String word : words) {
			sbTableName.append(word.toLowerCase()+"_");
		}
		String tableName = sbTableName.toString().substring(0, sbTableName.length()-1);
	    return tableName;
	}
	
	private String generateIdColumnName(String name) {
		String[] words = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
		StringBuilder sbIdColumnName = new StringBuilder();
		for(String word : words) {
			sbIdColumnName.append(word.toLowerCase()+"_");
		}
		String idColumnName = sbIdColumnName.toString().substring(0, sbIdColumnName.length()-1);
	    return idColumnName;
	}
	
	private String generateColumnScript() {
		String columnScript = "";
		StringBuilder sbColumnScript = new StringBuilder();
		for(ColumnField column: this.columnFields) {
			sbColumnScript.append(column.getColumnSscript(this.databaseType)+"\n");
		}
		if(sbColumnScript.length() > 0) {
			int length = sbColumnScript.toString().length();
			columnScript = sbColumnScript.toString().substring(0, (length-2));
		}
		else {
			columnScript = sbColumnScript.toString();
		}
		return columnScript;
	}
	
	public void generateReferenceColumn(final String pkClazzName, final String collectionClazzName) {
		if(null != this.reference) {
			ColumnField refField = new ColumnField(pkClazzName);
			refField.setName(reference.getColumnName());
			refField.setDatatype(this.getPkDataType(pkClazzName));
			refField.setComputedDatatype(this.getPkDataType(pkClazzName));
			refField.setMaxLength(Constants.PK_MAXLENGTH);
			refField.setPrimaryKey(this.isPrimaryKey(reference.getRelation()));
			refField.setReferenceColumn(reference.getTargetColumnName());
			refField.setReferenceTable(reference.getTargetName());
			refField.setReferencing(true);
			if(this.isPrimaryKey(reference.getRelation())) {
				this.columnFields.add(0, refField);
			}
			else {
				this.columnFields.add(1, refField);
			}
			refField.setCollectionClazzName(collectionClazzName);
		}
	}
	
	private boolean isPrimaryKey(String relation) {
		return relation.equalsIgnoreCase(Constants.RELATION_ONETOONE);
	}
	
	private String getPkDataType(final String clazzName) {
		if(clazzName.toLowerCase().equals(Constants.STRING)) {
			return "varchar";
		} else {
			return "int";
		}
	}
	

}
