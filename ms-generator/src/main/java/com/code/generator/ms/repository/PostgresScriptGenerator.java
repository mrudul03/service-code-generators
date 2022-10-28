package com.code.generator.ms.repository;

public class PostgresScriptGenerator<T> implements ScriptGenerator{

	private ColumnField<?> field;
	private static final String CHAR = "char";
	private static final String VARCHAR = "varchar";

	public PostgresScriptGenerator(ColumnField<T> field) {
		this.field = field;
	}

	public String generateColumnScript() {
		String notNull = "";
		if(!field.isNullable()) {
			notNull = "not null";
		}
		else {
			notNull = "";
		}
		StringBuilder sbColumn = new StringBuilder();
		sbColumn.append("    " + field.getComputedColumnName());

		if (field.isPrimaryKey()) {

			if (field.getComputedDatatype().contentEquals(VARCHAR)
					|| field.getComputedDatatype().contentEquals(CHAR)) {
				sbColumn.append(" " + field.getComputedDatatype() + "(" + field.getMaxLength() + ") primary key");
			} 
			else if (field.getDatatypeClass().equals(Integer.class) ||
					field.getDatatypeClass().equals(Long.class)) {
				
				if (field.isReferencing()) {
					sbColumn.append(" " + "int primary key");
				} else {
					sbColumn.append(" " + "serial primary key");
				}
			}
			if (field.isReferencing()) {
				sbColumn.append(
						" " + "references " + field.getReferenceTable() + "(" + field.getReferenceColumn() + ")");
			}
			sbColumn.append(",");
		} else {
			if (field.getComputedDatatype().contentEquals(VARCHAR) || field.getComputedDatatype().contentEquals(CHAR)) {
				sbColumn.append(" " + field.getComputedDatatype() + "(" + field.getMaxLength() + ") " +notNull+ " ");
			} else {
				sbColumn.append(" " + field.getComputedDatatype() + " "+notNull+ " ");
			}
			if (field.isReferencing()) {
				sbColumn.append(
						" " + "references " + field.getReferenceTable() + "(" + field.getReferenceColumn() + ")");
			}
			sbColumn.append(",");

			// adding list key column
			if (field.isReferencing() && null != field.getCollectionClazz()
					&& field.getCollectionClazz().getSimpleName().equals("List")) {

				if (field.getComputedDatatype().contentEquals(VARCHAR)
						|| field.getComputedDatatype().contentEquals(CHAR)) {
					sbColumn.append("\n" + "    " + field.getComputedColumnName() + "_key "
							+ field.getComputedDatatype() + "(" + field.getMaxLength() + "),");
				} else {
					sbColumn.append("\n" + "    " + field.getComputedColumnName() + "_key "
							+ field.getComputedDatatype() + ",");
				}
			}
		}

		return sbColumn.toString();

	}

}
