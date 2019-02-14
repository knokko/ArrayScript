package arrayscript.parser.source.reading;

import java.io.IOException;

import arrayscript.parser.source.SourceElement;
import arrayscript.parser.util.ParsingException;

class SimpleSourceFileReader implements SourceFileReader {
	
	private final SourceFileReader3 third;
	
	SimpleSourceFileReader(SourceFileReader3 third){
		this.third = third;
	}

	@Override
	public SourceElement next() throws IOException, ParsingException {
		return third.next();
	}
}