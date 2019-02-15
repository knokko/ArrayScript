package arrayscript.parser.source;

import arrayscript.lang.Keyword;
import arrayscript.lang.Operator;

public class SourceOperator implements SourceElement {
	
	private static final SourceOperator[] INSTANCES;
	
	static {
		
		// Don't create new instances all the time, just create 1 instance for every operator
		Operator[] OPERATORS = Operator.values();
		INSTANCES = new SourceOperator[OPERATORS.length];
		
		// Fill the instance array
		for (int index = 0; index < OPERATORS.length; index++) {
			INSTANCES[index] = new SourceOperator(OPERATORS[index]);
		}
	}
	
	public static SourceOperator getInstance(Operator operator) {
		return INSTANCES[operator.ordinal()];
	}
	
	private final Operator operator;
	
	private SourceOperator(Operator operator) {
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

	@Override
	public Keyword getKeyword() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is not a keyword, but an operator");
	}
}