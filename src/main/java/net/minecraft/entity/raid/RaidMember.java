package net.minecraft.entity.raid;

import java.util.Arrays;
import java.util.HashMap;

import billy.raidim.RaidImMod;
import billy.raidim.reflect.EnumHelper;
import billy.raidim.reflect.FieldHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.Raid.Member;

public class RaidMember {
	public static final EnumHelper<Member> helper=new EnumHelper<>(
			Member.class,new FieldHelper<>(Member.class,"field_16632",null));
	public static final FieldHelper<?> type_fld=
			new FieldHelper<>(Member.class,"field_16629","type");
	public static final FieldHelper<?> count_fld=
			new FieldHelper<>(Member.class,"field_16628","countInWave");
	public static final FieldHelper<Member[]> values_fld=
			new FieldHelper<>(Member.class,"field_16636","VALUES");
	@SuppressWarnings("rawtypes")
	public static void addRaidMember(String name,EntityType type,int...waves) {
		HashMap<FieldHelper,Object> attrmap=new HashMap<>();
		attrmap.put(type_fld,type);
		attrmap.put(count_fld, waves);
		Member m=helper.add(name,attrmap);
		Member[] mem=values_fld.get(null);
		RaidImMod.logger.info(m);
		RaidImMod.logger.info(count_fld.get(m));
		for(int i:(int[])count_fld.get(m)) {
			RaidImMod.logger.info(i);
		}
		RaidImMod.logger.info(type_fld.get(m));
		for(Member mn:mem) {
			RaidImMod.logger.info("      {}",mn);
		}
		Member[] n=Arrays.copyOf(mem, mem.length+1);
		n[mem.length]=m;
		values_fld.set(null, n);
		RaidImMod.logger.info("after");
		for(Member mn:values_fld.get(null)) {
			RaidImMod.logger.info("      {}",mn);
		}
	}
}
