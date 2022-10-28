package com.code.generator.ms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.model.ModelHolder;
import com.code.generator.ms.output.CodeGenerator;
import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ControllerContractGenerator {
	
	@Autowired
	private CodeGenerator codeGenerator;
	
	public void generate(final MsMetaData metaData, final List<ModelHolder> tModels) {
		this.generateRequestsResponses(metaData, tModels);
		this.generateControllers(metaData, tModels);
	}
	
	private void generateRequestsResponses(final MsMetaData metaData, List<ModelHolder> modelHolders) {
		for(ModelHolder modelHolder:modelHolders) {
			codeGenerator.generateCode(modelHolder.getContractRequest());
			codeGenerator.generateCode(modelHolder.getContractResponse());
		}
	}
	
	private void generateControllers(final MsMetaData metaData, final List<ModelHolder> tModels) {
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
				
				Controller controller = new Controller.Builder(
						metaData.getResourceName(), tModel.getModel().getName())
						.withClassName(tModel.getModel().getClassName()+"Controller")
						.withModelVariableName(this.getModelNameVariable(tModel.getModel().getName()))
						.withIdFieldName(tModel.getModel().getIdFieldName())
						.withCodeGenDirPath(metaData.getCodeGenDirPath()+"controller/")
						.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".controller")
						.withTemplateDir("templates")
						.withTemplateName("Controller.template")
						.withRequestMapping("/api")
						.withPkDatatype(pkDataType)
						.build();
				codeGenerator.generateCode(controller);
			}
			else {
				log.info("Generating Coltroller:: Not an aggregate or reference aggregate");
			}
		}
	}
	
	private String getModelNameVariable(String modelName) {
        String result = "";
        if(null != modelName && modelName.length() > 0) {
        	// Append first character(in lower case)
            char c = modelName.charAt(0);
            result = Character.toLowerCase(c)+ modelName.substring(1);
            
        }
        else {
        	result = "model";
        }
        log.info("ModelNameVariable as::"+result);
        return result;
	}
}
