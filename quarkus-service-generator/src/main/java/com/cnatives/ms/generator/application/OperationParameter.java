package com.cnatives.ms.generator.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class OperationParameter {
	
	@Getter
	private String paramtype;
	
	@Getter
	private String paramname;
	
	@Getter
	private String paramvariable;
}
