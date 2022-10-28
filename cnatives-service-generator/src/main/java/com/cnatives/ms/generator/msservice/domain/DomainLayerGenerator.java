package com.cnatives.ms.generator.msservice.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cnatives.ms.generator.mscontroller.CodeGenerationRequest;
import com.cnatives.ms.generator.mscontroller.DomainModel;
import com.cnatives.ms.generator.msservice.base.Constants;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;
import com.cnatives.ms.generator.msservice.output.CodeGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DomainLayerGenerator {
	
	private CodeGenerator codeGenerator;
	
	public DomainLayerGenerator(CodeGenerator codeGenerator) {
		this.codeGenerator = codeGenerator;
	}
	
	public String generateCode(
			GeneratorMetaData metaData, 
			CodeGenerationRequest request) {
		
		this.generateDomains(metaData, request);
		this.generateServices(metaData, request);
		this.generateRepositories(metaData, request);
		
		return "";
	}
	
	public List<DomainTemplate> getDomainTemplates(List<DomainModel> domainModels, GeneratorMetaData metaData){
		
		List<DomainTemplate> domainTemplates = new ArrayList<>();
		for(DomainModel domainModel: domainModels) {
			log.info("Domain form name::"+domainModel.getName());
			log.info("Domain form entity type::"+domainModel.getEntitytype());
			
			DomainTemplate domainTemplate = DomainTemplate.builder()
					.buildFrom(domainModels, domainModel, metaData);
			
			domainTemplates.add(domainTemplate);
		}
		
		return domainTemplates;
	}
	
	
	private void generateDomains(GeneratorMetaData metaData, 
			CodeGenerationRequest request) {
		
		List<DomainModel> domains = request.getDomains();
		for(DomainModel domainModel: domains) {
			log.info("Domain form name::"+domainModel.getName());
			log.info("Domain form entity type::"+domainModel.getEntitytype());
			
			DomainTemplate domainTemplate = DomainTemplate.builder()
					.buildFrom(request.getDomains(), domainModel, metaData);
			codeGenerator.generateCode(domainTemplate);
		}
	}
	
	private void generateServices(GeneratorMetaData metaData, 
			CodeGenerationRequest request) {
		
		List<DomainModel> domains = request.getDomains();
		for(DomainModel domainModel: domains) {
			log.info("Domain form name::"+domainModel.getName());
			log.info("Domain form entity type::"+domainModel.getEntitytype());
			
			if(domainModel.getEntitytype().equalsIgnoreCase(Constants.AGGREGATE) ||
					domainModel.getEntitytype().equalsIgnoreCase(Constants.REF_AGGREGATE)) {
				
				ServiceTemplate serviceTemplate = ServiceTemplate.builder()
						.buildFrom(request, domainModel, metaData);
				codeGenerator.generateCode(serviceTemplate);
			}
			
		}
	}
	
	private void generateRepositories(GeneratorMetaData metaData, 
			CodeGenerationRequest request) {
		
		List<DomainModel> domains = request.getDomains();
		for(DomainModel domainModel: domains) {
			log.info("Domain form name::"+domainModel.getName());
			log.info("Domain form entity type::"+domainModel.getEntitytype());
			
			if(domainModel.getEntitytype().equalsIgnoreCase(Constants.AGGREGATE) ||
					domainModel.getEntitytype().equalsIgnoreCase(Constants.REF_AGGREGATE)) {
				
				RepositoryTemplate repositoryTemplate = RepositoryTemplate.builder()
						.buildFrom(request, domainModel, metaData);
				codeGenerator.generateCode(repositoryTemplate);
			}
			
		}
	}
}
