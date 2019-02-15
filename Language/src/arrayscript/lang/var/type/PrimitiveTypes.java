package arrayscript.lang.var.type;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PrimitiveTypes {
	
	public static final TypeAny ANY = new TypeAny();
	public static final TypeBoolean BOOLEAN = new TypeBoolean();
	public static final TypeNumber NUMBER = new TypeNumber();
	public static final TypeString STRING = new TypeString();
	public static final TypeFunction FUNCTION = new TypeFunction();
	public static final TypeInt8 INT8 = new TypeInt8();
	public static final TypeInt16 INT16 = new TypeInt16();
	public static final TypeInt32 INT32 = new TypeInt32();
	public static final TypeUInt8 UINT8 = new TypeUInt8();
	public static final TypeUInt16 UINT16 = new TypeUInt16();
	public static final TypeUInt32 UINT32 = new TypeUInt32();
	
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