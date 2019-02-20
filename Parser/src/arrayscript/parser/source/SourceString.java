package arrayscript.parser.source;

import arrayscript.lang.Keyword;
import arrayscript.lang.Operator;

public class SourceString implements SourceElement {
	
	private final String string;
	
	public SourceString(String string) {
		this.string = string;
	}

	@Override
	public SourceElementType getType() {
		return SourceElementType.STRING;
	}

	@Override
	public Operator getOperator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not an operator, but a string");
	}

	@Override
	public String getStringContent() throws UnsupportedOperationException {
		return string;
	}

	@Override
	public String getWord() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a word, but a string");
	}
	
	@Override
	public String toString() {
		return "'" + string + "'";
	}

	@Override
	public Keyword getKeyword() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a keyword, but a string");
	}

	@Override
	public double getNumber() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a number, but a string");
	}
}