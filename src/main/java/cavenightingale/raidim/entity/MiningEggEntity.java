package cavenightingale.raidim.entity;

import cavenightingale.raidim.RaidImMod;
import cavenightingale.raidim.item.MiningEggItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.List;

public class MiningEggEntity extends ThrownItemEntity {
	public MiningEggEntity(EntityType<? extends MiningEggEntity> entityType_1, World world_1) {
		super(entityType_1, world_1);
	}

	public MiningEggEntity(EntityType<? extends MiningEggEntity> entityType_1, LivingEntity livingEntity_1,
							World world_1) {
		super(entityType_1, livingEntity_1, world_1);
	}

	public MiningEggEntity(EntityType<? extends MiningEggEntity> entityType_1, double double_1, double double_2,
							double double_3, World world_1) {
		super(entityType_1, double_1, double_2, double_3, world_1);
	}

	public MiningEggEntity(World world_1) {
		this(RaidImMod.MINING_EGG_ENTITY_TYPE, world_1);
	}

	@Override
	protected Item getDefaultItem() {
		return MiningEggItem.INSTANCE;
	}

	@Override
	protected void onCollision(HitResult var1) {
		if(world.isClient) {
			return;
		}
		if(var1.getType()!=HitResult.Type.ENTITY || (var1.getType() == HitResult.Type.ENTITY &&
				!((EntityHitResult)var1).getEntity().equals(getOwner()))) {
			remove();
			float amount = world.getDifficulty() == Difficulty.HARD ? 90 : 60;
			for(Entity victim : getNearByEntities(var1.getPos()))
				if(victim instanceof IronGolemEntity ||victim instanceof WitherEntity || victim instanceof SnowGolemEntity)
					victim.damage(DamageSource.thrownProjectile(this, getOwner()), amount);
			BlockPos pos = new BlockPos(var1.getPos());
			for(int i = -1; i <= 1; i++)
				for(int j = -1; j <= 1; j++)
					for(int k = -1; k <= 1;k++)
						tryDest(pos.add(i, j, k));
			tryDest(pos.down(2));
			tryDest(pos.up(2));
			tryDest(pos.north(2));
			tryDest(pos.south(2));
			tryDest(pos.east(2));
			tryDest(pos.west(2));
		}
	}

	private List<Entity> getNearByEntities(Vec3d pos) {
		return world.getEntitiesByClass(Entity.class, new Box(
				pos.x - 3.5, pos.y - 3.5, pos.z - 3.5, pos.x + 3.5, pos.y + 3.5, pos.z + 3.5), x -> true);
	}

	private void tryDest(BlockPos pos) {
		if(canbreak(pos))
			world.breakBlock(pos, true);
	}

	private boolean canbreak(BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		boolean flag = true;
		if(getOwner() instanceof PlayerEntity)
			flag = ((PlayerEntity)getOwner()).canModifyBlocks();
		else
			flag = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
		return flag && state.getHardness(world, pos) >= 0;
	}

	@Override
	public ItemStack getStack() {
		return new ItemStack(MiningEggItem.INSTANCE);
	}

	@Override
	protected float getGravity() {
		return 0.002F;
	}
}
