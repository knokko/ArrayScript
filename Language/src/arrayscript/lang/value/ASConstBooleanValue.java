package arrayscript.lang.value;

public class ASConstBooleanValue implements ASBooleanValue {
	
	public static final ASConstBooleanValue TRUE = new ASConstBooleanValue(true);
	public static final ASConstBooleanValue FALSE = new ASConstBooleanValue(false);
	
	private final boolean value;
	
	private ASConstBooleanValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
}