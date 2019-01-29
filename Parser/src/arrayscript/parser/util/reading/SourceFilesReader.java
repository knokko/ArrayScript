package arrayscript.parser.util.reading;

import java.io.IOException;

/**
 * Instances of SourceFilesReader will be used to read all source files of a project. They have a public
 * method called next() that returns the next string separated by a whitespace character.
 * @author knokko
 *
 */
public interface SourceFilesReader {
	
	/**
	 * Reads the next String that is separated by whitespace characters, line endings or file begins/ends.
	 * If the end of the current line is reached, it will go on with the next line. If the end of the
	 * current source file is reached, it will continue at the next source file. Individual source files
	 * can be read in arbitrary order, but the content of the source files must be read line by line and
	 * from start to end. If there is no more source code to read, this method will return null.
	 * @return The next String in the source code or null if all source files have been read
	 */
	String next() throws IOException;
}