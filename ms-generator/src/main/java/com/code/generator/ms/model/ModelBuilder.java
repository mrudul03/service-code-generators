package com.code.generator.ms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.code.generator.ms.controller.Contract;
import com.code.generator.ms.input.DomainModel;
import com.code.generator.ms.input.Field;
import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelBuilder {

	public List<ModelHolder> toTransformModel(List<DomainModel> domainModels, 
			MsMetaData metaData){
		
		List<ModelHolder> modelHolders = new ArrayList<>();
		for(DomainModel domainModel:domainModels) {
			
			String idColumnName = this.generateIdColumnName(domainModel.getName());
			String classNameVariable = this.getClassNameVariable(domainModel.getName());
			String idFieldName = classNameVariable+"Id";
			
			List<ClassField<?>> classFields = this.createClassFields(domainModel, metaData);
			
			ClassField<?> idField = this.getIdField(domainModels, domainModel, metaData.getPkClazz());
			boolean childCollection = this.isChildCollection(domainModels, domainModel);
			
			Contract contractRequest = new Contract.Builder(domainModel.getName())
					.withClassName(domainModel.getName()+"Request")
					.withClassNameVariable(classNameVariable)
					.withEntityType(domainModel.getEntitytype())
					.withClassFields(classFields)
					.withIdField(idField)
					.withIdCloumnName(idColumnName)
					.withIdFieldName(idFieldName)
					.withCodeGenDirPath(metaData.getCodeGenDirPath()+"contract/")
					.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".contract")
					.withModelPackageName("com.service."+metaData.getResourceName().toLowerCase()+".domain")
					.withTemplateDir("templates")
					.withTemplateName("ContractRequest.template")
					.build();
			
			Contract contractResponse = new Contract.Builder(domainModel.getName())
					.withClassName(domainModel.getName()+"Response")
					.withClassNameVariable(classNameVariable)
					.withEntityType(domainModel.getEntitytype())
					.withClassFields(classFields)
					.withIdField(idField)
					.withIdCloumnName(idColumnName)
					.withIdFieldName(idFieldName)
					.withCodeGenDirPath(metaData.getCodeGenDirPath()+"contract/")
					.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".contract")
					.withModelPackageName("com.service."+metaData.getResourceName().toLowerCase()+".domain")
					.withTemplateDir("templates")
					.withTemplateName("ContractResponse.template")
					.build();
			
			Model model= new Model.Builder(domainModel.getName())
					.withClassName(domainModel.getName())
					.withClassNameVariable(classNameVariable)
					.withEntityType(domainModel.getEntitytype())
					.withClassFields(classFields)
					.withIdField(idField)
					.withIdCloumnName(idColumnName)
					.withIdFieldName(idFieldName)
					.withCodeGenDirPath(metaData.getCodeGenDirPath()+"domain/")
					.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".domain")
					.withContractPackageName("com.service."+metaData.getResourceName().toLowerCase()+".contract")
					.withTemplateDir("templates")
					.withTemplateName("Model.template")
					.withPkClazz(metaData.getPkClazz())
					.withChildCollection(childCollection)
					.build();
			
			ModelHolder modelHolder = new ModelHolder(model, contractRequest, contractResponse);
			modelHolders.add(modelHolder);
		}
		log.info("Generated ModelHolders");
		return modelHolders;
	}
	
	private List<ClassField<?>> createClassFields(final DomainModel domainModel, MsMetaData metaData){
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
			classField = new ClassField.Builder().createIdField(pkClazz, domainModel.getName());
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
						classField = new ClassField.Builder().createIdField(pkClazz, domainModel.getName());
						break;
					}
				}
				
			}
		}
		return classField;
	}
	
	private boolean isChildCollection(final List<DomainModel> domainModels,
			final DomainModel domainModel) {
		
		boolean isChildAndCollection = false;
		List<DomainModel> aggrDomainModels = domainModels.stream()
				.filter(z -> z.getEntitytype().equalsIgnoreCase(Constants.AGGREGATE))
				.collect(Collectors.toList());
		
		for(DomainModel aggrDomainModel:aggrDomainModels) {
			for(Field aggrModelField:aggrDomainModel.getFields()) {
				
				if(aggrModelField.getDatatype().equalsIgnoreCase(domainModel.getName()) &&
						null != aggrModelField.getRelation() &&
						aggrModelField.getRelation().equalsIgnoreCase(Constants.RELATION_ONETOMANY)) {
					
					isChildAndCollection = true;
					break;
				}
			}
			
		}
		return isChildAndCollection;
	}
	
	private String generateIdColumnName(String name) {
		String[] words = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
		StringBuilder sbIdColumnName = new StringBuilder();
		for(String word : words) {
			sbIdColumnName.append(word.toLowerCase()+"_");
		}
		String idColumnName = sbIdColumnName.toString().substring(0, sbIdColumnName.length()-1);
		idColumnName = idColumnName+"_id";
	    return idColumnName;
	}
	
	private String getClassNameVariable(String className) {
        String result = "";
        if(null != className && className.length() > 0) {
        	// Append first character(in lower case)
            char c = className.charAt(0);
            result = Character.toLowerCase(c)+ className.substring(1);
            
        }
        else {
        	result = "model";
        }
        log.info("ClassNameVariable as::"+result);
        return result;
	}
}
