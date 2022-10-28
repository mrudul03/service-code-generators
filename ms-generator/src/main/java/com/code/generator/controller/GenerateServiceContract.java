package com.code.generator.controller;

import java.util.List;

import com.code.generator.ms.input.Configurations;
import com.code.generator.ms.input.DomainModel;

import lombok.Data;

@Data
public class GenerateServiceContract {
	
	private Configurations configurations;
	private List<DomainModel> models;

}
