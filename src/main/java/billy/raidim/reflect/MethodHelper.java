package billy.raidim.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import billy.raidim.RaidImMod;

@SuppressWarnings({"rawtypes","unchecked"})
public class MethodHelper {
	public final String srg;
	public final String mcp;
	public final Class cls;
	public final static Field mod;
	public final static Field override;
	private Method fnc_cache;
	public MethodHelper(Class cls,String srg,String mcp) {
		this.srg=srg;
		this.mcp=mcp;
		this.cls=cls;
		try {
			this.fnc_cache=cls.getDeclaredMethod(srg);
		} catch (NoSuchMethodException | SecurityException e) {
			try {
				this.fnc_cache=cls.getDeclaredMethod(mcp);
			} catch (NoSuchMethodException | SecurityException e1) {
				RaidImMod.logger.fatal("Error:Cannot find method.List all methods.");
				for(Method f:cls.getDeclaredMethods()) {
					RaidImMod.logger.fatal("\t {}",f);
				}
				throw new RuntimeException(e1);
			}
		}
		try {
			mod.setInt(fnc_cache, fnc_cache.getModifiers()& 0xFFFFFFEF);
			this.fnc_cache.setAccessible(true);
		} catch (IllegalArgumentException | IllegalAccessException e2) {
			throw new RuntimeException(e2);
		}
	}
	static {
		try {
			mod = Field.class.getDeclaredField("modifiers");
			mod.setAccessible(true);
			override= AccessibleObject.class.getDeclaredField("override");
			override.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
