package arrayscript.parser.source.reading;

import java.io.IOException;

import arrayscript.lang.Operator;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.SourceElementType;
import arrayscript.parser.source.SourceOperator;
import arrayscript.parser.source.SourceWord;

public class DefaultSourceFileReader3 implements SourceFileReader3 {
	
	private static final Operator[] OPERATORS = Operator.values();
	
	private final SourceFileReader2 second;
	
	private String currentWord;
	private int currentWordIndex;
	
	public DefaultSourceFileReader3(SourceFileReader2 second) {
		this.second = second;
	}

	@Override
	public SourceElement next() throws IOException {
		if (currentWord == null) {
			
			SourceElement next = second.next();
			
			// End of source file has been reached
			if (next == null) {
				return null;
			}
			
			// Don't do anything with strings
			if (next.getType() == SourceElementType.STRING) {
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
		if (currentWordIndex < currentWord.length() - 1) {
			for (Operator operator : OPERATORS) {
				
				int index = currentWord.indexOf(operator.getTokens(), currentWordIndex);
				if (index != -1) {
					
					// There is no point to look further because no operator can be closer
					// Don't do this with 1-character operators because there can be a 2-character operator
					// that starts with the same character.
					if (index == currentWordIndex && operator.getTokens().length() == 2) {
						currentWordIndex += 2;
						return new SourceOperator(operator);
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
				return new SourceOperator(closestOperator);
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