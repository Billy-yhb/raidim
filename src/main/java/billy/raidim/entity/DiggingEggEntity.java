package billy.raidim.entity;

import billy.raidim.item.DiggingEggItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DiggingEggEntity extends ThrownItemEntity {

	public DiggingEggEntity(EntityType<? extends DiggingEggEntity> entityType_1, World world_1) {
		super(entityType_1, world_1);
	}

	public DiggingEggEntity(EntityType<? extends DiggingEggEntity> entityType_1, LivingEntity livingEntity_1,
			World world_1) {
		super(entityType_1, livingEntity_1, world_1);
	}

	public DiggingEggEntity(EntityType<? extends DiggingEggEntity> entityType_1, double double_1, double double_2,
			double double_3, World world_1) {
		super(entityType_1, double_1, double_2, double_3, world_1);
	}
	@Override
	protected Item getDefaultItem() {
		return DiggingEggItem.INSTANCE;
	}
	@Override
	protected void onCollision(HitResult var1) {
		if(world.isClient) {
			return;
		}
		if(var1.getType()!=HitResult.Type.ENTITY||
				(var1.getType()==HitResult.Type.ENTITY&&
				!((EntityHitResult)var1).getEntity().equals(getOwner()))) {
			remove();
			BlockPos pos=new BlockPos(var1.getPos());
			for(int i=-1;i<=1;i++) {
				for(int j=-1;j<=1;j++) {
					for(int k=-1;k<=1;k++) {
						tryDest(pos.add(i, j, k));
					}
				}
			}
			tryDest(pos.down(2));
			tryDest(pos.up(2));
			tryDest(pos.north(2));
			tryDest(pos.south(2));
			tryDest(pos.east(2));
			tryDest(pos.west(2));
		}
	}
	protected void tryDest(BlockPos pos) {
		if(canbreak(pos))
			world.breakBlock(pos, true);
	}

	private boolean canbreak(BlockPos pos) {
		BlockState state=world.getBlockState(pos);
		Block block=state.getBlock();
		boolean flag=true;
		if(getOwner() instanceof PlayerEntity) {
			flag=((PlayerEntity)getOwner()).abilities.allowModifyWorld;
		}else {
			flag=world.getGameRules().getBoolean("mobGriefing");
		}
		return flag &&
				block!=Blocks.BEDROCK && 
				block!=Blocks.COMMAND_BLOCK &&
				block!=Blocks.CHAIN_COMMAND_BLOCK &&
				block!=Blocks.REPEATING_COMMAND_BLOCK &&
				block!=Blocks.END_PORTAL_FRAME &&
				block!=Blocks.END_PORTAL &&
				block!=Blocks.END_GATEWAY &&
				block!=Blocks.NETHER_PORTAL;
	}
	public ItemStack getStack() {
		return new ItemStack(DiggingEggItem.INSTANCE);
	}
}
