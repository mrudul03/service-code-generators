package com.cnatives.ms.generator.msclient.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cnatives.ms.generator.mscontroller.CodeGenerationRequest;
import com.cnatives.ms.generator.mscontroller.Configurations;
import com.cnatives.ms.generator.mscontroller.DomainModel;
import com.cnatives.ms.generator.mscontroller.DomainModelForm;
import com.cnatives.ms.generator.msservice.application.ContractTemplate;
import com.cnatives.ms.generator.msservice.base.Constants;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;
import com.cnatives.ms.generator.msservice.output.CodeGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MsClientGenerator {
	
	private static final String CLIENT_PACKAGE_NAME = "serviceclient";
	private CodeGenerator codeGenerator;
	
	public MsClientGenerator(CodeGenerator codeGenerator) {
		this.codeGenerator = codeGenerator;
	}
	
	public String generateClient(
			CodeGenerationRequest request, Configurations configurations, GeneratorMetaData metaData) {
		
		String packageName = metaData.getBasePackage()+"."+metaData.getDomainName().toLowerCase()+"."+CLIENT_PACKAGE_NAME;
		this.generateContract(metaData, request, packageName);
		this.generateServiceClient(metaData, request, packageName);
		this.generateServiceClientConfig(metaData, packageName);
		
		return "";
	}
	
	private void generateContract(GeneratorMetaData metaData, 
			CodeGenerationRequest models,
			String packageName) {
		
		List<DomainModelForm> domainforms = models.getDomainforms();
		for(DomainModelForm domainModelForm: domainforms) {
			log.info("Domain form name::"+domainModelForm.getName());
			log.info("Domain form entity type::"+domainModelForm.getEntitytype());
			ContractTemplate cTemplate = ContractTemplate.builder()
					.withPackageName(packageName)
					.withCodeGenDirPath(metaData.getClientGenDirPath())
					.buildFrom(metaData, domainModelForm);
			codeGenerator.generateCode(cTemplate);
		}
	}
	
	private void generateServiceClient(GeneratorMetaData metaData, 
			CodeGenerationRequest request, String packageName) {
		
		for(DomainModel domainModel: request.getDomains()) {
			
			if(domainModel.getEntitytype().equalsIgnoreCase(Constants.AGGREGATE)) {
				ServiceCleintTemplate template = ServiceCleintTemplate.builder()
						.withPackageName(packageName)
						.buildFrom(metaData, domainModel);
				codeGenerator.generateCode(template);
			}
		}
	}
	
	private void generateServiceClientConfig(GeneratorMetaData metaData, String packageName) {
		ServiceClientConfig clientConfig = new ServiceClientConfig(packageName, metaData.getClientGenDirPath());
		codeGenerator.generateCode(clientConfig);
	}

}
