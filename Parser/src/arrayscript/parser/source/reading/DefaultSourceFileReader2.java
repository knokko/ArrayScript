package arrayscript.parser.source.reading;

import java.io.IOException;

import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.SourceString;
import arrayscript.parser.source.SourceWord;
import arrayscript.parser.util.ParsingException;

class DefaultSourceFileReader2 implements SourceFileReader2 {
	
	private final SourceFileReader1 first;
	
	private String currentWord;
	private int currentWordIndex;
	
	public DefaultSourceFileReader2(SourceFileReader1 first) {
		this.first = first;
	}

	@Override
	public SourceElement next() throws IOException, ParsingException {
		
		// This happens at the first call to next or after the previous word has been processed
		if (currentWord == null) {
			currentWord = first.next();
			
			// This is only possible if the first reader returned null, and thus the end of source is reached
			if (currentWord == null) {
				return null;
			}
			
			// Start at the beginning of the new word to process
			currentWordIndex = 0;
		}
		
		char currentChar = currentWord.charAt(currentWordIndex);
		
		// Return the next double quoted string
		if (currentChar == '"') {
			int indexEnd = currentWord.indexOf('"', currentWordIndex + 1);
			
			// The next steps are safe because the first source file reader must not return unterminated
			// strings. The + 1 is necessary because we don't include the string delimiters in the result.
			String result = currentWord.substring(currentWordIndex + 1, indexEnd);
			currentWordIndex = indexEnd + 1;
			
			// When we reach the end of the current word, we set it to null to let the first part of the
			// next call to next() handle it.
			if (currentWordIndex >= currentWord.length()) {
				currentWord = null;
			}
			
			return new SourceString(result);
		}
		
		// Return the next single quoted string
		if (currentChar == '\'') {
			int indexEnd = currentWord.indexOf('\'', currentWordIndex + 1);
			
			// The next steps are safe because the first source file reader must not return unterminated
			// strings. The + 1 is necessary because we don't include the string delimiters in the result.
			String result = currentWord.substring(currentWordIndex + 1, indexEnd);
			currentWordIndex = indexEnd + 1;
			
			// When we reach the end of the current word, we set it to null to let the first part of the
			// next call to next() handle it.
			if (currentWordIndex >= currentWord.length()) {
				currentWord = null;
			}
			
			return new SourceString(result);
		}
		
		// The result of this call will not be a string
		int indexSingle = currentWord.indexOf('\'', currentWordIndex + 1);
		int indexDouble = currentWord.indexOf('"', currentWordIndex + 1);
		
		// The current word doesn't contain any more strings, so just return what is left
		if (indexSingle == -1 && indexDouble == -1) {
			String result = currentWord.substring(currentWordIndex);
			currentWord = null;
			return new SourceWord(result);
		}
		
		// The current word doesn't contain any more single quote strings, but does contain double quote strings
		else if (indexSingle == -1 || (indexDouble < indexSingle && indexDouble != -1)) {
			String result = currentWord.substring(currentWordIndex, indexDouble);
			
			// The double quote musts be read in the next call to this method
			currentWordIndex = indexDouble;
			
			return new SourceWord(result);
		}
		
		// The current word doesn't contain any more double quote strings or a single quote string comes first
		else if (indexDouble == -1 || (indexSingle < indexDouble && indexSingle != -1)) {
			String result = currentWord.substring(currentWordIndex, indexSingle);
			
			// The single quote musts be read in the next call to this method
			currentWordIndex = indexSingle;
			
			return new SourceWord(result);
		}
		
		// Should not occur
		else {
			throw new Error("indexSingle is " + indexSingle + " and indexDouble is " + indexDouble);
		}
	}
}