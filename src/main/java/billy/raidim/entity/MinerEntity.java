package billy.raidim.entity;

import billy.raidim.item.DiggingEggItem;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.IWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class MinerEntity extends IllagerEntity {
	public MinerEntity(EntityType<? extends IllagerEntity> entityType_1, World world_1) {
		super(entityType_1, world_1);
	}
	@Override
	public void addBonusForWave(int var1, boolean var2) {
	}
	@Override
	public SoundEvent getCelebratingSound() {
		return SoundEvents.ENTITY_VINDICATOR_CELEBRATE;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(2, new RaiderEntity.PatrolApproachGoal(this, 10.0F));
		this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
		this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new FollowTargetGoal(this, AbstractTraderEntity.class, false));
	}
	@Override
	protected void initAttributes() {
		super.initAttributes();
		this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.3499999940395355D);
		this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
		this.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
	}
	@Override
	public EntityData initialize(IWorld iWorld_1, LocalDifficulty localDifficulty_1, SpawnType spawnType_1, EntityData entityData_1, CompoundTag compoundTag_1) {
		this.initEquipment(localDifficulty_1);
		this.updateEnchantments(localDifficulty_1);
		return super.initialize(iWorld_1, localDifficulty_1, spawnType_1, entityData_1, compoundTag_1);
	}
	@Override
	protected void initEquipment(LocalDifficulty localDifficulty_1) {
		ItemStack itemStack_1 = new ItemStack(DiggingEggItem.INSTANCE);
		this.setEquippedStack(EquipmentSlot.MAINHAND, itemStack_1);
	}
}
