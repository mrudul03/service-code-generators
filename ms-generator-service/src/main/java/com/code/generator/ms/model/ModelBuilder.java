package com.code.generator.ms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.code.generator.ms.controller.Contract;
import com.code.generator.ms.controller.TransformModel;
import com.code.generator.ms.input.DomainModel;
import com.code.generator.ms.input.Field;
import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelBuilder {

	public List<TransformModel> toTransformModel(List<DomainModel> domainModels, 
			MsMetaData metaData){
		
		List<TransformModel> tModels = new ArrayList<>();
		for(DomainModel domainModel:domainModels) {
			List<ClassField<?>> classFields = this.createClassFields(domainModel, metaData);
			ClassField<?> idField = this.getIdField(domainModels, domainModel, metaData.getPkClazz());
			
			Contract contract= new Contract.Builder(domainModel.getName())
					.withClassName(domainModel.getName()+"Contract")
					.withEntityType(domainModel.getEntitytype())
					.withClassFields(classFields)
					.withIdField(idField)
					.withCodeGenDirPath(metaData.getCodeGenDirPath()+"contract/")
					.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".contract")
					.withTemplateDir("templates")
					.withTemplateName("Contract.template")
					.build();
			
			Model model= new Model.Builder(domainModel.getName())
					.withClassName(domainModel.getName())
					.withEntityType(domainModel.getEntitytype())
					.withClassFields(classFields)
					.withIdField(idField)
					.withCodeGenDirPath(metaData.getCodeGenDirPath()+"model/")
					.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".model")
					.withTemplateDir("templates")
					.withTemplateName("Model.template")
					.withPkClazz(metaData.getPkClazz())
					.build();
			
			TransformModel tModel = new TransformModel(model, contract);
			tModels.add(tModel);
		}
		log.info("Generated TransformModels");
		return tModels;
	}
	
	private List<ClassField<?>> createClassFields(final DomainModel domainModel, MsMetaData metaData){
		log.info("ClassField metaData Collection Clazz:"+metaData.getCollectionClazz());
		List<ClassField<?>> classFields = new ArrayList<>();
		
		for(Field field:domainModel.getFields()) {
			ClassField<?> classField = ClassField.Builder.newInstance()
					.withDatatype(field.getDatatype())
					.withMaxlength(field.getMaxlength())
					.withName(field.getName())
					.withRelation(field.getRelation())
					.withRequired(field.getRequired())
					.withPkClazz(metaData.getPkClazz())
					.withCollectionClazz(metaData.getCollectionClazz())
					.build();
			
			classFields.add(classField);
		}
		return classFields;
	}
	
	private ClassField<?> getIdField(
			final List<DomainModel> domainModels,
			final DomainModel domainModel, 
			final Class<?> pkClazz){
		
		ClassField<?> classField = null;
		
		if(domainModel.getEntitytype().equalsIgnoreCase(Constants.AGGREGATE) ||
				domainModel.getEntitytype().equalsIgnoreCase(Constants.REF_AGGREGATE)) {
			classField = new ClassField.Builder().createIdField(pkClazz);
		}
		else {
			// check if child entity's relation is onetomany
			// get aggregate domain 
			List<DomainModel> aggrDomainModels = domainModels.stream()
					.filter(z -> z.getEntitytype().equalsIgnoreCase(Constants.AGGREGATE))
					.collect(Collectors.toList());
			
			for(DomainModel aggrDomainModel:aggrDomainModels) {
				for(Field field:aggrDomainModel.getFields()) {
					
					if(field.getDatatype().equalsIgnoreCase(domainModel.getName()) &&
							null != field.getRelation() &&
							field.getRelation().equalsIgnoreCase(Constants.RELATION_ONETOMANY)) {
						
						//classField = this.createIdField(pkClazz);
						classField = new ClassField.Builder().createIdField(pkClazz);
						break;
					}
				}
				
			}
		}
		return classField;
	}
	
//	private void updateRelations(final List<DomainModel> domainModels, 
//			final List<TransformModel> tModels) {
//		
//		List<TransformModel> tAggregateModels = tModels.stream().filter(tModel -> (
//				tModel.getModel().getEntityType().equalsIgnoreCase(Constants.AGGREGATE) ||
//				tModel.getModel().getEntityType().equalsIgnoreCase(Constants.REF_AGGREGATE))
//				).collect(Collectors.toList());
//		
//		for(TransformModel tAggregateModel : tModels) {
//			log.info("----------TransformModel Name::"+tAggregateModel.getModel().getName());
//			for(ClassField<?> field: tAggregateModel.getModel().getClassfields()) {
//				log.info("------------ Transform Model fields: IsCollection::"+ field.getIsCollection());
//				log.info("------------ Transform Model fields: DatatypeClass::"+ field.getDatatypeClass());
//				log.info("------------ Transform Model fields: Name::"+ field.getName());
//				log.info("------------ Transform Model fields: DataType::"+ field.getDatatype());
//			}
//			log.info("..................................");
//		}
//		
//	}
//	
//	private TransformModel matchWithName(final List<TransformModel> tModels, final String name) {
//		TransformModel matchedModel = tModels.stream().filter(tModel -> tModel.getModel().getName().equalsIgnoreCase(name)).findFirst().get();
//		return matchedModel;
//	}
}
