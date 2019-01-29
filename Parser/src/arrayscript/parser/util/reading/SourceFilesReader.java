package arrayscript.parser.util.reading;

import java.io.IOException;

/**
 * Instances of SourceFilesReader will be used to read all source files of a project. They have a public
 * method called next() that returns a SourceFileReader for the next source file.
 * @author knokko
 *
 */
public interface SourceFilesReader {
	
	/**
	 * Finds the next source file and returns a SourceFileReader to read from that file. If all source files
	 * in the source folders have been read, this method returns null
	 * @return A SourceFileReader for the next source file, or null if all source files have been read
	 */
	SourceFileReader next() throws IOException;
}