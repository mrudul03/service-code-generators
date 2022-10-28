package com.cnatives.ms.generator.dbservice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cnatives.ms.generator.dbcontroller.DbGenerationRequest;
import com.cnatives.ms.generator.msservice.base.GeneratorMetaData;
import com.cnatives.ms.generator.msservice.domain.DomainLayerGenerator;
import com.cnatives.ms.generator.msservice.domain.DomainTemplate;
import com.cnatives.ms.generator.msservice.output.CodeGenerator;

@Service
public class DbModelGenerator {
	
	private CodeGenerator codeGenerator;
	private DbModelBuilder dbModelBuilder;
	private DomainLayerGenerator domainLayerGenerator;
	
	public DbModelGenerator(CodeGenerator codeGenerator,
			DbModelBuilder dbModelBuilder,
			DomainLayerGenerator domainLayerGenerator) {
		
		this.codeGenerator = codeGenerator;
		this.dbModelBuilder = dbModelBuilder;
		this.domainLayerGenerator = domainLayerGenerator;
	}
	
	public void generateDbModel(
			GeneratorMetaData metaData, 
			DbGenerationRequest request
			) {
		
		List<DomainTemplate> domainTemplates = domainLayerGenerator.getDomainTemplates(request.getDomains(), metaData);
		
		List<DbModel> dbModels = dbModelBuilder.createDbModel(metaData, domainTemplates);
		List<DbModel> updatedDbModels = dbModelBuilder.initializeDbModels(metaData, dbModels, domainTemplates);
		
		// generate db script
		String schemaName = request.getConfigurations().getSchemaName();
		DbModelTemplate dbModelTemplate = DbModelTemplate.builder().buildFrom(
				updatedDbModels, metaData, schemaName);
		codeGenerator.generateCode(dbModelTemplate);

	}
	
//	private List<DomainTemplate> createDomainTemplate(
//			GeneratorMetaData metaData, 
//			CodeGenerationRequest request){
//		
//		List<DomainModel> domains = request.getDomains();
//		List<DomainTemplate> domainTemplates = new ArrayList<>();
//		
//		for(DomainModel domainModel: domains) {
//			
//			DomainTemplate domainTemplate = DomainTemplate.builder()
//					.buildFrom(request, domainModel, metaData);
//			domainTemplates.add(domainTemplate);
//		}
//		return domainTemplates;
//	}

}
