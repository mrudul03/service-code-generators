package com.code.generator.model;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.code.generator.ms.input.Configurations;
import com.code.generator.ms.input.Models;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileProcessService {
	
	private final String UPLOADS_DIR = "uploads";
	
	public FileProcessResponse processInputFiles(final String customerId, final List<String> fileNames) {
		log.info("Processing input files");
		Models models = null;
		Configurations configurations = null;
		FileProcessResponse fileProcessResponse = new FileProcessResponse();
		
		for(String fileName: fileNames) {
			if(fileName.toLowerCase().contains("models") ||
					fileName.toLowerCase().contains("model")) {
				// process Model
				models = this.loadModels(customerId, fileName);
			}
			else if(fileName.toLowerCase().contains("configurations") ||
					fileName.toLowerCase().contains("configuration")) {
				// process configuration
				configurations = this.loadConfigurations(customerId, fileName);
			}
			else {
				// throw incorrect files exception
				log.info("Incorrect file name");
			}
		}
		fileProcessResponse.setCustomerId(customerId);
		fileProcessResponse.setModels(models);
		fileProcessResponse.setConfigurations(configurations);
		return fileProcessResponse;
	}
	
	private Models loadModels(String customerId, String fileName) {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Models models = null;
		try {
			File modelFile = new File(UPLOADS_DIR+"/"+customerId+"/"+fileName);
			models = mapper.readValue(modelFile, Models.class);
			log.info("Got models");
		} catch (Exception e) {
			log.error("Error parsing models YAML", e);
		}
		return models;
	}

	private Configurations loadConfigurations(String customerId, String fileName) {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Configurations configurations = null;
		try {
			
			File modelFile = new File(UPLOADS_DIR+"/"+customerId+"/"+fileName);
			configurations = mapper.readValue(modelFile, Configurations.class);
			log.info("Got Configuations");
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Error parsing models YAML", e);
		}
		return configurations;
	}

}
