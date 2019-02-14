package arrayscript.parser.source.reading;

import java.io.IOException;

import arrayscript.parser.source.SourceElement;
import arrayscript.parser.util.ParsingException;

/**
 * Instances of SourceFileReader are used by the parser to read source files nicely. Source file readers take
 * care of the process that distinguishes strings and operators from all other 'things'/'words' in the source
 * code that is about to be parsed. They are quite convenient for the parser because especially strings can
 * be annoying to handle for the parser.
 * @author knokko
 *
 */
public interface SourceFileReader {
	
	/**
	 * Reads the next source element from its source file. If the end of its source file has been reached,
	 * this method will return null.
	 * @return The next source element or null if the end of the source file has been reached
	 * @throws IOException if an IOException occurs while reading the source file
	 * @throws ParsingException if the read source code is incorrect and thus can't be parsed
	 */
	SourceElement next() throws IOException, ParsingException;
}