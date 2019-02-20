package arrayscript.parser.source;

import arrayscript.lang.Keyword;
import arrayscript.lang.Operator;

public class SourceKeyword implements SourceElement {
	
	private static final SourceKeyword[] INSTANCES;
	
	static {
		
		// Create a single instance for every keyword
		Keyword[] KEYWORDS = Keyword.values();
		INSTANCES = new SourceKeyword[KEYWORDS.length];
		
		// Now actually fill the array
		for (int index = 0; index < INSTANCES.length; index++) {
			INSTANCES[index] = new SourceKeyword(KEYWORDS[index]);
		}
	}
	
	public static SourceKeyword getInstance(Keyword keyword) {
		return INSTANCES[keyword.ordinal()];
	}
	
	private final Keyword keyword;
	
	private SourceKeyword(Keyword keyword) {
		this.keyword = keyword;
	}

	@Override
	public SourceElementType getType() {
		return SourceElementType.KEYWORD;
	}

	@Override
	public Operator getOperator() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not an operator, but a keyword");
	}

	@Override
	public String getStringContent() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a string, but a keyword");
	}

	@Override
	public String getWord() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a normal word, but a keyword");
	}

	@Override
	public Keyword getKeyword() throws UnsupportedOperationException {
		return keyword;
	}
	
	@Override
	public String toString() {
		return keyword.name();
	}

	@Override
	public double getNumber() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a number, but a keyword");
	}
}