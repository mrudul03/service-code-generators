package com.code.generator.ms.input;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.code.generator.ms.application.BuildFileGenerator;
import com.code.generator.ms.controller.ControllerContractGenerator;
import com.code.generator.ms.controller.TransformModel;
import com.code.generator.ms.exception.ExceptionGenerator;
import com.code.generator.ms.model.Model;
import com.code.generator.ms.model.ModelBuilder;
import com.code.generator.ms.model.ModelGenerator;
import com.code.generator.ms.repository.DbModel;
import com.code.generator.ms.repository.DbModelBuilder;
import com.code.generator.ms.repository.DbModelGenerator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MsGenerator {
	
	private ControllerContractGenerator controllerContractGenerator;
	private ModelGenerator modelGenerator;
	private ExceptionGenerator exceptionGenerator;
	private DbModelGenerator dbModelGenerator;
	private BuildFileGenerator buildFileGenerator;
	
	public MsGenerator(
			ControllerContractGenerator controllerContractGenerator,
			ModelGenerator modelGenerator,
			ExceptionGenerator exceptionGenerator,
			DbModelGenerator dbModelGenerator,
			BuildFileGenerator buildFileGenerator) {
		
		this.controllerContractGenerator = controllerContractGenerator;
		this.modelGenerator = modelGenerator;
		this.exceptionGenerator = exceptionGenerator;
		this.dbModelGenerator = dbModelGenerator;
		this.buildFileGenerator = buildFileGenerator;
	}
	
	public String generateServiceCode(
			Configurations configurations, Models models, String customerId) {
		
		models.initializeModel();
		List<DomainModel> domainModels = models.getModels();
		
		MsMetaData metaData = new MsMetaData();
		metaData.initialize(configurations, customerId, domainModels);
		
		log.info("Initialized configurations");
		log.info("--- resourceName:"+metaData.getResourceName());
		log.info("--- serviceName:"+metaData.getServiceName());
		log.info("--- codeGenDirPath:"+metaData.getCodeGenDirPath());
		
		// generate ms model
		ModelBuilder modelBuilder = new ModelBuilder();
		List<TransformModel> tModels = modelBuilder.toTransformModel(
				domainModels, metaData);
		
		// generate ms db model
		List<Model> msModels = tModels.stream()
				.map(t -> t.getModel())
				.collect(Collectors.toList());
		DbModelBuilder dbModelBuilder = new DbModelBuilder();
		List<DbModel> dbModels = dbModelBuilder.toDbModels(msModels, metaData);
		List<DbModel> updatedDbModels = dbModelBuilder.initializeDbModels(metaData, dbModels, msModels);
		
		// start generating code
		controllerContractGenerator.generate(metaData, tModels);
		modelGenerator.generateModel(metaData, tModels);
		dbModelGenerator.generate(metaData, configurations, updatedDbModels, domainModels);
		exceptionGenerator.generate(metaData);
		buildFileGenerator.generate(metaData, configurations);
		
		return metaData.getServiceName();
	}

}
