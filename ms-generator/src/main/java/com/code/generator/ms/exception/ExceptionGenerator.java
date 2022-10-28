package com.code.generator.ms.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.output.CodeGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExceptionGenerator {
	
	@Autowired
	private CodeGenerator codeGenerator;
	
	public void generate(final MsMetaData metaData) {
		this.generateExceptionClasses(metaData);
	}
	
	private void generateExceptionClasses(final MsMetaData metaData) {
		Map<String, String> templateNames = new HashMap<>();
		templateNames.put("ExceptionHelper", "ExceptionHelper.template");
		templateNames.put("InvalidInputException", "InvalidInputException.template");
		templateNames.put("NoDataFoundException", "NoDataFoundException.template");
		
		ExceptionCode exGenerator = new ExceptionCode.Builder(metaData.getResourceName())
				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"exception/")
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".exception")
				.withTemplateDir("templates")
				.withTemplateNames(templateNames)
				.build();
		
		codeGenerator.generateCodeForTemplates(exGenerator);
		log.info("Generated exception classes");
	}
}
