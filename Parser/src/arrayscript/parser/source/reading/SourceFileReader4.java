package arrayscript.parser.source.reading;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import arrayscript.lang.Keyword;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.SourceKeyword;
import arrayscript.parser.util.ParsingException;

/**
 * The fourth and last source file reader in the chain of source file readers. This reader takes the result
 * of the third source reader as input and distinguishes the keywords from the other words. Currently, the
 * results of these source file readers are passed directly to the source file reader that will be used by
 * the actual parser.
 * @author knokko
 *
 */
class SourceFileReader4 {
	
	private static final Map<String,Keyword> KEYWORDS;
	
	static {
		
		// Add all keywords to the map
		Keyword[] all = Keyword.values();
		
		// We already know what the size will be, so use that
		KEYWORDS = new HashMap<String,Keyword>(all.length);
		
		// Now put them all in the map
		for (Keyword keyword : all) {
			KEYWORDS.put(keyword.name().toLowerCase(), keyword);
		}
	}
	
	private final SourceFileReader3 third;
	
	public SourceFileReader4(SourceFileReader3 third) {
		this.third = third;
	}
	
	/**
	 * Processes the result from the third source file reader and checks if that result is an instance of
	 * SourceWord, but should be a keyword. If that is the case, this method will return the instance of
	 * SourceKeyword that represents the keyword. Otherwise, it will simply return the result of the third
	 * source file reader.
	 * @return The next source element in the source code
	 * @throws IOException If an IO error occurred while reading the source file
	 * @throws ParsingException If the source code is incorrect and thus can't be parsed
	 */
	public SourceElement next() throws IOException, ParsingException {
		
		// The task of this reader is fairly simple, just check if it is a keyword
		SourceElement from3 = third.next();
		
		// End of source file has been reached
		if (from3 == null) {
			return null;
		}
		
		// Words could be keywords
		if (from3.isWord()) {
			Keyword asKeyword = KEYWORDS.get(from3.getWord());
			
			// The word was actually a keyword, so return it as keyword
			if (asKeyword != null) {
				return SourceKeyword.getInstance(asKeyword);
			}
		}
		
		// The result from the third reader was not a keyword, so just pass it along
		return from3;
	}
}