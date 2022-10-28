package com.cnatives.ms.generator.msservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DomainOperationParam {
	
	@Getter
	private String paramtype;
	
	@Getter
	private String paramvariable;

}
