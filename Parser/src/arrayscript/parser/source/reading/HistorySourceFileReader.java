package arrayscript.parser.source.reading;

import java.io.IOException;

import arrayscript.parser.source.SourceElement;
import arrayscript.parser.util.ParsingException;

/**
 * The history source file reader is a special implementation of source file reader that allows users to
 * return an array of specified source elements before returning the results of the backing source file
 * reader. This can be useful if you want to avoid dirty constructions when you have read too many elements
 * from the backing reader and would like to re-read those elements.
 * @author knokko
 *
 */
public class HistorySourceFileReader implements SourceFileReader {
	
	private final SourceFileReader realReader;
	private final SourceElement[] history;
	
	private int historyIndex;
	
	/**
	 * Constructs a HistorySourceFileReader with the given history and backing reader. The first calls to the
	 * next() method of this HistorySourceFileReader will return the elements in the 'fakeHistory'. Once all
	 * elements in the fake history have been read once, its next() method will simply return the result of
	 * the next() method of the provided 'realReader'.
	 * @param realReader The backing reader that will be used after the fake history has been used
	 * @param fakeHistory The values to return before starting to read the real reader
	 */
	public HistorySourceFileReader(SourceFileReader realReader, SourceElement... fakeHistory) {
		this.realReader = realReader;
		this.history = fakeHistory;
	}

	@Override
	public SourceElement next() throws IOException, ParsingException {
		if (historyIndex < history.length) {
			return history[historyIndex++];
		} else {
			return realReader.next();
		}
	}
}