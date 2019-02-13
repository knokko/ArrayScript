package arrayscript.lang;

/**
 * All operators/special symbols of the language. They will be treated differently than other words in the
 * source code.
 * @author knokko
 *
 */
public enum Operator {
	
	// Logical operators
	AND("&&"),
	OR("||"),
	NOT("!"),
	EQUALS("=="),
	NOT_EQUALS("!="),
	
	// Mathematical change operators
	INCREASE("+="),
	DECREASE("-="),
	MULTIPLY("*="),
	DIVIDE("/="),
	MOD("%="),
	INCREASE_1("++"),
	DECREASE_1("--"),
	
	// Others
	ASSIGNMENT("="),
	NEXT(","),
	PROPERTY("."),
	
	// Mathematical operators
	SUM("+"),
	SUBSTRACTION("-"),
	PRODUCT("*"),
	DIVISION("/"),
	REMAINDER("%"),
	
	// Brackets
	OPEN_BLOCK("{"),
	CLOSE_BLOCK("}"),
	OPEN_BRACKET("("),
	CLOSE_BRACKET(")"),
	ARRAY_OPEN("["),
	ARRAY_CLOSE("]")
	;
	
	private final String tokens;
	
	Operator(String tokens){
		this.tokens = tokens;
	}
	
	/**
	 * Whenever the tokens of an operator appear in the source code, the parser will assume that the
	 * programmer wants to use the operator at that place in the code.
	 * @return The character sequence to use this operator
	 */
	public String getTokens() {
		return tokens;
	}
}