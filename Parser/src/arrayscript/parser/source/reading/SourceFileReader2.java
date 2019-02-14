package arrayscript.parser.source.reading;

import java.io.IOException;

import arrayscript.parser.source.SourceElement;
import arrayscript.parser.util.ParsingException;

/**
 * The second source reader in the chain of source file readers. This reader will take the result of the first
 * source file reader in the chain and extract all strings from the words. So 'words' returned by the first
 * source file reader that contain a string, will be broken into 3 pieces: the word before the string, the
 * string and the word after the string.
 * @author knokko
 *
 */
interface SourceFileReader2 {
	
	/**
	 * Determines the next word or string that is read from the source. It will process the words returned by
	 * the first source file reader one by one. But notice that this reader might return more than 1 source
	 * element from 1 word returned by the first source file reader. This method will return null if the end
	 * of the source has been reached.
	 * @return The next string or word that is read from the source code, or null if the end of source has
	 * been reached
	 * @throws IOException if the first reader throws an IOException
	 * @throws ParsingException If the source code is incorrect and thus can't be parsed
	 */
	SourceElement next() throws IOException, ParsingException;
}