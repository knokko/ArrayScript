package arrayscript.lang.var.type;

import arrayscript.lang.var.value.Value;

public class TypeString implements Type {
	
	TypeString(){}

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
		return "string";
	}
}