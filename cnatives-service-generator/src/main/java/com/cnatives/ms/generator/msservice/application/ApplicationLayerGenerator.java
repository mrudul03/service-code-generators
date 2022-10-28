package com.cnatives.ms.generator.msservice.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cnatives.ms.generator.mscontroller.CodeGenerationRequest;
import com.cnatives.ms.generator.mscontroller.DomainModel;
import com.cnatives.ms.generator.mscontroller.DomainModelForm;
import com.cnatives.ms.generator.msservice.base.Constants;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;
import com.cnatives.ms.generator.msservice.output.CodeGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationLayerGenerator {
	
	private CodeGenerator codeGenerator;
	
	public ApplicationLayerGenerator(CodeGenerator codeGenerator) {
		this.codeGenerator = codeGenerator;
	}
	
	public String generateCode(
			GeneratorMetaData metaData, 
			CodeGenerationRequest models) {
		
		this.generateController(metaData, models);
		this.generateContract(metaData, models);
		this.generateApplication(metaData);
		this.generateExceptionClasses(metaData);
		
		return "";
	}
	
	private void generateContract(GeneratorMetaData metaData, 
			CodeGenerationRequest models) {
		
		List<DomainModelForm> domainforms = models.getDomainforms();
		for(DomainModelForm domainModelForm: domainforms) {
			log.info("Domain form name::"+domainModelForm.getName());
			log.info("Domain form entity type::"+domainModelForm.getEntitytype());
			ContractTemplate cTemplate = ContractTemplate.builder().buildFrom(metaData, domainModelForm);
			codeGenerator.generateCode(cTemplate);
		}
	}
	
	private void generateController(
			GeneratorMetaData metaData, 
			CodeGenerationRequest request) {
		
		log.info("Domains::"+request.getDomains().size());
		for(DomainModel domainModel:request.getDomains()) {
			if(domainModel.getEntitytype().equalsIgnoreCase(Constants.AGGREGATE)) {
				
				log.info("Domain name::"+domainModel.getName());
				log.info("Domain entity type::"+domainModel.getEntitytype());
				ControllerTemplate controller = ControllerTemplate.builder()
						.buildFrom(metaData, domainModel);
				codeGenerator.generateCode(controller);
			}
		}
	}
	
	private void generateApplication(GeneratorMetaData metaData) {
		ApplicationTemplate application = ApplicationTemplate.builder().buildFrom(metaData);
		codeGenerator.generateCode(application);
	}
	
	private void generateExceptionClasses(final GeneratorMetaData metaData) {
		ExceptionTemplate exception = ExceptionTemplate.builder().buildFrom(metaData);
		codeGenerator.generateCodeForTemplates(exception);
	}
}
