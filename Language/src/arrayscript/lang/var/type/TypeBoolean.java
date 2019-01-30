package arrayscript.lang.var.type;

import arrayscript.lang.var.value.BooleanValue;
import arrayscript.lang.var.value.Value;

public class TypeBoolean implements Type {
	
	TypeBoolean() {}

	@Override
	public boolean canHaveValue(Value value) {
		return value instanceof BooleanValue;
	}
}