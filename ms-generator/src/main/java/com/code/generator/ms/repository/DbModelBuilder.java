package com.code.generator.ms.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.model.ClassField;
import com.code.generator.ms.model.Model;
import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbModelBuilder {
	
	public List<DbModel> toDbModels(final List<Model> models, final MsMetaData metaData){
		
		log.info("DbModelBuilder::toDbModels::Got Models size:"+models.size());
		List<DbModel> dbModels = new ArrayList<>();
		for(Model model: models) {
			DbModel dbModel = new DbModel(model, metaData.getDatabaseType());
			dbModels.add(dbModel);
		}
		return dbModels;
	}
	
	public List<DbModel> initializeDbModels(final MsMetaData metaData,
			final List<DbModel> dbModels, 
			final List<Model> models) {
		
		List<DbModel> updatedDbModels = this.createReferences(dbModels, models);
		updatedDbModels.forEach(m -> m.generatePk(metaData.getPkClazz(), m.getIdColumnName()));
		updatedDbModels.forEach(m -> m.generateReferenceColumn(metaData.getPkClazz(), metaData.getCollectionClazz()));
		return updatedDbModels;
	}
	
	private List<DbModel> createReferences(final List<DbModel> dbModels, final List<Model> models){
		List<DbModel> updatedDbModels = new ArrayList<>();
		// select all aggregate db model
		updatedDbModels.addAll(this.getAggregateDbModel(dbModels));
		for(Model model:models) {
			for(ClassField<?> cField:model.getClassfields()) {
				if(!this.isPrimitive(cField.getDatatype())) {
					// get matching Db Model and add reference
					String modelDataType = cField.getDatatype();
					DbModel dbModel = dbModels.stream()
							.filter(dm -> dm.getName().equalsIgnoreCase(modelDataType))
							.findFirst()
							.orElse(null);
					log.info("Got Db Model Match for creating references:"+dbModel);
					Reference reference = this.createReference(model, cField);
					dbModel.setReference(reference);
					updatedDbModels.add(dbModel);
				}
			}
		}
		return updatedDbModels;
	}
	
	private List<DbModel> getAggregateDbModel(final List<DbModel> dbModels) {
		List<DbModel> aggDbModels = dbModels.stream()
				.filter(dm -> (dm.getEntityType().equalsIgnoreCase(Constants.AGGREGATE) ||
						dm.getEntityType().equalsIgnoreCase(Constants.REF_AGGREGATE)))
				.collect(Collectors.toList());
		return aggDbModels;
	}
	
	private boolean isPrimitive(String datatype) {
		return Constants.primitiveDatatypes.contains(datatype.toLowerCase());
	}
	
	private Reference createReference(Model model, ClassField<?> cField) {
		String targetName = model.getTableName();
		//String targetColumnName = "id";
		String targetColumnName = model.getIdColumnName();
		
		String columnName = model.getTableName();
		String relation = cField.getRelation();
		
		Reference reference = new Reference(
				targetName,
				targetColumnName,
				columnName,
				relation);
		return reference;
	}

}
