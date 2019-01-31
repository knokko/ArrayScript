package arrayscript.parser.builder.var.type;

import arrayscript.lang.var.type.Type;

public class TypeBuilder {
	
	private final String typeName;
	private Type type;
	
	public TypeBuilder(String typeName) {
		this.typeName = typeName;
	}
	
	public TypeBuilder(Type type) {
		this.type = type;
		this.typeName = type.getName();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof TypeBuilder) {
			TypeBuilder t = (TypeBuilder) other;
			if (!isTypeConfirmed()) {
				throw new IllegalStateException("Can't compare types before they are confirmed");
			}
			if (!t.isTypeConfirmed()) {
				throw new IllegalArgumentException("Type of other type builder is not confirmed");
			}
			return type.equals(t.type);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		if (type != null) {
			return "confirmed " + type.getName();
		} else {
			return "unconfirmed " + typeName;
		}
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