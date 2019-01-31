package arrayscript.lang.var.type;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PrimitiveTypes {
	
	public static final TypeAny ANY = new TypeAny();
	public static final TypeBoolean BOOLEAN = new TypeBoolean();
	
	private static final Map<String,Type> NAME_MAP;
	
	static {
		Field[] fields = PrimitiveTypes.class.getDeclaredFields();
		NAME_MAP = new HashMap<String,Type>(fields.length - 1);
		for (Field field : fields) {
			if (field.getType() != HashMap.class) {
				try {
					Type type = (Type) field.get(null);
					NAME_MAP.put(type.getName(), type);
				} catch (Exception e) {
					throw new Error(e);
				}
			}
		}
	}
	
	public static Type getByName(String name) {
		return NAME_MAP.get(name);
	}
}