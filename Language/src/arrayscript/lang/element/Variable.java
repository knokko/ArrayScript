package arrayscript.lang.element;

import arrayscript.lang.value.Value;
import arrayscript.lang.var.type.Type;
import arrayscript.util.Checks;

public class Variable implements Element {
	
	private final String name;
	private final Type type;
	private final Value defaultValue;
	
	public Variable(String name, Type type, Value defaultValue) {
		Checks.notNull(name, "name");
		this.name = name;
		Checks.notNull(type, "type");
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean hasDefaultValue() {
		return defaultValue != null;
	}
	
	public Value getDefaultValue() {
		return defaultValue;
	}
}