package com.code.generator.ms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.output.CodeGenerator;
import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ControllerContractGenerator {
	
	@Autowired
	private CodeGenerator codeGenerator;
	
	public void generate(final MsMetaData metaData, final List<TransformModel> tModels) {
		this.generateContract(tModels);
		this.generateControllers(metaData, tModels);
		this.generateTransformers(metaData, tModels);
	}
	
	private void generateContract(List<TransformModel> tModels) {
		for(TransformModel tModel:tModels) {
			codeGenerator.generateCode(tModel.getContract());
		}
	}
	
	private void generateControllers(final MsMetaData metaData, final List<TransformModel> tModels) {
		String pkDataType = "";
		if(metaData.getPkClazz().equals(String.class)) {
			pkDataType = "String";
		}
		else {
			pkDataType = "Integer";
		}
		
		for(TransformModel tModel:tModels) {
			log.info("generateControllers ::Entity:"+tModel.getModel().getClassName()+" EntityType:"+tModel.getModel().getEntityType());
			if(tModel.getModel().getEntityType().equalsIgnoreCase(Constants.AGGREGATE) ||
					tModel.getModel().getEntityType().equalsIgnoreCase(Constants.REF_AGGREGATE)) {
				
				Controller controller = new Controller.Builder(
						metaData.getResourceName(), tModel.getModel().getName())
						.withClassName(tModel.getModel().getClassName()+"Controller")
						.withCodeGenDirPath(metaData.getCodeGenDirPath()+"controller/")
						.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".controller")
						.withTemplateDir("templates")
						.withTemplateName("Controller.template")
						.withRequestMapping("/api/")
						.withPkDatatype(pkDataType)
						.build();
				codeGenerator.generateCode(controller);
			}
			else {
				log.info("Not an aggregate or reference aggregate");
			}
		}
	}
	
//	private void generateController(final MsMetaData metaData) {
//		String pkDataType = "";
//		if(metaData.getPkClazz().equals(String.class)) {
//			pkDataType = "String";
//		}
//		else {
//			pkDataType = "Integer";
//		}
//		Controller controller = new Controller.Builder(metaData.getResourceName())
//				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"controller/")
//				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".controller")
//				.withTemplateDir("templates")
//				.withTemplateName("Controller.template")
//				.withRequestMapping("/api/"+metaData.getServiceName())
//				.withPkDatatype(pkDataType)
//				.build();
//		codeGenerator.generateCode(controller);
//	}
	
//	private void generateTransformer(final MsMetaData metaData, final List<TransformModel> tModels) {
//		Trasformer transformer = new Trasformer.Builder(metaData.getResourceName())
//				.withClassName(metaData.getResourceName()+"Transformer")
//				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"transform/")
//				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".transform")
//				.withTemplateDir("templates")
//				.withTemplateName("Transformer.template")
//				.withTransformModels(tModels)
//				.build();
//				
//		codeGenerator.generateCode(transformer);
//	}
	
	private void generateNewTransformer(final MsMetaData metaData, final List<TransformModel> tModels,
			final String aggregateModelName) {
		Trasformer transformer = new Trasformer.Builder(metaData.getResourceName())
				.withClassName(aggregateModelName+"Transformer")
				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"transform/")
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".transform")
				.withTemplateDir("templates")
				.withTemplateName("Transformer.template")
				.withTransformModels(tModels)
				.build();
				
		codeGenerator.generateCode(transformer);
	}
	
	private void generateTransformers(final MsMetaData metaData, final List<TransformModel> tModels) {
		// collect all aggregate TModels
		List<TransformModel> tAggregateModels = tModels.stream().filter(tmodel -> 
			(tmodel.getModel().getEntityType().equalsIgnoreCase(Constants.AGGREGATE) ||
				tmodel.getModel().getEntityType().equalsIgnoreCase(Constants.REF_AGGREGATE)) )
			.collect(Collectors.toList());
		
		for(TransformModel tAggregateModel:tAggregateModels) {
			
			List<TransformModel> tRefModels  = new ArrayList<>();
			this.collectReferences(tModels, tAggregateModel, tRefModels);
			tRefModels.add(tAggregateModel);
			this.generateNewTransformer(metaData, tRefModels, tAggregateModel.getModelName());
		}
	}
	
	private List<TransformModel> collectReferences(final List<TransformModel> tModels, 
			final TransformModel tAggregateModel, List<TransformModel> tRefModels){
		//log.info("---------------------------------- Recursively collecting for ::"+tAggregateModel.getModelName());
		List<String> referenceFields = tAggregateModel.getModel().getReferenceFields();
		if(referenceFields.size() > 0) {
			for(String refFieldName: referenceFields) {
				TransformModel refModel = tModels.stream().filter(tModel -> 
					tModel.getModel().getName().equalsIgnoreCase(refFieldName)).findFirst().orElse(null);
				tRefModels.add(refModel);
				
				log.info("Continute to check all ref models for their references");
				this.collectReferences(tModels, refModel, tRefModels);
			}
		}
		return tRefModels;
	}
}
