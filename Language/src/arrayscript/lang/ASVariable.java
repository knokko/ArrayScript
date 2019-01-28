package arrayscript.lang;

import arrayscript.lang.type.ASType;
import arrayscript.lang.value.ASValue;
import arrayscript.util.Checks;

public class ASVariable implements ASElement {
	
	private final String name;
	private final ASType type;
	private final ASValue defaultValue;
	
	public ASVariable(String name, ASType type, ASValue defaultValue) {
		Checks.notNull(name, "name");
		this.name = name;
		Checks.notNull(type, "type");
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	public String getName() {
		return name;
	}
	
	public ASType getType() {
		return type;
	}
	
	public boolean hasDefaultValue() {
		return defaultValue != null;
	}
	
	public ASValue getDefaultValue() {
		return defaultValue;
	}
}