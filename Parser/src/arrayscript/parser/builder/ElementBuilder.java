package arrayscript.parser.builder;

import arrayscript.lang.element.Element;

public interface ElementBuilder {
	
	String getName();
	
	Element build();
}