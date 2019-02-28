package arrayscript.lang.var.type;

import arrayscript.lang.var.value.Value;

public class TypeFloat32 implements Type {
	
	TypeFloat32(){}

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
		return "float32";
	}
}