package arrayscript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import arrayscript.lang.Operator;
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
		
		// Let's take a rather big initial capacity because we are expecting a lot
		List<SourceElement> content = new ArrayList<SourceElement>(100);
		
		// Keep track of the depth so that we know when we are exiting the code block
		int depth = 0;
		
		// I find breaking once the final curly bracket is read more convenient
		// than putting it in the loop condition here
		while (true) {
			SourceElement next = reader.next();
			
			if (next == null) {
				throw new ParsingException("Unfinished code block");
			}
			
			// Check if a block is being closed
			if (next.isOperator() && next.getOperator() == Operator.CLOSE_BLOCK) {
				
				// If depth is larger than 0, we just close a sub-block we opened in the code block
				if (depth > 0) {
					depth--;
				} else {
					
					// This is the end of the code block
					// Intentionally don't add this to the content list
					break;
				}
			}
			
			// Check if a sub-block is being opened
			if (next.isOperator() && next.getOperator() == Operator.OPEN_BLOCK) {
				depth++;
			}
			
			// If we reach this point, the current source element was not the code block closer
			content.add(next);
		}
		
		return content;
	}
}