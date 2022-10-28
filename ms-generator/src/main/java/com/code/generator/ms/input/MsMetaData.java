package com.code.generator.ms.input;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.code.generator.ms.util.Constants;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class MsMetaData {
	
	private String basePath = "codegeneration/";
	private String resourceName;
	private String serviceName;
	private String codeGenDirPath;
	private String testGenDirPath;
	private String dbscriptGenDirPath;
	private String propertyFilePath;
	private Class<?> pkClazz;
	private Class<?> collectionClazz;
	private String databaseType;
	
	public void initialize(Configurations configurations, String customerId, List<DomainModel> domainModels) {
		
		this.initializePathConfigurations(configurations, customerId, domainModels);
		this.initializeDatabaseConfigurations(configurations);
	}
	
	private String getAggregateModelName(List<DomainModel> domainModels) {
		
		DomainModel aggDomainModel = domainModels.stream()
				.filter(dm -> dm.getEntitytype().equalsIgnoreCase("Aggregate"))
				.findFirst()
				.orElse(null);
		
		if(null != aggDomainModel) {
			return StringUtils.capitalize(aggDomainModel.getName());
		}
		else {
			return null;
		}
	}
	
	private void initializePathConfigurations(Configurations configurations, String customerId, List<DomainModel> domainModels) {
		this.basePath = "codegeneration/"+customerId+"/";
		this.resourceName = this.getAggregateModelName(domainModels);
		
		this.serviceName = configurations.getServiceName();
		this.codeGenDirPath = this.basePath+configurations.getServiceName()+"/src/main/java/com/service/"+this.resourceName.toLowerCase()+"/";
		this.testGenDirPath = this.basePath+configurations.getServiceName()+"/src/test/java/com/service/"+this.resourceName.toLowerCase()+"/";
		this.dbscriptGenDirPath = this.basePath+configurations.getServiceName()+"/db/migration/";
		this.propertyFilePath = this.basePath+configurations.getServiceName()+"/src/main/resources/";
	}
	
	private void initializeDatabaseConfigurations(Configurations configurations) {
		
		log.info("Configurations ............ PKType--------:"+configurations.getPkType());
		collectionClazz = List.class;
		
//		if(configurations.getPkType().equalsIgnoreCase(Constants.STRING)) {
//			pkClazz = String.class;
//		} 
//		else if(configurations.getPkType().equalsIgnoreCase(Constants.LONG)){
//			pkClazz = Long.class;
//		}
//		else {
//			pkClazz = Integer.class;
//		}
		this.pkClazz = this.getPkDataType(configurations);
		
		if(configurations.getDatabaseType().equalsIgnoreCase("mysql")) {	
			this.databaseType = configurations.getDatabaseType();
		}
		else if(configurations.getDatabaseType().equalsIgnoreCase("postgres") ||
				configurations.getDatabaseType().equalsIgnoreCase("postgressql")) {
			this.databaseType = configurations.getDatabaseType();
		}
		else {
			this.databaseType = "";
		}
	}
	
	private Class<?> getPkDataType(Configurations configurations) {
		Class<?> pkClazz = null;
		if(null == configurations.getPkType() || configurations.getPkType().isBlank() || configurations.getPkType().isEmpty()) {
			pkClazz = Integer.class;
		}
		else if(configurations.getPkType().equalsIgnoreCase(Constants.LONG)){
			pkClazz = Long.class;
		}
		else {
			pkClazz = Integer.class;
		}
		return pkClazz;
	}

}
