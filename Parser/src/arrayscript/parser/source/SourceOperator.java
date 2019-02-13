package arrayscript.parser.source;

import arrayscript.lang.Operator;

public class SourceOperator implements SourceElement {
	
	private final Operator operator;
	
	public SourceOperator(Operator operator) {
		this.operator = operator;
	}

	@Override
	public SourceElementType getType() {
		return SourceElementType.OPERATOR;
	}

	@Override
	public Operator getOperator() throws UnsupportedOperationException {
		return operator;
	}

	@Override
	public String getStringContent() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a string, but an operator");
	}

	@Override
	public String getWord() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a word, but an operator");
	}
	
	@Override
	public String toString() {
		return operator.getTokens();
	}
}