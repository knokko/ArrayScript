package arrayscript.parser.builder.var.type;

import arrayscript.lang.var.type.Type;

public class TypeBuilder {
	
	private final String typeName;
	private Type type;
	
	public TypeBuilder(String typeName) {
		this.typeName = typeName;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public boolean isTypeConfirmed() {
		return type != null;
	}
	
	public void confirmType(Type type) {
		if (type != null) {
			throw new IllegalStateException("The type is already " + this.type);
		}
		this.type = type;
	}
	
	public Type getType() {
		if (type == null) {
			throw new IllegalStateException("Type has not been confirmed yet.");
		}
		return type;
	}
}