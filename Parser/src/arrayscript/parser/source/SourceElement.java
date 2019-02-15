package arrayscript.parser.source;

import arrayscript.lang.Keyword;
import arrayscript.lang.Operator;

/**
 * Represents an element in the source code. Those elements must be words, strings or operators. Use the
 * getType() method to find out what kind of source element this is. See SourceElementType for more
 * information about the source element types.
 * @author knokko
 *
 */
public interface SourceElement {
	
	/**
	 * @return The type of this source element
	 */
	SourceElementType getType();
	
	/**
	 * Determines whether this source element is a word, or not.
	 * @return true if and only if the type of this source element is SourceElementType.WORD
	 */
	default boolean isWord() {
		return getType() == SourceElementType.WORD;
	}
	
	/**
	 * Determines whether this source element is a keyword, or not.
	 * @return true if and only if the type of this source element is SourceElementType.KEYWORD
	 */
	default boolean isKeyword() {
		return getType() == SourceElementType.KEYWORD;
	}
	
	/**
	 * Determines whether this source element is a string, or not.
	 * @return true if and only if the type of this source element is SourceElementType.STRING
	 */
	default boolean isString() {
		return getType() == SourceElementType.STRING;
	}
	
	/**
	 * Determines whether this source element is an operator, or not.
	 * @return true if and only if the type of this source element is SourceElementType.OPERATOR
	 */
	default boolean isOperator() {
		return getType() == SourceElementType.OPERATOR;
	}
	
	/**
	 * Use this method to get the operator that this source element represents after you checked that
	 * getType() returns SourceElementType.OPERATOR.
	 * @return The operator if this source element represents an operator
	 * @throws UnsupportedOperationException If this source element doesn't represent an operator
	 */
	Operator getOperator() throws UnsupportedOperationException;
	
	/**
	 * Use this method to get the content of the string after you checked that getType() returns
	 * SourceElementType.STRING.
	 * @return The content of the string that is represented by this source element if this source element
	 * represents a string
	 * @throws UnsupportedOperationException If this source element doesn't represent a string
	 */
	String getStringContent() throws UnsupportedOperationException;
	
	/**
	 * Use this method to get the 'word' that this source element represents after you checked that getType()
	 * returns SourceElementType.WORD.
	 * @return The word that is represented by this source element if this source element represents a word
	 * @throws UnsupportedOperationException If this source element doesn't represent a word
	 */
	String getWord() throws UnsupportedOperationException;
	
	/**
	 * Use this method to get the keyword that this source elements represents after you checked that
	 * isKeyword() returns true.
	 * @return The keyword that this source element represents if this source element represents a keyword
	 * @throws UnsupportedOperationException If this source element doesn't represent a keyword
	 */
	Keyword getKeyword() throws UnsupportedOperationException;
}