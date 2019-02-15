package arrayscript.parser.source.reading;

import java.io.IOException;

import arrayscript.parser.source.SourceElement;
import arrayscript.parser.util.ParsingException;

class DefaultSourceFileReader implements SourceFileReader {
	
	private final SourceFileReader4 fourth;
	
	DefaultSourceFileReader(SourceFileReader1 first){
		fourth = new SourceFileReader4(new SourceFileReader3(new SourceFileReader2(first)));
	}

	@Override
	public SourceElement next() throws IOException, ParsingException {
		return fourth.next();
	}
}