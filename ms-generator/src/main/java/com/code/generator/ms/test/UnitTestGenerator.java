package com.code.generator.ms.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.model.ModelHolder;
import com.code.generator.ms.output.CodeGenerator;
import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UnitTestGenerator {
	
	@Autowired
	private CodeGenerator codeGenerator;
	
	public void generateServiceTests(final MsMetaData metaData, final List<ModelHolder> tModels) {
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
				
				ServiceTest serviceTest = new ServiceTest.Builder(
						metaData.getResourceName(), tModel.getModel().getName())
						.withClassName(tModel.getModel().getClassName()+"ServiceTest")
						.withCodeGenDirPath(metaData.getTestGenDirPath()+"domain/")
						.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".domain")
						.withTemplateDir("templates")
						.withTemplateName("ServiceTest.template")
						.withPkDatatype(pkDataType)
						.withServiceName(tModel.getModel().getClassName()+"Service")
						.withRepositoryName(tModel.getModel().getClassName()+"Repository")
						.withModelHolders(tModels)
						.build();
				codeGenerator.generateCode(serviceTest);
			}
			else {
				log.info("Generate Services::Not an aggregate or reference aggregate");
			}
		}
		log.info("Generated Service Tests");
	}

}
