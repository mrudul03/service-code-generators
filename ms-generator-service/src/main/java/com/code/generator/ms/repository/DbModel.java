package com.code.generator.ms.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.code.generator.ms.model.ClassField;
import com.code.generator.ms.model.Model;
import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbModel {
	
	private String name;
	private String parentName;
	private String entityType;
	private Reference reference;
	private String tableName;
	private List<ColumnField<?>> columnFields = new ArrayList<>();
	private String databaseType;
	
	public void setReference(final Reference reference) {
		this.reference = reference;
	}
	public Reference getReference() {
		return this.reference;
	}
	public String getName() {
		return this.name;
	}
	public String getEntityType() {
		return this.entityType;
	}
	public String getParentName() {
		return this.parentName;
	}
	public String getTableName() {
		return this.tableName;
	}

	public DbModel(final Model model, String databaseType) {
		this.name = (model.getName());
		this.tableName = (model.getTableName());
		this.entityType = (model.getEntityType());
		this.columnFields = this.createColumnFields(model);
		this.databaseType = databaseType;
	}

	private List<ColumnField<?>> createColumnFields(final Model model){
		List<ColumnField<?>> columnFields = new ArrayList<>();
		
		List<ClassField<?>> nonRelationFields = model.getClassfields().parallelStream().filter(
				x -> null == x.getRelation() || x.getRelation().isEmpty())
		.collect(Collectors.toList());
		
		for(ClassField<?> classField:nonRelationFields) {
			ColumnField<?> columnField = new ColumnField<>(classField.getDatatypeClass());
			columnField.initializeColumnField(classField, model.getTableName());
			columnFields.add(columnField);
		}
		return columnFields;
	}
	
	public String getColumnScript() {
		return this.generateColumnScript();
	}
	
	public void generatePk(final Class<?> pkClazz) {
		log.info("generatePk:this.name:"+this.name);
		log.info("generatePk:this.entityType:"+this.entityType);
		log.info("generatePk:this.reference:"+this.reference);
		log.info("generatePk:this.reference:relation:"+(null != this.reference?this.reference.getRelation():""));
		
		if(this.entityType.equalsIgnoreCase(Constants.ENTITY_AGGREGATE) ||
				this.entityType.equalsIgnoreCase(Constants.REF_AGGREGATE) ||
				(null != this.reference &&
				this.reference.getRelation().equalsIgnoreCase(Constants.RELATION_ONETOMANY))) {
			
			ColumnField<?> pkField = new ColumnField<>(pkClazz);
			pkField.setName(Constants.PK_COLUMN_NAME);
			pkField.setDatatype(this.getPkDataType(pkClazz));
			pkField.setComputedDatatype(this.getPkDataType(pkClazz));
			pkField.setMaxLength(Constants.PK_MAXLENGTH);
			pkField.setNullable(false);
			pkField.setPrimaryKey(true);
			pkField.setTableName(this.name);
			this.columnFields.add(0, pkField);
			log.info("Added PK for:"+this.name);
		}
	}
	
	private String generateColumnScript() {
		String columnScript = "";
		StringBuilder sbColumnScript = new StringBuilder();
		for(ColumnField<?> column: this.columnFields) {
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
	
	public void generateReferenceColumn(final Class<?> pkClazz, final Class<?> collectionClazz) {
		if(null != this.reference) {
			ColumnField<?> refField = new ColumnField<>(pkClazz);
			refField.setName(reference.getColumnName());
			refField.setDatatype(this.getPkDataType(pkClazz));
			refField.setComputedDatatype(this.getPkDataType(pkClazz));
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
			log.info("ColumentField Collection Clazz:"+collectionClazz);
			refField.setCollectionClazz(collectionClazz);
		}
	}
	
	private boolean isPrimaryKey(String relation) {
		return relation.equalsIgnoreCase(Constants.RELATION_ONETOONE);
	}
	
	private String getPkDataType(final Class<?> clazz) {
		if(clazz.equals(String.class)) {
			return "varchar";
		} else {
			return "int";
		}
	}
}
