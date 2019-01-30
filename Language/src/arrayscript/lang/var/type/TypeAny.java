package arrayscript.lang.var.type;

import arrayscript.lang.value.Value;

public class TypeAny implements Type {
	
	TypeAny(){}

	@Override
	public boolean canHaveValue(Value value) {
		return true;
	}
}