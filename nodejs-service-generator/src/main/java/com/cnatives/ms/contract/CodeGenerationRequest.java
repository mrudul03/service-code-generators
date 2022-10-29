package com.cnatives.ms.contract;

import java.util.List;

import lombok.Data;

@Data
public class CodeGenerationRequest {
	
	private Configurations configurations;
	private List<DomainModel> domains;
	private List<DomainModelForm> domainforms;
}
