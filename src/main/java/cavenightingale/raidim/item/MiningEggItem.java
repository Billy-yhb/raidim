package cavenightingale.raidim.item;

import cavenightingale.raidim.RaidImMod;
import cavenightingale.raidim.entity.MiningEggEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MiningEggItem extends Item {
	public static final MiningEggItem INSTANCE = new MiningEggItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(64));

	public MiningEggItem(Settings settings) {
		super(settings);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack item = player.getStackInHand(hand);
		if(!player.abilities.creativeMode)
			item.decrement(1);
		if(!world.isClient) {
			MiningEggEntity egg = new MiningEggEntity(RaidImMod.MINING_EGG_ENTITY_TYPE, player, player.world);
			egg.setProperties(player, player.pitch, player.yaw, 0.0F, 2.0F, 0.8F);
			world.spawnEntity(egg);
		}
		return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
	}
}
