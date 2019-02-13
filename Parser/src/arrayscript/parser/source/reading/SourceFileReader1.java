package arrayscript.parser.source.reading;

import java.io.IOException;

/**
 * The first source file reader in the chain of source readers. The task of this reader is to find all 'words'
 * that are separated by whitespace characters, except for those whitespace characters that are in a string.
 * (So this reader will not break strings.)
 * @author knokko
 *
 */
public interface SourceFileReader1 {
	
	/**
	 * Reads the next String that is separated by whitespace characters or line endings.
	 * If the end of the current line is reached, it will go on with the next line. As an exception, this
	 * method will not interrupt strings. So if there is a whitespace character within a string, that
	 * whitespace character will be included in the result and the reading will continue. If the end of the
	 * current source file is reached, it will return null.
	 * @return The next String in this source file or null if the end of the file has been reached
	 */
	String next() throws IOException;
}