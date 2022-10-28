package com.cnatives.ms.generator.mscontroller;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DomainModel {
	
	private String name;
	private String domainName;
	private String entitytype;
	private List<Field> fields;
	private List<DomainModelOperation> operations = new ArrayList<>();
}
