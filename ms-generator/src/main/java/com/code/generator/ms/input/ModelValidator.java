package com.code.generator.ms.input;

import java.util.List;

public class ModelValidator {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(ModelValidation.class);
	
	private static final String MODEL_REQUIRED = "Atlease one model definition is required.";
	private static final String AGGREGATE_REQUIRED = "Atlease one aggregate model definition is required.";
	private static final String CHILDENTITY_REQUIRED = "Child entity(s) are required.";
	private static final String AGGREGATE = "aggregate";
	private static final String CHILD_ENTITY = "ChildEntity";
	
	public ValidationResult validateDomainModels(final List<DomainModel> models) {
		ValidationResult validationResult = new ValidationResult();
		if(models.size() == 0) {
			validationResult.add(MODEL_REQUIRED);
		}
		for(DomainModel domainModel: models) {
			validationResult.addAll(domainModel.validateDomainModel().getErrors());
		}
		if(models.size() > 1 && isAggregateMissing(models)) {
			validationResult.add(AGGREGATE_REQUIRED);
		}
		//
		if(!isChildEntityDefined(models)) {
			validationResult.add(CHILDENTITY_REQUIRED);
		}
		if(!areChildEntityAvailable(models)) {
			validationResult.add(CHILDENTITY_REQUIRED);
		}
		
		return validationResult;
	}
	
	private boolean isAggregateMissing(final List<DomainModel> models) {
		boolean isAggregateMissing = true;
		for(DomainModel domainModel: models) {
			if(null != domainModel.getEntitytype() && 
					domainModel.getEntitytype().trim().equalsIgnoreCase(AGGREGATE)) {
				
				isAggregateMissing = false;
				break;
			}
		}
		return isAggregateMissing;
	}
	
	private boolean isChildEntityDefined(final List<DomainModel> models) {
		boolean isValid = true;
		DomainModel aggregateModel = this.getAggregateModel(models);
		if(!isAggregateWithChilds(aggregateModel)) {
			isValid = false;
		}
		
		return isValid;
	}
	
	private DomainModel getAggregateModel(final List<DomainModel> models) {
		DomainModel aggregateModel = null;
		for(DomainModel domainModel:models) {
			if(!domainModel.getEntitytype().isEmpty() &&
					domainModel.getEntitytype().equalsIgnoreCase(AGGREGATE)) {
				aggregateModel = domainModel;
				break;
			}
		}
		return aggregateModel;
	}
	
	private boolean isAggregateWithChilds(final DomainModel domainModel) {
		boolean isAggregateWithRef = false;
		for(Field field: domainModel.getFields()) {
			if(!field.isPrimitive() && 
					null != field.getRelation() &&
					!field.getRelation().isEmpty()) {
				isAggregateWithRef = true;
				break;
			}
		}
		
		return isAggregateWithRef;
	}
	
	private boolean areChildEntityAvailable(
			final List<DomainModel> models) {
		
		final DomainModel aggModel = this.getAggregateModel(models);
		int aggChildCount = aggModel.getChildCount();
		
		boolean isChildAvailable = true;
		
		int totalChildCount = 0;
		for(DomainModel domainModel:models) {
			if(domainModel.getName().equalsIgnoreCase(aggModel.getName())) {
				continue;
			}
			if(null != domainModel.getEntitytype() &&
					domainModel.getEntitytype().trim().equalsIgnoreCase(CHILD_ENTITY)) {
				totalChildCount = totalChildCount+1;
			}
			
		}
		isChildAvailable = (aggChildCount == totalChildCount); 
		return isChildAvailable;
	}

}
