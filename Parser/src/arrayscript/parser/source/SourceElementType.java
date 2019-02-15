package arrayscript.parser.source;

/**
 * All the kinds of source elements that are distinguished by the parser.
 * @author knokko
 *
 */
public enum SourceElementType {
	
	/**
	 * A word basically represents the 'default' source element type. All source elements that are not
	 * strings or operators are words. So all variable names, variable types and namespaces are 'words'. What
	 * this word exactly is will be determined in a later parsing stage.
	 */
	WORD,
	
	/**
	 * Keywords are words that are reserved by the language and have a special meaning. Programmers are not
	 * allowed to use keywords as names for types or variables.
	 */
	KEYWORD,
	
	/**
	 * Strings are source elements that are written between "s and 's. Strings are distinguished early by the
	 * parser because the parser so that it can safely ignore the content of the string and thus will not be
	 * confused by whatever is in the string.
	 */
	STRING,
	
	/**
	 * Operators are sequences of characters that have a special meaning in this language (and most other
	 * languages). For instance, =, +, *= and && are operators.
	 */
	OPERATOR
}