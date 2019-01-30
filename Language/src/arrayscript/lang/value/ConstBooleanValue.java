package arrayscript.lang.value;

public class ConstBooleanValue implements BooleanValue {
	
	public static final ConstBooleanValue TRUE = new ConstBooleanValue(true);
	public static final ConstBooleanValue FALSE = new ConstBooleanValue(false);
	
	private final boolean value;
	
	private ConstBooleanValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
}