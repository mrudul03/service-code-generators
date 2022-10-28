package com.cnatives.ms.generator.dbservice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cnatives.ms.generator.msservice.base.Constants;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;
import com.cnatives.ms.generator.msservice.domain.ClassField;
import com.cnatives.ms.generator.msservice.domain.DomainTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DbModelBuilder {
	
	public List<DbModel> createDbModel(GeneratorMetaData metaData, 
			List<DomainTemplate> domainTemplates){
		
		List<DbModel> dbModels = new ArrayList<>();
//		for(DomainModel domainModel: request.getDomains()) {
//			log.info("Domain form name::"+domainModel.getName());
//			log.info("Domain form entity type::"+domainModel.getEntitytype());
//			
//			DomainTemplate domainTemplate = DomainTemplate.builder()
//					.buildFrom(request, domainModel, metaData);
//			
//			DbModel dbModel = new DbModel(domainTemplate, metaData.getDatabaseType());
//			dbModels.add(dbModel);
//		}
		
		for(DomainTemplate domainTemplate: domainTemplates) {
			DbModel dbModel = new DbModel(domainTemplate, metaData.getDatabaseType());
			dbModels.add(dbModel);
		}
		return dbModels;
		
	}
	
	public List<DbModel> initializeDbModels(final GeneratorMetaData metaData,
			final List<DbModel> dbModels, 
			final List<DomainTemplate> models) {
		
		List<DbModel> updatedDbModels = this.createReferences(dbModels, models);
		updatedDbModels.forEach(m -> m.generatePk(metaData.getPkClazzName(), m.getIdColumnName()));
		updatedDbModels.forEach(m -> m.generateReferenceColumn(metaData.getPkClazzName(), metaData.getCollectionClazzName()));
		return updatedDbModels;
	}
	
	private List<DbModel> createReferences(final List<DbModel> dbModels, final List<DomainTemplate> models){
		List<DbModel> updatedDbModels = new ArrayList<>();
		// select all aggregate db model
		updatedDbModels.addAll(this.getAggregateDbModel(dbModels));
		
		for(DomainTemplate model: models) {
			for(ClassField cField: model.getClassfields()) {
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
	
	private Reference createReference(DomainTemplate model, ClassField cField) {
		
		String targetName = model.getTableName();
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
