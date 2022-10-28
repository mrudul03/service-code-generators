package com.cnatives.ms.generator.base;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cnatives.ms.contract.Configurations;
import com.cnatives.ms.contract.DomainModel;

import lombok.Data;

@Data
public class GeneratorMetaData {
	
	private String basePath = "codegeneration";
	private String archivebasePath = "archives";
	
	private String domainName;
	private String serviceName;
	private String serviceUrl;
	private String servicePort;
	
	private String propertyFilePath;
	private String pkClazzName;
	
	private String codeGenDirPath;
	private String collectionClazzName;
	private String databaseType;
	private String pkDataType;
	
	private String serviceGenerationDir;
	private String serviceArchiveFilename;
	
	private String pomFilePath;
	
	private final String PATH_SEPARATOR = "/";
	
	private String customerId;
	private String basePackageName;
	private String baseDirName;
	
	public void initialize(Configurations configurations, 
			List<DomainModel> domainModels, 
			String customerId) {
		this.customerId = customerId;
		this.basePackageName = configurations.getBasePackage().toLowerCase();
		this.baseDirName = this.createBaseFolderName(configurations.getBasePackage());
		this.initializePathConfigurations(configurations, domainModels, customerId);
		this.initializeDatabaseConfigurations(configurations);
		this.initializeServiceConfigurations(configurations);
	}
	
	private String createBaseFolderName(String basePackage) {
		StringBuilder sbPackageName = new StringBuilder();
		String updatebasePackage = basePackage.replace(".", " ");
		String pkgTokens [] = updatebasePackage.split(" ");
		
		for(String pkgName: pkgTokens) {
			sbPackageName.append(pkgName.toLowerCase()+"/");
		}
		
		return sbPackageName.toString();
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
		
		pkDataType = "Long";
		collectionClazzName = "List";
		this.pkClazzName = Constants.STRING;
		this.databaseType = "mongo";
	}
	
	public String getServiceCodeGenDirPath(String domainName) {
		return this.basePath+PATH_SEPARATOR
				+this.customerId+PATH_SEPARATOR
				+this.serviceName+"/src/main/java/"+this.baseDirName+domainName.toLowerCase()+"/";
	}
	
	public String getBaseCodeGenDirPath() {
		
		return this.basePath+PATH_SEPARATOR
				+this.customerId+PATH_SEPARATOR
				+this.serviceName+"/src/main/java/"+this.baseDirName;
	}
	
	private void initializePathConfigurations(Configurations configurations, 
			List<DomainModel> domainModels,
			String customerId) {
		
		if(null != configurations.getServiceBaseName()) {
			this.domainName = configurations.getServiceBaseName();
		}
		else {
			this.domainName = this.getAggregateModelName(domainModels);
		}
		this.serviceName = configurations.getServiceName();
		this.propertyFilePath = this.basePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName()+"/src/main/resources/";
		
		this.codeGenDirPath = this.basePath+PATH_SEPARATOR
			+this.customerId+PATH_SEPARATOR
			+this.serviceName+"/src/main/java/"+this.baseDirName;
		
		//------
		
		this.serviceGenerationDir = this.basePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName();
		
		this.pomFilePath = this.basePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+configurations.getServiceName()+PATH_SEPARATOR;
		
		//------
		
		this.serviceArchiveFilename = archivebasePath+PATH_SEPARATOR+customerId+
				PATH_SEPARATOR+this.serviceName+"-archive.zip";;
	}
	
	private void initializeServiceConfigurations(Configurations configurations) {
		this.serviceName = configurations.getServiceName();
		this.servicePort = configurations.getServicePort();
		this.serviceUrl = configurations.getServiceUrl();
	}

}
