package com.code.generator.ms.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.code.generator.ms.input.Configurations;
import com.code.generator.ms.input.DomainModel;
import com.code.generator.ms.input.MsMetaData;
import com.code.generator.ms.output.CodeGenerator;
import com.code.generator.ms.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DbModelGenerator {
	
	@Autowired
	private CodeGenerator codeGenerator;
	
	public void generate(
			final MsMetaData metaData, 
			final Configurations configurations,
			final List<DbModel> dbModels,
			final List<DomainModel> domainModels) {
		
		this.generateDbModel(metaData, configurations, dbModels);
		this.generateRepository(metaData, domainModels);
		this.generateRepository(metaData, domainModels);
		this.generatePersistenceConfig(metaData);
	}
	
	private void generateDbModel(MsMetaData metaData, 
			Configurations configurations,
			List<DbModel> dbModels) {
		
		
		DbModels dbModelList = new DbModels.Builder(dbModels, metaData.getResourceName(), 
				configurations.getSchemaName())
				.withCodeGenDirPath(metaData.getDbscriptGenDirPath())
				.withTemplateDir("templates")
				.withTemplateName("V1.0__create_table.template")
				.build();
		
		codeGenerator.generateCode(dbModelList);
		log.info("Generated SQL Script");
	}
	
	private void generateRepository(final MsMetaData metaData, final List<DomainModel> domainModels) {
		String pkdataType = "";
		if(metaData.getPkClazz().equals(String.class)) {
			pkdataType = "String";
		}
		else {
			pkdataType = "Integer";
		}
		for(DomainModel domainModel:domainModels) {
			if(domainModel.getEntitytype().equalsIgnoreCase(Constants.ENTITY_AGGREGATE) ||
					domainModel.getEntitytype().equalsIgnoreCase(Constants.REF_AGGREGATE)) {
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
	
	private void generatePersistenceConfig(final MsMetaData metaData) {
		
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
