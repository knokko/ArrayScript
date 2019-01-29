package arrayscript.lang.type;

import arrayscript.lang.value.ASValue;

public class ASTypeAny implements ASType {

	@Override
	public boolean canHaveValue(ASValue value) {
		return true;
	}
}