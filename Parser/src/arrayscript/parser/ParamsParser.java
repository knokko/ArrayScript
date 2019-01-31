package arrayscript.parser;

import arrayscript.parser.builder.param.ParamBuilder;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.util.ParsingException;

public class ParamsParser {
	
	/**
	 * Attempts to parse the given parameters. It must start with a ( and end with a ). It only determines
	 * the type name and parameter name of every parameter, it doesn't try to find the actual type.
	 * @param parameters The parameters as read from the source code
	 * @return An instance of ParamsBuilder that represents the given parameters
	 * @throws ParsingException If the given parameters do not have the right syntax
	 */
	public static ParamsBuilder parse(String parameters) throws ParsingException {
		
		// Simple checks
		if (!parameters.startsWith("(")) {
			throw new ParsingException("The parameters should start with (");
		}
		if (!parameters.endsWith(")")) {
			throw new ParsingException("The parameters should end with )");
		}
		
		String[] splitted = parameters.split(",");
		
		// every parameter is separated by a comma and there was no comma apparently, so there are no params
		if (splitted.length == 1) {
			return new ParamsBuilder();
		}
		
		// remove opening bracket and ending bracket
		splitted[0] = splitted[0].substring(1);
		splitted[splitted.length - 1] = splitted[splitted.length - 1].substring(0, splitted[splitted.length - 1].length() - 1);
		
		ParamBuilder[] params = new ParamBuilder[splitted.length];
		
		for (int index = 0; index < params.length; index++) {
			String current = splitted[index].trim();
			int endTypeIndex = current.indexOf(' ');
			int startNameIndex = current.lastIndexOf(' ' + 1);
			if (endTypeIndex == -1 || startNameIndex == -1) {
				throw new ParsingException("Illegal parameters: " + parameters);
			}
			params[index] = new ParamBuilder(current.substring(0, endTypeIndex), current.substring(startNameIndex));
		}
		
		return new ParamsBuilder(params);
	}
}