package arrayscript.parser.source;

import arrayscript.lang.Keyword;
import arrayscript.lang.Operator;

public class SourceWord implements SourceElement {
	
	private final String word;
	
	public SourceWord(String word) {
		this.word = word;
	}

	@Override
	public SourceElementType getType() {
		return SourceElementType.WORD;
	}

	@Override
	public Operator getOperator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not an operator, but a word");
	}

	@Override
	public String getStringContent() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a string, but a word");
	}

	@Override
	public String getWord() throws UnsupportedOperationException {
		return word;
	}
	
	@Override
	public String toString() {
		return word;
	}

	@Override
	public Keyword getKeyword() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a keyword, but a normal word");
	}
}