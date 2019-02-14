package arrayscript.parser.source.reading;

import java.io.IOException;

import arrayscript.parser.source.SourceElement;
import arrayscript.parser.util.ParsingException;

/**
 * The third and last source reader in the chain of source file readers. Instances of this interface take the
 * result of the second source reader in the chain as input and distinguishes the operators from the other
 * 'words'. Currently, the output of these source file readers are directly used by the final source file
 * reader that will be used by the parser.
 * @author knokko
 *
 */
interface SourceFileReader3 {
	
	/**
	 * Determines the next source element that appears in the source code. It will process the result that
	 * was returned by the second source file reader and search for operators in the words. Notice that this
	 * reader might return more than 1 result for a single result of the second source file reader. This
	 * method will return null if the end of the source file has been reached.
	 * @return The next source element that appears in the source code, or null if the end of the source file
	 * has been reached.
	 * @throws IOException If the second source file reader throws an IOException
	 * @throws ParsingException If the source code is incorrect and thus can't be parsed
	 */
	SourceElement next() throws IOException, ParsingException;
}