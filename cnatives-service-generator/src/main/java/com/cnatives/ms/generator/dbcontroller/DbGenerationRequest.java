package com.cnatives.ms.generator.dbcontroller;

import java.util.List;

import com.cnatives.ms.generator.mscontroller.Configurations;
import com.cnatives.ms.generator.mscontroller.DomainModel;

import lombok.Data;
@Data
public class DbGenerationRequest {

	private Configurations configurations;
	private List<DomainModel> domains;
}
