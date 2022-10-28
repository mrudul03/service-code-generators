package com.code.generator.ms.input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.code.generator.ms.application.Application;
import com.code.generator.ms.application.Pom;
import com.code.generator.ms.application.YamlFile;
import com.code.generator.ms.controller.Controller;
import com.code.generator.ms.controller.TransformModel;
import com.code.generator.ms.controller.Trasformer;
import com.code.generator.ms.exception.ExceptionCode;
import com.code.generator.ms.model.GenerateId;
import com.code.generator.ms.model.Model;
import com.code.generator.ms.model.ModelBuilder;
import com.code.generator.ms.output.CodeGenerator;
import com.code.generator.ms.output.Template;
import com.code.generator.ms.repository.DbModel;
import com.code.generator.ms.repository.DbModelBuilder;
import com.code.generator.ms.repository.DbModels;
import com.code.generator.ms.repository.PersistenceConfig;
import com.code.generator.ms.repository.Repository;
import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MicroserviceGenerator {
	
	@Autowired
	private Template template;
	
	@Autowired
	private CodeGenerator codeGenerator;
	
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
		ModelBuilder modelGenerator = new ModelBuilder();
		List<TransformModel> tModels = modelGenerator.toTransformModel(
				domainModels, metaData);
		
		// generate ms db model
		List<Model> msModels = tModels.stream()
				.map(t -> t.getModel())
				.collect(Collectors.toList());
		DbModelBuilder dbModelGenerator = new DbModelBuilder();
		List<DbModel> dbModels = dbModelGenerator.toDbModels(msModels, metaData);
		List<DbModel> updatedDbModels = dbModelGenerator.initializeDbModels(metaData, dbModels, msModels);
		
		// start generating code
		this.createContract(tModels);
		log.info("------- Generated Contracts");
		
		this.createModel(metaData, tModels);
		log.info("------- Generated Models");
		
		this.createDbModel(metaData, configurations, updatedDbModels);
		log.info("------- Generated DB Models");
		
		this.createRepository(metaData, domainModels);
		log.info("------- Generated Repository");
		
		this.createController(metaData);
		log.info("------- Generated Controller");
		
		this.createApplication(metaData);
		log.info("------- Generated Application");
		
		this.createPom(metaData, configurations);
		log.info("------- Generated Pom");
		
		this.createYamlFiles(metaData, configurations);
		log.info("------- Generated YAMLs");
		
		this.createTransformer(metaData, tModels);
		log.info("------- Generated Transformers");
		
		this.createExceptionClasses(metaData);
		log.info("------- Generated Exception Classes");
		
		this.createConfig(metaData);
		log.info("------- Generated Config Classes");
		
		return metaData.getServiceName();
	}
	
	private void createContract(List<TransformModel> tModels) {
		for(TransformModel tModel:tModels) {
			codeGenerator.generateCode(tModel.getContract());
		}
	}
	
	private void createModel(MsMetaData metaData, List<TransformModel> tModels) {
		for(TransformModel tModel:tModels) {
			//tModel.getModel().generateCode();
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
	}
	
	private void createDbModel(MsMetaData metaData, 
			Configurations configurations,
			List<DbModel> dbModels) {
		
		
		DbModels dbModelList = new DbModels.Builder(dbModels, metaData.getResourceName(), 
				configurations.getSchemaName())
				.withCodeGenDirPath(metaData.getDbscriptGenDirPath())
				.withTemplateDir("templates")
				.withTemplateName("V1.0__create_table.template")
				.build();
		
		codeGenerator.generateCode(dbModelList);
	}
	
	private void createRepository(final MsMetaData metaData, final List<DomainModel> domainModels) {
		String pkdataType = "";
		if(metaData.getPkClazz().equals(String.class)) {
			pkdataType = "String";
		}
		else {
			pkdataType = "Integer";
		}
		for(DomainModel domainModel:domainModels) {
			if(domainModel.getEntitytype().equalsIgnoreCase(Constants.ENTITY_AGGREGATE)) {
				Repository repository = new Repository.Builder(domainModel.getName())
						.withClassName(domainModel.getName()+"Repository")
						.withModelClassName(domainModel.getName())
						.withModelImport("com.service."+metaData.getResourceName().toLowerCase()+".model."+domainModel.getName())
						//.withPkDatatype("String")
						.withPkDatatype(pkdataType)
						.withCodeGenDirPath(metaData.getCodeGenDirPath()+"repository/")
						.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".repository")
						.withTemplateDir("templates")
						.withTemplateName("Repository.template")

						.build();
				codeGenerator.generateCode(repository);
			}
		}
	}
	
	private void createController(final MsMetaData metaData) {
		String pkDataType = "";
		if(metaData.getPkClazz().equals(String.class)) {
			pkDataType = "String";
		}
		else {
			pkDataType = "Integer";
		}
		Controller controller = new Controller.Builder(metaData.getResourceName(), "")
				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"controller/")
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".controller")
				.withTemplateDir("templates")
				.withTemplateName("Controller.template")
				.withRequestMapping("/api/"+metaData.getServiceName())
				.withPkDatatype(pkDataType)
				.build();
		codeGenerator.generateCode(controller);
	}
	
	private void createApplication(final MsMetaData metaData) {
		Application application = new Application.Builder(metaData.getResourceName())
				.withCodeGenDirPath(metaData.getCodeGenDirPath())
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase())
				.withTemplateDir("templates")
				.withTemplateName("Application.template")
				.build();
		codeGenerator.generateCode(application);
	}
	
	private void createPom(final MsMetaData metaData, final Configurations configurations) {
		Pom pom = new Pom.Builder(metaData.getResourceName())
				.withCodeGenDirPath(metaData.getBasePath()+configurations.getServiceName()+"/")
				.withTemplateDir("templates")
				.withTemplateName("pom.template")
				.withServiceName(configurations.getServiceName())
				.build();
		codeGenerator.generateCode(pom);
	}
	
	private void createYamlFiles(final MsMetaData metaData, Configurations configurations) {
		YamlFile yamlFile = new YamlFile();
		yamlFile.initialize(configurations);
		yamlFile.setCodeGenDirPath(metaData.getPropertyFilePath());
		yamlFile.setTemplateDir("templates");
		yamlFile.generateCode(template);
	}
	
	private void createTransformer(final MsMetaData metaData, final List<TransformModel> tModels) {
		Trasformer transformer = new Trasformer.Builder(metaData.getResourceName())
				.withClassName(metaData.getResourceName()+"Transformer")
				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"transform/")
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".transform")
				.withTemplateDir("templates")
				.withTemplateName("Transformer.template")
				.withTransformModels(tModels)
				.build();
				
		codeGenerator.generateCode(transformer);
	}
	
	private void createExceptionClasses(final MsMetaData metaData) {
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
	}
	
	private void createConfig(final MsMetaData metaData) {
		
		PersistenceConfig generateId = new PersistenceConfig.Builder(metaData.getResourceName())
				.withClassName("PersistenceConfig")
				.withPackageName("com.service."+metaData.getResourceName().toLowerCase()+".config")
				.withCodeGenDirPath(metaData.getCodeGenDirPath()+"config/")
				.withTemplateDir("templates")
				.withTemplateName("Config.template")
				.withGeneratedIdPkgName("com.service."+metaData.getResourceName().toLowerCase()+".model.GeneratedStringId")
				.build();
		codeGenerator.generateCode(generateId);
	}

}
