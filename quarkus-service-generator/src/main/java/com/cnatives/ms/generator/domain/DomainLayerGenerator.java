package com.cnatives.ms.generator.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cnatives.ms.contract.CodeGenerationRequest;
import com.cnatives.ms.contract.DomainModel;
import com.cnatives.ms.generator.base.Constants;
import com.cnatives.ms.generator.base.GeneratorMetaData;
import com.cnatives.ms.generator.output.CodeGenerator;

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
	
//	public List<DomainTemplate> getDomainTemplates(List<DomainModel> domainModels, GeneratorMetaData metaData){
//		
//		List<DomainTemplate> domainTemplates = new ArrayList<>();
//		for(DomainModel domainModel: domainModels) {
//			log.info("Domain form name::"+domainModel.getName());
//			log.info("Domain form entity type::"+domainModel.getEntitytype());
//			
//			DomainTemplate domainTemplate = DomainTemplate.builder()
//					.buildFrom(domainModels, domainModel, metaData, null);
//			
//			domainTemplates.add(domainTemplate);
//		}
//		
//		return domainTemplates;
//	}
	
	
	private void generateDomains(GeneratorMetaData metaData, 
			CodeGenerationRequest request) {
		
		List<DomainModel> domains = request.getDomains();
		List<DomainTemplate> domainTemplates = new ArrayList<>();
		for(DomainModel domainModel: domains) {
			log.info("Domain form name::"+domainModel.getName());
			log.info("Domain form entity type::"+domainModel.getEntitytype());
			
			DomainTemplate domainTemplate = DomainTemplate.builder()
					.buildFrom(request.getDomains(), domainModel, metaData, request.getDomainforms());
			
			//codeGenerator.generateCode(domainTemplate);
			domainTemplates.add(domainTemplate);
		}
		
		// update references
		List<DomainTemplate> updatedTemplates = this.updateReferences(domainTemplates);
		for(DomainTemplate domainTemplate: updatedTemplates) {
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
	
	private List<DomainTemplate> updateReferences(
			final List<DomainTemplate> domainTemplates) {
		
		for(DomainTemplate domainTemplate:domainTemplates) {
			
			for(ClassField classField: domainTemplate.getClassfields()) {
				//if(!this.isPrimitive(classField.getDatatype())) {
				if(null != classField.getIsCustom()) {
					String modelDataType = classField.getDatatype();
					DomainTemplate matchedDomainTemlate = domainTemplates.stream()
						.filter(dm -> dm.getClassName().equalsIgnoreCase(modelDataType))
						.findFirst()
						.orElse(null);
					log.info("Custom data type::"+modelDataType);
					log.info("Got Domain template Match for creating references::"+matchedDomainTemlate.getClassName());
					Reference reference = this.createReference(matchedDomainTemlate, classField);
					log.info("Reference targetColumnName::"+reference.getTargetColumnName());
					log.info("Reference ColumnName::"+reference.getColumnName());
					log.info("Reference Relation::"+reference.getRelation());
					classField.setReference(reference);
				}
			}
		}
		return domainTemplates;
	}
	
	private Reference createReference(DomainTemplate model, ClassField cField) {
		
		String targetName = model.getTableName();
		String targetColumnName = model.getIdColumnName();
		String columnName = model.getTableName();
		String relation = cField.getRelation();
		
		Reference reference = new Reference(
				targetName,
				targetColumnName,
				columnName,
				relation);
		return reference;
	}
}
