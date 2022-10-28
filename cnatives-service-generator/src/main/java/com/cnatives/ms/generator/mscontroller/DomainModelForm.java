package com.cnatives.ms.generator.mscontroller;

import java.util.List;

import lombok.Data;

@Data
public class DomainModelForm {
	
	private String name;
	private String domainName;
	private String entitytype;
	private List<Field> fields;

}
