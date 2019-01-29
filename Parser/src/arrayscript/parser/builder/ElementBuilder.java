package arrayscript.parser.builder;

import arrayscript.lang.ASElement;

public interface ElementBuilder {
	
	String getName();
	
	ASElement build();
}