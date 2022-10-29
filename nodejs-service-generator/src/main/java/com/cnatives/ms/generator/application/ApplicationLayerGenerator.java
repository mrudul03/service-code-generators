package com.cnatives.ms.generator.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cnatives.ms.contract.CodeGenerationRequest;
import com.cnatives.ms.contract.DomainModel;
import com.cnatives.ms.contract.Field;
import com.cnatives.ms.generator.base.Constants;
import com.cnatives.ms.generator.base.GeneratorMetaData;
import com.cnatives.ms.generator.output.CodeGenerator;

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
		return "";
	}
	
	private void generateController(
			GeneratorMetaData metaData, 
			CodeGenerationRequest request) {
		
		log.info("Domains::"+request.getDomains().size());
		for(DomainModel domainModel:request.getDomains()) {
			if(domainModel.getEntitytype().equalsIgnoreCase(Constants.AGGREGATE)) {
				
				log.info("Domain name::"+domainModel.getName());
				log.info("Domain entity type::"+domainModel.getEntitytype());
				List<DomainDto> domains = this.getChildEntities(domainModel);
				DomainDto domainDto = new DomainDto();
				domainDto.setDomainName(domainModel.getName());
				domainDto.setDomainNameVariable(domainModel.getName().toLowerCase());
				domains.add(domainDto);
				
				ControllerTemplate controller = ControllerTemplate.builder()
						.buildFrom(metaData, domainModel, domains);
				
				
				codeGenerator.generateCode(controller);
			}
		}
	}
	
	private List<DomainDto> getChildEntities(DomainModel aggDomainModel) {
		List<DomainDto> childDomainNames = new ArrayList<>();
		for(Field field: aggDomainModel.getFields()) {
			boolean isPrimitive = Constants.primitiveDatatypes.contains(field.getDatatype());
			if(!isPrimitive) {
				DomainDto domainDto = new DomainDto();
				domainDto.setDomainName(field.getDatatype());
				domainDto.setDomainNameVariable(field.getDatatype().toLowerCase());
				log.info("Adding domain to controller::"+field.getDatatype());
				childDomainNames.add(domainDto);
			}
		}
//		
//		for(DomainModel domainModel:domainModels) {
//			if(aggDomainModel.getName().equalsIgnoreCase(domainModel.getName())) {
//				// skip
//			}
//			else {
//				for(Field field: domainModel.getFields()) {
//					boolean isPrimitive = Constants.primitiveDatatypes.contains(field.getDatatype());
//					if(!isPrimitive) {
//						DomainDto domainDto = new DomainDto();
//						domainDto.setDomainName(field.getDatatype());
//						domainDto.setDomainNameVariable(field.getDatatype().toLowerCase());
//						log.info("Adding domain to controller::"+field.getDatatype());
//						childDomainNames.add(domainDto);
//					}
//				}
//			}
//		}
		return childDomainNames;
	}
}
