package arrayscript.parser.builder.param;

public class ParamsBuilder {
	
	private final ParamBuilder[] params;
	
	public ParamsBuilder(ParamBuilder... params) {
		this.params = params;
	}
	
	@Override
	public String toString() {
		String[] strings = new String[params.length];
		for (int index = 0; index < params.length; index++) {
			strings[index] = params[index].toString();
		}
		
		int length = 0;
		for (String string : strings) {
			length += string.length() + 2;
		}
		if (strings.length != 0) {
			length -= 2;
		}
		
		StringBuilder builder = new StringBuilder(length);
		for (int index = 0; index < strings.length - 1; index++) {
			builder.append(strings[index]);
			builder.append(',');
			builder.append(' ');
		}
		if (strings.length != 0) {
			builder.append(strings[strings.length - 1]);
		}
		
		return builder.toString();
	}
	
	public ParamBuilder[] getParams() {
		return params;
	}
	
	public boolean isConfirmed() {
		
		// The parameters are not confirmed if there exists a parameter that is not confirmed
		for (ParamBuilder param : params) {
			if (!param.getType().isTypeConfirmed()) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean conflicts(ParamsBuilder other) {
		ParamBuilder[] o = other.params;
		if (params.length == o.length) {
			
			// If at least 1 of the 
			for (int index = 0; index < o.length; index++) {
				if (!o[index].getType().equals(params[index].getType())) {
					return false;
				}
			}
			
			return true;
		} else {
			
			// No conflict if amount of parameters is different
			return false;
		}
	}
}