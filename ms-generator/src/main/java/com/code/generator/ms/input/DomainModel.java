package com.code.generator.ms.input;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class DomainModel {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DomainModel.class);

	private String name;
	private String entitytype;
	private List<Field> fields;
	
	private static final String NAME_REQUIRED = "Model should have a name.";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEntitytype() {
		return entitytype;
	}
	public void setEntitytype(String entitytype) {
		this.entitytype = entitytype;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public static Models loadModelConfiguration(String modelFilePath) {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Models models = null;
		try {
            models = mapper.readValue(new File(modelFilePath), Models.class);
            LOGGER.info("Got models:"+models.getModels().size());
        } catch (Exception e) {
            LOGGER.error("Error parsing models YAML", e);
        }
		return models;
	}
	
	public ValidationResult validateDomainModel() {
		ValidationResult validationResult = new ValidationResult();
		if(!this.isNameValid()) {
			validationResult.add(NAME_REQUIRED);
		}
		
		for(Field field: this.fields) {
			validationResult.addAll(field.validate().getErrors());
		}
		
		return validationResult;
	}
	
	private boolean isNameValid() {
		return (null != this.name && !this.name.trim().isEmpty());
	}
	
	public int getChildCount() {
		int count = 0;
		for(Field field:fields) {
			if(!field.isPrimitive() &&
					null != field.getRelation() &&
					!field.getRelation().trim().isEmpty()) {
				count = count+1;
			}
		}
		return count;
	}
	
}
