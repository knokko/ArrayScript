package arrayscript.parser.builder.var.value;

import arrayscript.lang.var.value.Value;

public class ValueBuilder {
	
	private final String unparsedValue;
	
	private Value value;
	
	public ValueBuilder(String unparsedValue) {
		this.unparsedValue = unparsedValue;
	}
	
	public ValueBuilder(Value value) {
		this.unparsedValue = null;
		this.value = value;
	}
	
	public String getUnparsedValue() {
		return unparsedValue;
	}
	
	public boolean isParsed() {
		return value != null;
	}
	
	public void setParsedValue(Value value) {
		if (this.value != null) {
			throw new IllegalStateException("The value (" + this.value + ") is already parsed");
		}
		
		this.value = value;
	}
	
	public Value getValue() {
		if (value == null) {
			throw new IllegalStateException("The value is not yet parsed");
		}
		return value;
	}
}