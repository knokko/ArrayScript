package arrayscript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import arrayscript.lang.Operator;
import arrayscript.parser.builder.param.ParamBuilder;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.reading.SourceFileReader;
import arrayscript.parser.util.ParsingException;

public class ParamsParser {
	
	/**
	 * Attempts to parse the given parameters. The initial '(' (that starts the function declaration) should
	 * already be read. This method only determines the type name and parameter name of every parameter, it 
	 * doesn't try to find the actual type. (That must be done in a later parsing stage.)
	 * @param reader The source file reader to read the
	 * @return An instance of ParamsBuilder that represents the given parameters
	 * @throws ParsingException If the given parameters do not have the right syntax
	 * @throws IOException If the reader throws an IO exception
	 */
	public static ParamsBuilder parse(SourceFileReader reader) throws ParsingException, IOException {
		
		// We can't just create an array because we won't know the size until the end
		List<ParamBuilder> paramList = new ArrayList<ParamBuilder>();
		
		// Looping until the break is a convenient way to do things
		while (true) {
			
			SourceElement first = reader.next();
			if (first == null) {
				throw new ParsingException("Unfinished parameters");
			}
			
			if (first.isOperator()) {
				
				// No commas are allowed at this point, only closing brackets
				// Note that this should only occur if there are no parameters
				if (first.getOperator() == Operator.CLOSE_BRACKET) {
					break;
				} else {
					throw new ParsingException("Unexpected " + first);
				}
			} else if (first.isWord()){
				
				// The first must be the type name
				SourceElement second = reader.next();
				
				if (second == null) {
					throw new ParsingException("Unfinished parameters");
				}
				
				// The second must be the parameter name
				if (second.isWord()) {
					
					SourceElement third = reader.next();
					
					if (third == null) {
						throw new ParsingException("Unfinished parameters");
					}
					
					// The next should be either a ',' to indicate another parameter or a ')' to indicate
					// the end of the parameters.
					if (third.isOperator()) {
						
						// Add the parameter to the list and either continue or terminate the loop
						paramList.add(new ParamBuilder(first.getWord(), second.getWord()));
						
						if (third.getOperator() == Operator.NEXT) {
							continue;
						} else if (third.getOperator() == Operator.CLOSE_BRACKET) {
							break;
						} else {
							throw new ParsingException("Expected ',' or ')', but found " + third);
						}
					} else {
						throw new ParsingException("Expected ',' or ')', but found " + third);
					}
				} else {
					throw new ParsingException("Expected parameter name, but got " + second);
				}
			} else {
				throw new ParsingException("Unexpected " + first);
			}
		}
		
		// Now that we do know the size, create an array, fill it and return it
		ParamBuilder[] params = new ParamBuilder[paramList.size()];
		paramList.toArray(params);
		
		return new ParamsBuilder(params);
	}
}