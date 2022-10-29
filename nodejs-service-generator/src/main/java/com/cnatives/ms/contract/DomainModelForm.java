package com.cnatives.ms.contract;

import java.util.List;

import lombok.Data;

@Data
public class DomainModelForm {
	
	private String name;
	private String entitytype;
	private List<Field> fields;

}
