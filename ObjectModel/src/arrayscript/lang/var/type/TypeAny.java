package arrayscript.lang.var.type;

import arrayscript.lang.var.value.Value;

public class TypeAny implements Type {
	
	TypeAny(){}

	@Override
	public boolean canHaveValue(Value value) {
		return true;
	}

	@Override
	public String getName() {
		return "any";
	}

	@Override
	public boolean isPrimitive() {
		return true;
	}
}