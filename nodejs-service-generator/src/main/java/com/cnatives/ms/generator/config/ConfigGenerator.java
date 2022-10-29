package com.cnatives.ms.generator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnatives.ms.contract.CodeGenerationRequest;
import com.cnatives.ms.contract.Configurations;
import com.cnatives.ms.contract.DomainModel;
import com.cnatives.ms.generator.application.DomainDto;
import com.cnatives.ms.generator.base.GeneratorMetaData;
import com.cnatives.ms.generator.output.Template;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConfigGenerator {
	
	@Autowired
	private Template template;
	
	public void generateOtherfiles(GeneratorMetaData metaData, 
			final Configurations configurations,
			final CodeGenerationRequest request) {
		this.generateDbConfigFile(metaData, configurations, request);
		this.generateEnvFile(metaData, configurations);
	}
	
	private void generateDbConfigFile(final GeneratorMetaData metaData, 
			final Configurations configurations,
			final CodeGenerationRequest request) {
		
		DbConfigTemplate dbConfig = new DbConfigTemplate();
		dbConfig.initialize(configurations);
		for(DomainModel domainModel:request.getDomains()) {
			DomainDto domainDto = new DomainDto();
			domainDto.setDomainName(domainModel.getName());
			domainDto.setDomainNameVariable(domainModel.getName().toLowerCase());
			dbConfig.addDomain(domainDto);
		}
		dbConfig.setCodeGenDirPath(metaData.getConfigPath());
		dbConfig.setTemplateDir("templates");
		dbConfig.generateCode(template);
		log.info("Generated DB config");
	}
	
	private void generateEnvFile(final GeneratorMetaData metaData, 
			final Configurations configurations) {
		
		EnvTemplate envTemplate = new EnvTemplate();
		envTemplate.initialize(configurations);
		envTemplate.setCodeGenDirPath(metaData.getConfigPath());
		envTemplate.setTemplateDir("templates");
		envTemplate.generateCode(template);
		log.info("Generated DB config");
	}

}
