package com.code.generator.ms.input;

import java.util.List;

public class Models {

	private List<DomainModel> models;
	private String entityType;

	public List<DomainModel> getModels() {
		return models;
	}

	public void setModels(List<DomainModel> models) {
		this.models = models;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public void initializeModel() {
		for (DomainModel domainModel : this.models) {
			// domainModel.getFields().stream().
			for(Field field:domainModel.getFields()) {
				field.initialize();
			}
		}
	}
}
