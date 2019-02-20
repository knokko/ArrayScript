package arrayscript.parser.source;

import arrayscript.lang.Keyword;
import arrayscript.lang.Operator;

public class SourceNumber implements SourceElement {
	
	private final double number;
	
	public SourceNumber(double number) {
		this.number = number;
	}

	@Override
	public SourceElementType getType() {
		return SourceElementType.NUMBER;
	}

	@Override
	public Operator getOperator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not an operator, but a number");
	}

	@Override
	public String getStringContent() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a string, but a number");
	}

	@Override
	public String getWord() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a word, but a number");
	}

	@Override
	public double getNumber() throws UnsupportedOperationException {
		return number;
	}

	@Override
	public Keyword getKeyword() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a keyword, but a number");
	}
	
	@Override
	public String toString() {
		return Double.toString(number);
	}
}