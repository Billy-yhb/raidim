package billy.raidim.mixin;

import java.util.ArrayList;
import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.registry.Registry;

@Mixin(ItemGroup.class)
public abstract class ItemGroupMixin {
	@Environment(EnvType.CLIENT)
	@Overwrite
	public void appendStacks(DefaultedList<ItemStack> defaultedList_1) {
		Iterator<Item> var2 = Registry.ITEM.iterator();
		var2=sort(var2);
		while(var2.hasNext()) {
			Item item_1 = (Item)var2.next();
			item_1.appendStacks((ItemGroup)((Object)this), defaultedList_1);
		}
	}

	private Iterator<Item> sort(Iterator<Item> i) {
		ArrayList<Item> before=new ArrayList<Item>(),
				egg=new ArrayList<Item>(),
				after=new ArrayList<Item>();
		boolean find_villager_spawn_egg=false;
		while(i.hasNext()) {
			Item item=i.next();
			if(item instanceof SpawnEggItem) {
				egg.add(item);
				if(item==Items.VILLAGER_SPAWN_EGG) {
					find_villager_spawn_egg=true;
				}
			}else {
				if(find_villager_spawn_egg) {
					after.add(item);
				}else {
					before.add(item);
				}
			}
		}
		before.addAll(egg);
		before.addAll(after);
		return before.iterator();
	}
}
