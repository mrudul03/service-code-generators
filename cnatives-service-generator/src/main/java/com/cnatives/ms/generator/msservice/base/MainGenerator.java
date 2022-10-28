package com.cnatives.ms.generator.msservice.base;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cnatives.ms.generator.mscontroller.CodeGenerationRequest;
import com.cnatives.ms.generator.mscontroller.Configurations;
import com.cnatives.ms.generator.mscontroller.DomainModel;
import com.cnatives.ms.generator.msservice.application.ApplicationLayerGenerator;
import com.cnatives.ms.generator.msservice.config.ConfigGenerator;
import com.cnatives.ms.generator.msservice.domain.DomainLayerGenerator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MainGenerator {
	
	private ApplicationLayerGenerator applicationGenerator;
	private DomainLayerGenerator domainGenerator;
	private ConfigGenerator configGenerator;
	
	public MainGenerator(
			ApplicationLayerGenerator applicationGenerator,
			DomainLayerGenerator domainGenerator,
			ConfigGenerator configGenerator) {
		
		this.applicationGenerator = applicationGenerator;
		this.domainGenerator = domainGenerator;
		this.configGenerator = configGenerator;
	}
	
	public String generateServiceCode(
			CodeGenerationRequest request, String customerId) {
		
		//models.initializeModel();
		Configurations configurations = request.getConfigurations();
		configurations.initialize();
		List<DomainModel> domainModels = request.getDomains();
		
		GeneratorMetaData metaData = new GeneratorMetaData();
		metaData.initialize(configurations, domainModels, customerId);
		
		log.info("Initialized configurations");
		log.info("--- resourceName:"+metaData.getDomainName());
		log.info("--- serviceName:"+metaData.getServiceName());
		log.info("--- codeGenDirPath:"+metaData.getCodeGenDirPath());
		
		// start generating code
		applicationGenerator.generateCode(metaData, request);
		domainGenerator.generateCode(metaData, request);
		configGenerator.generateOtherfiles(metaData, configurations);
		
		return metaData.getServiceName();
	}
}
