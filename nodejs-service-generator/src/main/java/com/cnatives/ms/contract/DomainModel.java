package com.cnatives.ms.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DomainModel {
	
	private String name;
	private String entitytype;
	private List<Field> fields;
	private List<DomainModelOperation> operations = new ArrayList<>();
}
