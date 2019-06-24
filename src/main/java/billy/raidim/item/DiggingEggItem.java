package billy.raidim.item;

import billy.raidim.RaidImMod;
import billy.raidim.entity.DiggingEggEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DiggingEggItem extends Item {
	public static Item INSTANCE=new DiggingEggItem();
	public DiggingEggItem() {
		super(new Settings().group(ItemGroup.COMBAT).maxCount(64));
	}
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack item=player.getStackInHand(hand);
		if(!player.abilities.creativeMode)
			item.decrement(1);
		if(!world.isClient) {
			DiggingEggEntity egg=new DiggingEggEntity(RaidImMod.DIGGINGEGG,
					player,player.world);
			egg.method_19207(player, player.pitch, player.yaw, 0.0F, 2.0F, 1.0F);
			world.spawnEntity(egg);
		}
		return new TypedActionResult<>(ActionResult.SUCCESS,player.getStackInHand(hand));
	}
	protected float getGravity() {
		return 0.002F;
	}
}
