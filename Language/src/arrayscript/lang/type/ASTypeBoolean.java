package arrayscript.lang.type;

import arrayscript.lang.value.ASBooleanValue;
import arrayscript.lang.value.ASValue;

public class ASTypeBoolean implements ASType {
	
	public static final ASTypeBoolean BOOLEAN = new ASTypeBoolean();
	
	private ASTypeBoolean() {}

	@Override
	public boolean canHaveValue(ASValue value) {
		return value instanceof ASBooleanValue;
	}
}