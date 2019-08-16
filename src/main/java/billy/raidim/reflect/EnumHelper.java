package billy.raidim.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Map;

import sun.misc.Unsafe;

@SuppressWarnings({"rawtypes","unchecked"})
public class EnumHelper<T extends Enum>{
	public final Class cls;
	public static final FieldHelper<Class> clazz_fld=
			new FieldHelper(Constructor.class,"clazz",null);
	public static final FieldHelper<Unsafe> theunsafe_fld=
			new FieldHelper(Unsafe.class,"theUnsafe",null);
	public static final Unsafe unsafe=theunsafe_fld.get(null);
	public static final FieldHelper<Integer> ordinal_fld=
			new FieldHelper(Enum.class,"ordinal",null);
	public static final FieldHelper<String> name_fld=
			new FieldHelper(Enum.class,"name",null);
	public final FieldHelper<T[]> values_fld; 
	public EnumHelper(Class<T> cls) {
		this.cls=cls;
		this.values_fld=new FieldHelper(cls,"$VALUES","ENUM$VALUES");
	}
	public EnumHelper(Class<T> cls,FieldHelper<T[]> values_fld) {
		this.cls=cls;
		this.values_fld=values_fld;
	}
	public T add(String name,Map<FieldHelper,Object> attr) {
		try {
			T instance=(T) unsafe.allocateInstance(cls);
			for(Map.Entry<FieldHelper, Object> e:attr.entrySet()) {
				e.getKey().set(instance, e.getValue());
			}
			T[] val=values_fld.get(null);
			T[] newval=(T[]) Array.newInstance(cls, val.length+1);
			ordinal_fld.set(instance, val.length);
			name_fld.set(instance, name);
			for(int i=0;i<val.length;i++) {
				newval[i]=val[i];
			}
			newval[val.length]=instance;
			values_fld.set(null, newval);
			return instance;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}
}
