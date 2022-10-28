package com.code.generator.ms.input;

import java.util.List;
import java.util.Set;

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
	private String dbscriptGenDirPath;
	private String propertyFilePath;
	private Class<?> pkClazz;
	private Class<?> collectionClazz;
	private String databaseType;
	
	public void initialize(Configurations configurations, String customerId, List<DomainModel> domainModels) {
		this.basePath = "codegeneration/"+customerId+"/";
		this.resourceName = this.getAggregateModelName(domainModels);
		this.serviceName = configurations.getServiceName();
		this.codeGenDirPath = this.basePath+configurations.getServiceName()+"/src/main/java/com/service/"+this.resourceName.toLowerCase()+"/";
		this.dbscriptGenDirPath = this.basePath+configurations.getServiceName()+"/src/main/resources/db/migration/";
		this.propertyFilePath = this.basePath+configurations.getServiceName()+"/src/main/resources/";
		log.info("Configurations ............ PKType--------:"+configurations.getPkType());
		if(configurations.getPkType().equalsIgnoreCase(Constants.STRING)) {
			pkClazz = String.class;
		} else {
			pkClazz = Integer.class;
		}
		if(null != configurations.getCollectionType() 
				&& configurations.getCollectionType().equalsIgnoreCase("List")) {
			collectionClazz = List.class;
		} else {
			collectionClazz = Set.class;
		}
		if(configurations.getDatabaseType().equalsIgnoreCase("mysql") || 
				configurations.getDatabaseType().equalsIgnoreCase("postgres") ||
				configurations.getDatabaseType().equalsIgnoreCase("postgressql")) {	
			this.databaseType = configurations.getDatabaseType();
		}
		else {
			this.databaseType = "";
		}
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

}
