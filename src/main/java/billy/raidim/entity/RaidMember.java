package billy.raidim.entity;

import java.util.Arrays;
import java.util.HashMap;

import billy.raidim.reflect.ClassHelper;
import billy.raidim.reflect.EnumHelper;
import billy.raidim.reflect.FieldHelper;
import net.minecraft.entity.EntityType;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RaidMember {
	public static final Class mem_cls=new ClassHelper(
			"net.minecraft.class_3765$class_3766",
			"net.minecraft.entity.raid.Raid$Member").get();
	public static final EnumHelper helper=new EnumHelper(
			mem_cls,new FieldHelper<>(mem_cls,"field_16632",null));
	public static final FieldHelper<?> type_fld=
			new FieldHelper<>(mem_cls,"field_16629","type");
	public static final FieldHelper<?> count_fld=
			new FieldHelper<>(mem_cls,"field_16628","countInWave");
	public static final FieldHelper<Object[]> values_fld=
			new FieldHelper<>(mem_cls,"field_16636","VALUES");
	public static void addRaidMember(String name,EntityType type,int...waves) {
		HashMap<FieldHelper,Object> attrmap=new HashMap<>();
		attrmap.put(type_fld,type);
		attrmap.put(count_fld, waves);
		Object m=helper.add(name,attrmap);
		Object[] mem=values_fld.get(null);
		Object[] n=Arrays.copyOf(mem, mem.length+1);
		n[mem.length]=m;
		values_fld.set(null, n);
	}
}
