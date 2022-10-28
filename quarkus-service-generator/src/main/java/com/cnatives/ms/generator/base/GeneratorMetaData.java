package com.cnatives.ms.generator.base;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cnatives.ms.contract.Configurations;
import com.cnatives.ms.contract.DomainModel;

import lombok.Data;

@Data
public class GeneratorMetaData {
	
	private String basePath = "codegeneration";
	private String dbbasePath = "dbgeneration";
	private String clientbasePath = "clientgeneration";
	private String archivebasePath = "archives";
	
	private String domainName;
	private String serviceName;
	private String serviceUrl;
	private String servicePort;
	
	private String propertyFilePath;
	private String pkClazzName;
	
	private String codeGenDirPath;
	private String testGenDirPath;
	private String dbscriptGenDirPath;
	private String clientGenDirPath;
	
	private String collectionClazzName;
	private String databaseType;
	private String basePackage ;
	private String pkDataType;
	
	private String serviceGenerationDir;
	private String clientGenerationDir;
	private String dbscriptGenerationDir;
	
	private String serviceArchiveFilename;
	private String clientArchiveFilename;
	private String dbscriptArchiveFilename;
	
	private String pomFilePath;
	
	private final String PATH_SEPARATOR = "/";
	
	private String customerId;
	private String serviceBaseName;
	
	public void initialize(Configurations configurations, 
			List<DomainModel> domainModels, 
			String customerId) {
		this.customerId = customerId;
		this.serviceBaseName = configurations.getServiceBaseName();
		this.initializePathConfigurations(configurations, domainModels, customerId);
		this.initializeDatabaseConfigurations(configurations);
		this.initializeServiceConfigurations(configurations);
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
	
	private void initializeDatabaseConfigurations(Configurations configurations) {
		
		//log.info("Configurations ............ PKType--------:"+configurations.getPkType());
		pkDataType = "Long";
		collectionClazzName = "List";
		this.pkClazzName = this.getPkDataType(configurations);
		
		if(configurations.getDatabaseType().equalsIgnoreCase("mysql")) {	
			this.databaseType = configurations.getDatabaseType();
		}
		else if(configurations.getDatabaseType().equalsIgnoreCase("postgres") ||
				configurations.getDatabaseType().equalsIgnoreCase("postgressql")) {
			this.databaseType = configurations.getDatabaseType();
		}
		else if(configurations.getDatabaseType().equalsIgnoreCase("mongo") ||
				configurations.getDatabaseType().equalsIgnoreCase("mongodb")) {
			this.databaseType = "mongo";
		}
		else {
			this.databaseType = "";
		}
	}
	
	private String getPkDataType(Configurations configurations) {
		String pkClazzName = null;
		if(null == configurations.getPkType() || configurations.getPkType().isBlank() || configurations.getPkType().isEmpty()) {
			pkClazzName = Constants.LONG;
		}
		else if(configurations.getPkType().equalsIgnoreCase(Constants.LONG)){
			pkClazzName = Constants.LONG;
		}
		else {
			pkClazzName = Constants.INTEGER;
		}
		return pkClazzName;
	}
	
	public String getServiceCodeGenDirPath(String domainName) {
		return this.basePath+PATH_SEPARATOR
				+this.customerId+PATH_SEPARATOR
				+this.serviceName+"/src/main/java/com/service/"+this.serviceBaseName+"/"+domainName.toLowerCase()+"/";
	}
	
	public String getBaseCodeGenDirPath() {
		return this.basePath+PATH_SEPARATOR
				+this.customerId+PATH_SEPARATOR
				+this.serviceName+"/src/main/java/com/service/"+this.serviceBaseName.toLowerCase()+"/";
	}
	
	private void initializePathConfigurations(Configurations configurations, 
			List<DomainModel> domainModels,
			String customerId) {
		
//		this.basePath = "codegeneration/"+customerId+"/";
//		this.dbbasePath = dbbasePath+customerId+"/";
//		this.clientbasePath = clientbasePath+customerId+"/";
//		this.archivebasePath = archivebasePath+customerId+"/";
		
		this.domainName = this.getAggregateModelName(domainModels);
		this.serviceName = configurations.getServiceName();
		this.basePackage = "com.service";
		this.propertyFilePath = this.basePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName()+"/src/main/resources/";
		
//		this.codeGenDirPath = this.basePath+configurations.getServiceName()+"/src/main/java/com/service/"+this.domainName.toLowerCase()+"/";
//		this.testGenDirPath = this.basePath+configurations.getServiceName()+"/src/test/java/com/service/"+this.domainName.toLowerCase()+"/";
//		this.dbscriptGenDirPath = this.dbbasePath+configurations.getServiceName()+"-db/db/migration/";
//		this.clientGenDirPath = this.clientbasePath+configurations.getServiceName()+"-client/src/main/java/";
		
		this.codeGenDirPath = this.basePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName()+
				"/src/main/java/com/service/"+this.domainName.toLowerCase()+"/";
		
		this.dbscriptGenDirPath = this.dbbasePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName()+"-db/db/migration/";
		
		this.clientGenDirPath = this.clientbasePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName()+"-client/src/main/java/";
		
		//------
		
		this.serviceGenerationDir = this.basePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName();
		
		this.pomFilePath = this.basePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName()+PATH_SEPARATOR;
		
		this.dbscriptGenerationDir = this.dbbasePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName()+"-db";
		
		this.clientGenerationDir = this.clientbasePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName()+"-client";
		//------
		
		this.serviceArchiveFilename = archivebasePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+this.serviceName+"-archive.zip";;
		this.clientArchiveFilename = archivebasePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+this.serviceName+"-clientarchive.zip";
		this.dbscriptArchiveFilename = archivebasePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+this.serviceName+"-dbarchive.zip";
	}
	
	private void initializeServiceConfigurations(Configurations configurations) {
		this.serviceName = configurations.getServiceName();
		this.servicePort = configurations.getServicePort();
		this.serviceUrl = configurations.getServiceUrl();
	}

}
