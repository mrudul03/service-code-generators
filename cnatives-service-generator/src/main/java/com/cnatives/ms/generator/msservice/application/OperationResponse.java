package com.cnatives.ms.generator.msservice.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class OperationResponse {
	
	@Getter
	private String className;
	
	@Getter
	private String dataType;
	
	@Getter
	private String variableName;

}
