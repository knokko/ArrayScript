package arrayscript.parser;

import java.io.IOException;
import java.util.List;

import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.reading.SourceFileReader;
import arrayscript.parser.util.ParsingException;

public class ExecutableParser {
	
	/**
	 * This method is used for the first part of parsing executable code blocks. It doesn't find any
	 * application logic, but just collects all source elements until the '}' that will close the code block.
	 * This is useful because the parser can simply use this method to store the unparsed code block and
	 * continue at the end of the code block so that it can parse the actual meaning of the code block later.
	 * This method assumes that the opening '{' is already read and it will consume the closing '}' without
	 * adding it to the list of source elements. The source elements that are read first will be put first in
	 * the list.
	 * @param reader The source file reader that is reading the current source file
	 * @return a list containing all source elements within the code block
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException If the code block is incorrect and thus can't be parsed
	 */
	public static List<SourceElement> parseInitial(SourceFileReader reader) throws IOException, ParsingException {
		
		// Currently, this does exactly the same
		return SmallParser.readBlock(reader);
	}
}