package arrayscript.lang.var.type;

import arrayscript.lang.var.value.Value;

public class TypeNumber implements Type {
	
	TypeNumber(){}

	@Override
	public boolean canHaveValue(Value value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrimitive() {
		return true;
	}

	@Override
	public String getName() {
		return "number";
	}
}