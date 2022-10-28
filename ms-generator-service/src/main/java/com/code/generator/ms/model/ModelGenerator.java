package com.code.generator.ms.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.code.generator.ms.controller.TransformModel;
import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.output.CodeGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ModelGenerator {
	
	@Autowired
	private CodeGenerator codeGenerator;
	
	public void generateModel(MsMetaData metaData, List<TransformModel> tModels) {
		for(TransformModel tModel:tModels) {
			codeGenerator.generateCode(tModel.getModel());
		}
		
		GenerateId generateStringId = new GenerateId.Builder(metaData.getResourceName())
				.withClassName("GeneratedStringId")
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".model")
				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"model/")
				.withTemplateDir("templates")
				.withTemplateName("GeneratedStringId.template")
				.build();
		codeGenerator.generateCode(generateStringId);
		
		GenerateId generateNumnerId = new GenerateId.Builder(metaData.getResourceName())
				.withClassName("GeneratedNumberId")
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".model")
				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"model/")
				.withTemplateDir("templates")
				.withTemplateName("GeneratedNumberId.template")
				.build();
		codeGenerator.generateCode(generateNumnerId);
		log.info("Generated Model");
	}

}
