package com.cnatives.ms.generator.base;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;

public class CommonUtil {

	public static String mapDatatype(String datatype) {
		
		String mappedDatatype = null;
		if(datatype.toLowerCase().equalsIgnoreCase(Constants.INT) || 
				datatype.toLowerCase().equalsIgnoreCase(Constants.SHORT) ||
				datatype.toLowerCase().equalsIgnoreCase(Constants.NUMBER)) {
			mappedDatatype = Integer.class.getSimpleName();
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.LONG)) {
			mappedDatatype = Long.class.getSimpleName();
		} 
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.FLOAT)) {
			mappedDatatype = Float.class.getSimpleName();
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.DOUBLE)) {
			mappedDatatype = Double.class.getSimpleName();
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.STRING) || 
				datatype.toLowerCase().equalsIgnoreCase(Constants.TEXT)) {
			mappedDatatype = String.class.getSimpleName();
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.BYTE)) {
			mappedDatatype = Byte.class.getSimpleName();
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.CHAR)) {
			mappedDatatype = Character.class.getSimpleName();
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.DATE)) {
			mappedDatatype = Timestamp.class.getSimpleName();
		}
		else if(datatype.toLowerCase().equalsIgnoreCase(Constants.BOOLEAN)) {
			mappedDatatype = Boolean.class.getSimpleName();
		}
		else {
			mappedDatatype = StringUtils.capitalize(datatype);
		}
		
		return mappedDatatype;
	}
	
}
