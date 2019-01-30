package arrayscript.lang.var.type;

import arrayscript.lang.value.BooleanValue;
import arrayscript.lang.value.Value;

public class TypeBoolean implements Type {
	
	TypeBoolean() {}

	@Override
	public boolean canHaveValue(Value value) {
		return value instanceof BooleanValue;
	}
}