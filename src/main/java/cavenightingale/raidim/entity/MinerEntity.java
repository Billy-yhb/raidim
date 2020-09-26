package cavenightingale.raidim.entity;

import cavenightingale.raidim.RaidImMod;
import cavenightingale.raidim.entity.goal.ThrowMiningEggGoal;
import cavenightingale.raidim.item.MiningEggItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MinerEntity extends IllagerEntity {
	public static final TrackedData<Boolean> PICKAXE = DataTracker.registerData(MinerEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);
	private boolean pickaxe = false;
	private int miningEggCD = -1;
	private final EntityAttributeModifier SLOW_DOWN_MODIFIER =
			new EntityAttributeModifier("Slow down", -0.15, EntityAttributeModifier.Operation.ADDITION);

	public static DefaultAttributeContainer.Builder createMinerAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 56.0)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 36.0d)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0d)
				.add(EntityAttributes.GENERIC_ARMOR, 6.0);
	}

	public MinerEntity(EntityType<? extends IllagerEntity> entityType_1, World world_1) {
		super(entityType_1, world_1);
		setStackInHand(Hand.MAIN_HAND, new ItemStack(MiningEggItem.INSTANCE));
	}

	public MinerEntity(World world) {
		this(RaidImMod.MINER_ENTITY_TYPE, world);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(PICKAXE, false);
	}

	@Override
	public void addBonusForWave(int wave, boolean unused) {
	}

	@Override
	public SoundEvent getCelebratingSound() {
		return SoundEvents.ENTITY_VINDICATOR_CELEBRATE;
	}

	@Override
	public void readCustomDataFromTag(CompoundTag cpd) {
		this.miningEggCD = cpd.getInt("miningEggCD");
	}

	@Override
	public void writeCustomDataToTag(CompoundTag cpd) {
		cpd.putInt("miningEggCD", miningEggCD);
	}

	@Override
	public void tick() {
		super.tick();
		if(miningEggCD != -1)
			miningEggCD--;
		if(dataTracker.get(PICKAXE) != shouldShowPickaxe())
			dataTracker.set(PICKAXE, shouldShowPickaxe());
	}

	@Override
	public void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new MeleeAttackGoal(this,1.2D,false));
		this.goalSelector.add(1, new ThrowMiningEggGoal(this));
		this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.6D));
		this.goalSelector.add(3, new RaiderEntity.PatrolApproachGoal(this, 10.0F));
		this.targetSelector.add(0, new FollowTargetGoal(this, IronGolemEntity.class, true));
		this.targetSelector.add(1, new FollowTargetGoal(this, PlayerEntity.class, true));
		this.targetSelector.add(2, new FollowTargetGoal(this, AbstractTraderEntity.class, false));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public IllagerEntity.State getState() {
		return shouldShowPickaxe() ? State.ATTACKING : IllagerEntity.State.CROSSED;
	}

	public void throwMiningEgg(Vec3d target) {
		MiningEggEntity egg = new MiningEggEntity(RaidImMod.MINING_EGG_ENTITY_TYPE, this, world);
		Vec3d head = getPos().add(0, 1.6, 0);
		Vec3d tg = target.subtract(head).add(0, head.distanceTo(target) / 18.0, 0);
		tg = tg.multiply(2.0 / tg.length());
		egg.setVelocity(tg);
		world.spawnEntity(egg);
		this.swingHand(Hand.MAIN_HAND);
	}

	public boolean canThrow(){
		return miningEggCD == -1;
	}

	public void setMiningEggCD(int cd) {
		miningEggCD = cd;
	}

	public int getMiningEggCD() {
		return miningEggCD;
	}

	public boolean shouldShowPickaxe() {
		return world.isClient ? dataTracker.get(PICKAXE) : pickaxe | getTarget() != null;
	}

	public void setShowPickaxe(boolean pickaxe) {
		if(!this.pickaxe && pickaxe)
			this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(SLOW_DOWN_MODIFIER);
		else if(this.pickaxe && !pickaxe)
			this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(SLOW_DOWN_MODIFIER);
		this.pickaxe = pickaxe;
	}
}
