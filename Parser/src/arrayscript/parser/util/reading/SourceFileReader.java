package arrayscript.parser.util.reading;

import java.io.IOException;

/**
 * Instances of SourceFileReader read a single source file 'word by world' where words are any strings that
 * are separated by whitespace characters and/or line terminators.
 * @author knokko
 *
 */
public interface SourceFileReader {
	
	/**
	 * Reads the next String that is separated by whitespace characters or line endings.
	 * If the end of the current line is reached, it will go on with the next line. If the end of the
	 * current source file is reached, it will return null.
	 * @return The next String in this source file or null if the end of the file has been reached
	 */
	String next() throws IOException;
}