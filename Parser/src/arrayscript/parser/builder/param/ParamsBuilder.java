package arrayscript.parser.builder.param;

public class ParamsBuilder {
	
	private final ParamBuilder[] params;
	
	public ParamsBuilder(ParamBuilder... params) {
		this.params = params;
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