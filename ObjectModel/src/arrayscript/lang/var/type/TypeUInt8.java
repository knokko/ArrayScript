package arrayscript.lang.var.type;

import arrayscript.lang.var.value.Value;

public class TypeUInt8 implements Type {
	
	TypeUInt8(){}

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
		return "uint8";
	}
}