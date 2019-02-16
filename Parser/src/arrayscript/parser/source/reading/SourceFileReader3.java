package arrayscript.parser.source.reading;

import java.io.IOException;

import arrayscript.lang.Operator;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.SourceOperator;
import arrayscript.parser.source.SourceWord;
import arrayscript.parser.util.ParsingException;

/**
 * The third source reader in the chain of source file readers. Instances of this interface take the
 * result of the second source reader in the chain as input and distinguishes the operators from the other
 * 'words'.
 * @author knokko
 *
 */
class SourceFileReader3 {
	
	private static final Operator[] OPERATORS = Operator.values();
	
	private final SourceFileReader2 second;
	
	private String currentWord;
	private int currentWordIndex;
	
	public SourceFileReader3(SourceFileReader2 second) {
		this.second = second;
	}

	/**
	 * Determines the next word, string or operator that appears in the source code. It will process the 
	 * result that was returned by the second source file reader and search for operators in the words. 
	 * Notice that this reader might return more than 1 result for a single result of the second source file reader. This
	 * method will return null if the end of the source file has been reached.
	 * @return The next string, word or operator that appears in the source code, or null if the end of the 
	 * source file has been reached.
	 * @throws IOException If the second source file reader throws an IOException
	 * @throws ParsingException If the source code is incorrect and thus can't be parsed
	 */
	public SourceElement next() throws IOException, ParsingException {
		if (currentWord == null) {
			
			SourceElement next = second.next();
			
			// End of source file has been reached
			if (next == null) {
				return null;
			}
			
			// Don't do anything with strings
			if (next.isString()) {
				return next;
			}
			
			// Now that the next is not a string, it must be a word or operator
			// We will have to determine which one
			else {
				currentWord = next.getWord();
				currentWordIndex = 0;
				// The rest of the method will deal with this word or operator
			}
		}
		
		int closestOperatorIndex = Integer.MAX_VALUE;
		Operator closestOperator = null;
		
		// Search for the closest operator
		if (currentWordIndex < currentWord.length()) {
			for (Operator operator : OPERATORS) {
				
				int index = currentWord.indexOf(operator.getTokens(), currentWordIndex);
				if (index != -1) {
					
					// There is no point to look further because no operator can be closer
					// Don't do this with 1-character operators because there can be a 2-character operator
					// that starts with the same character.
					if (index == currentWordIndex && operator.getTokens().length() == 2) {
						currentWordIndex += 2;
						return SourceOperator.getInstance(operator);
					}
					
					// 2-character operators have priority over 1-character operators
					// So they use <= instead of <
					if (operator.getTokens().length() == 1) {
						if (index < closestOperatorIndex) {
							closestOperatorIndex = index;
							closestOperator = operator;
						}
					} else {
						
						// Length must be 2 because all operators have a length of 1 or 2
						if (index <= closestOperatorIndex) {
							closestOperatorIndex = index;
							closestOperator = operator;
						}
					}
				}
			}
		}
		
		// The current word doesn't contain any (more) operators, so we can just return what is left of it
		if (closestOperator == null) {
			String result = currentWord.substring(currentWordIndex);
			
			// The currentWordIndex will be set to 0 at the start of the next call to next()
			currentWord = null;
			
			// Don't return empty strings!
			if (!result.isEmpty()) {
				return new SourceWord(result);
			} else {
				return next();
			}
		}
		
		// Return the part until the next operator or the next operator
		else {
			
			// We have arrived at the operator
			if (closestOperatorIndex == currentWordIndex) {
				currentWordIndex += closestOperator.getTokens().length();
				if (currentWordIndex >= currentWord.length()) {
					
					// The currentWordIndex will be set to 0 at the start of the next call to next()
					currentWord = null;
				}
				return SourceOperator.getInstance(closestOperator);
			}
			
			// We have not yet arrived at the operator
			else {
				String result = currentWord.substring(currentWordIndex, closestOperatorIndex);
				
				// The next call to next() will find the operator again
				currentWordIndex = closestOperatorIndex;
				
				// Don't return empty strings!
				if (!result.isEmpty()) {
					return new SourceWord(result);
				} else {
					return next();
				}
			}
		}
	}
}