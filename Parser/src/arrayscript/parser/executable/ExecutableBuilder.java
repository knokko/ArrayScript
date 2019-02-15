package arrayscript.parser.executable;

import java.util.List;

import arrayscript.lang.element.Namespace;
import arrayscript.lang.executable.Executable;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.source.SourceElement;

public class ExecutableBuilder {
	
	private final List<SourceElement> unparsedExecutable;
	private Executable executable;
	
	public ExecutableBuilder(List<SourceElement> unparsed) {
		unparsedExecutable = unparsed;
	}
	
	public boolean isConfirmed() {
		return executable != null;
	}
	
	public void parse(AppBuilder application, Namespace namespace) {
		// TODO parse everything correctly
	}
	
	public Executable getExecutable() {
		if (executable == null) {
			throw new IllegalStateException("Executable is not yet parsed");
		}
		
		return executable;
	}
}