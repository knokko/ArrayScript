package arrayscript.parser.builder.var.value;

import java.util.List;

import arrayscript.lang.var.value.Value;
import arrayscript.parser.source.SourceElement;

public class ValueBuilder {
	
	private final List<SourceElement> unparsedValue;
	
	private Value value;
	
	public ValueBuilder(List<SourceElement> unparsedValue) {
		this.unparsedValue = unparsedValue;
	}
	
	public ValueBuilder(Value value) {
		this.unparsedValue = null;
		this.value = value;
	}
	
	public List<SourceElement> getUnparsedValue() {
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