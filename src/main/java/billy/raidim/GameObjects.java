package billy.raidim;

import java.util.TreeMap;

import billy.raidim.entity.RaidMember;
import billy.raidim.reflect.FieldHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;

public class GameObjects {
	public static final TreeMap<String, GameRules.Key> gamerules_map
		=new FieldHelper<TreeMap<String, GameRules.Key>>(
				GameRules.class,"field_9197","KEYS").get(null);
	public static void addGameRule(String name,
			String defaultval,GameRules.Type type) {
		gamerules_map.put(name, new GameRules.Key(defaultval, type));
	}
	public static void addRaidMember(String name,EntityType<?> type,
			int c1,int c2,int c3,int c4,int c5,int c6,int c7) {
		RaidMember.addRaidMember(name, type, new int[] {c1,c2,c3,c4,c5,c6,c7});
	}
	public static SpawnEggItem addSpawnEgg(EntityType<?> type,int color1,int color2) {
		SpawnEggItem item=new SpawnEggItem(type,color1,color2,
				new Item.Settings().group(ItemGroup.MISC));
		Registry.register(Registry.ITEM, new Identifier(
				EntityType.getId(type)+"_spawn_egg"),
				item);
		return item;
	}
}
