package com.code.generator.ms.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.output.CodeGenerator;
import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ModelGenerator {
	
	@Autowired
	private CodeGenerator codeGenerator;
	
	public void generateModel(MsMetaData metaData, List<ModelHolder> tModels) {
		this.generateModels(metaData, tModels);
		this.generateGenerateId(metaData);
		this.generateServices(metaData, tModels);
	}
	
	private void generateModels(MsMetaData metaData, List<ModelHolder> tModels) {
		for(ModelHolder tModel:tModels) {
			codeGenerator.generateCode(tModel.getModel());
		}
		log.info("Generated Models");
	}
	
	private void generateGenerateId(MsMetaData metaData) {
		GenerateId generateStringId = new GenerateId.Builder(metaData.getResourceName())
				.withClassName("GeneratedStringId")
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".domain")
				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"domain/")
				.withTemplateDir("templates")
				.withTemplateName("GeneratedStringId.template")
				.build();
		codeGenerator.generateCode(generateStringId);
		
		GenerateId generateNumnerId = new GenerateId.Builder(metaData.getResourceName())
				.withClassName("GeneratedNumberId")
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".domain")
				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"domain/")
				.withTemplateDir("templates")
				.withTemplateName("GeneratedNumberId.template")
				.build();
		codeGenerator.generateCode(generateNumnerId);
		log.info("Generated GeneratedStringId and GeneratedNumberId");
	}
	
	private void generateServices(final MsMetaData metaData, final List<ModelHolder> tModels) {
		String pkDataType = "";
		if(metaData.getPkClazz().equals(String.class)) {
			pkDataType = "String";
		}
		else if(metaData.getPkClazz().equals(Long.class)) {
			pkDataType = "Long";
		}
		else {
			pkDataType = "Integer";
		}
		
		for(ModelHolder tModel:tModels) {
			
			if(tModel.getModel().getEntityType().equalsIgnoreCase(Constants.AGGREGATE) ||
					tModel.getModel().getEntityType().equalsIgnoreCase(Constants.REF_AGGREGATE)) {
				
				Service service = new Service.Builder(
						metaData.getResourceName(), tModel.getModel().getName())
						.withClassName(tModel.getModel().getClassName()+"Service")
						.withCodeGenDirPath(metaData.getCodeGenDirPath()+"domain/")
						.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".domain")
						.withTemplateDir("templates")
						.withTemplateName("Service.template")
						.withPkDatatype(pkDataType)
						.withIdFieldName(tModel.getModel().getIdFieldName())
						.build();
				codeGenerator.generateCode(service);
			}
			else {
				log.info("Generate Services::Not an aggregate or reference aggregate");
			}
		}
		log.info("Generated Services");
	}

}
