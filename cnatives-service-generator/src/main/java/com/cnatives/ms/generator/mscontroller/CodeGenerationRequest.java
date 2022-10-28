package com.cnatives.ms.generator.mscontroller;

import java.util.List;

import lombok.Data;

@Data
public class CodeGenerationRequest {
	
	private Configurations configurations;
	private List<DomainModel> domains;
	private List<DomainModelForm> domainforms;
}
