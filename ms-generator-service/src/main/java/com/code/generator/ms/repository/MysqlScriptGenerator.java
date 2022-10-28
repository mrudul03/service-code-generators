package com.code.generator.ms.repository;

public class MysqlScriptGenerator<T> implements ScriptGenerator{

	private ColumnField<?> field;
	private static final String CHAR = "char";
	private static final String VARCHAR = "varchar";

	public MysqlScriptGenerator(ColumnField<T> field) {
		this.field = field;
	}
	
	public String generateColumnScript() {
		StringBuilder sbColumn = new StringBuilder();
		sbColumn.append("    " + field.getComputedColumnName());

		if (field.isPrimaryKey()) {

			if (!field.getDatatypeClass().equals(Integer.class)) {
				if (field.getComputedDatatype().contentEquals(VARCHAR)
						|| field.getComputedDatatype().contentEquals(CHAR)) {
					sbColumn.append(" " + field.getComputedDatatype() + "(" + field.getMaxLength() + ") primary key");
				} else {
					sbColumn.append(" " + field.getComputedDatatype() + " ");
				}
			}
			if (field.getDatatypeClass().equals(Integer.class)) {
				if (field.isReferencing()) {
					sbColumn.append(" " + "integer primary key");
				} else {
					sbColumn.append(" " + " int primary key auto_increment");
				}
			}
			if (field.isReferencing()) {
				sbColumn.append(
						" " + "references " + field.getReferenceTable() + "(" + field.getReferenceColumn() + ")");
			}
			sbColumn.append(",");
		} else {
			if (field.getComputedDatatype().contentEquals(VARCHAR) || field.getComputedDatatype().contentEquals(CHAR)) {
				sbColumn.append(" " + field.getComputedDatatype() + "(" + field.getMaxLength() + ")");
			} else {
				sbColumn.append(" " + field.getComputedDatatype() + " ");
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
