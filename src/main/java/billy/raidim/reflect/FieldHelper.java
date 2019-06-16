package billy.raidim.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import billy.raidim.RaidImMod;

@SuppressWarnings("rawtypes")
public class FieldHelper<T>{
	public final String srg;
	public final String mcp;
	public final Class cls;
	public final static Field mod;
	public final static Field override;
	private Field fld_cache;
	public FieldHelper(Class cls,String srg,String mcp) {
		this.srg=srg;
		this.mcp=mcp;
		this.cls=cls;
		try {
			this.fld_cache=cls.getDeclaredField(srg);
		} catch (NoSuchFieldException | SecurityException e) {
			try {
				this.fld_cache=cls.getDeclaredField(mcp);
			} catch (NoSuchFieldException | SecurityException e1) {
				RaidImMod.logger.fatal("Error:Cannot find field.List all fields.");
				for(Field f:cls.getDeclaredFields()) {
					RaidImMod.logger.fatal("\t {}",f);
				}
				throw new RuntimeException(e1);
			}
		}
		try {
			mod.setInt(fld_cache, fld_cache.getModifiers()& 0xFFFFFFEF);
			this.fld_cache.setAccessible(true);
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
	@SuppressWarnings("unchecked")
	public T get(Object obj) {
		try {
			return (T) fld_cache.get(obj);
		} catch (IllegalArgumentException e) {
			try {
				throw new ClassCastException(
						String.format("Cannot cast %s to %s.",
								obj.getClass().getName(),cls.getName())).initCause(e);
			} catch (Throwable e1) {
				throw new RuntimeException(e);
			}
		} catch (IllegalAccessException e) {
			try {
				System.err.println(override.get(fld_cache));
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException(e);
		}
	}
	public void set(Object obj,T val) {
		try {
			fld_cache.set(obj, val);
		} catch (IllegalArgumentException e) {
			try {
				throw new ClassCastException(
						String.format("Cannot cast %s to %s.",
								obj.getClass().getName(),cls.getName())).initCause(e);
			} catch (Throwable e1) {
				throw new RuntimeException(e1);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}