package com.code.generator.model;

import com.code.generator.ms.input.Configurations;
import com.code.generator.ms.input.Models;

import lombok.Data;

@Data
public class FileProcessResponse {
	
	private String customerId;
	private Models models;
	private Configurations configurations;

}
