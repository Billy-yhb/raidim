package billy.raidim.entity;

import java.util.function.Predicate;

import billy.raidim.RaidImMod;
import billy.raidim.entity.goal.AttackGolemGoal;
import billy.raidim.entity.goal.ThrowDiggingEggGoal;
import billy.raidim.reflect.FieldHelper;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Vec3d;
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
	private static final FieldHelper<Double> speed_fld=new FieldHelper<>(MeleeAttackGoal.class,
			"field_6500","speed");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new AttackGolemGoal(this));
		this.goalSelector.add(2, new ThrowDiggingEggGoal(this));
		this.goalSelector.add(3, new MeleeAttackGoal(this,1.2D,false) {
			protected double getSquaredMaxAttackDistance(
					LivingEntity livingEntity_1) {
				if (this.mob.getVehicle() instanceof RavagerEntity) {
					float float_1 = this.mob.getVehicle().getWidth() - 0.1F;
					return (double)(float_1 * 2.0F * float_1 * 2.0F + livingEntity_1.getWidth());
				} else {
					return super.getSquaredMaxAttackDistance(livingEntity_1);
				}
			}
			public void tick() {
				if(this.mob.getTarget() instanceof IronGolemEntity) {
					if(this.mob.getTarget().distanceTo(this.mob)<18)
						speed_fld.set(this, 0.3);
					else
						speed_fld.set(this, 0.8);
				}else {
					speed_fld.set(this, 1.2);
				}
				super.tick();
			}
		});
		this.goalSelector.add(4, new RaiderEntity.PatrolApproachGoal(this, 10.0F));
		this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
		this.targetSelector.add(0, new FollowTargetGoal(this, PlayerEntity.class, true));
		this.targetSelector.add(1, new FollowTargetGoal(this, AbstractTraderEntity.class, 10, false,
				true, new Predicate<LivingEntity>() {
					@Override
					public boolean test(LivingEntity t) {
						return MinerEntity.this.acceptVillager();
					}
		}));
	}
	public boolean acceptVillager() {
		if(!world.getGameRules().getBoolean("mobGriefing")) {
			return true;
		}
		if(this.hasActiveRaid()) {
			Raid raid=this.getRaid();
			int r=world.getGameRules().getInteger("raidRadius");
			float miner_health=0;
			float raider_health=0;
			for(RaiderEntity e:world.getEntities(RaiderEntity.class,
					new BoundingBox(x-r*2,x+r*2,y-r*2,y+r*2,z-r*2,z+r*2))) {
				if(e.getRaid()==raid) {
					raider_health+=e.getHealth();
					if(e instanceof MinerEntity) {
						miner_health+=e.getHealth();
					}
				}
			}
			if(raider_health>2*miner_health) {
				return false;
			}else {
				return true;
			}
		}else {
			return true;
		}
	}
	@Override
	protected void initAttributes() {
		super.initAttributes();
		this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.3499999940395355D);
		this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(36.0D);
		this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
		this.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(56.0D);
	}
	@Override
	public EntityData initialize(IWorld iWorld_1, LocalDifficulty localDifficulty_1, SpawnType spawnType_1, EntityData entityData_1, CompoundTag compoundTag_1) {
		this.initEquipment(localDifficulty_1);
		this.updateEnchantments(localDifficulty_1);
		return super.initialize(iWorld_1, localDifficulty_1, spawnType_1, entityData_1, compoundTag_1);
	}
	@Override
	protected void initEquipment(LocalDifficulty localDifficulty_1) {
		ItemStack itemStack_1 = new ItemStack(Items.DIAMOND_PICKAXE);
		this.setEquippedStack(EquipmentSlot.MAINHAND, itemStack_1);
	}
	public boolean shouldShowPickaxe() {
		return isAttacking()||isThrowingEgg();
	}
	public boolean isThrowingEgg() {
		return preThrowEgg>0;
	}
	public State getState() {
		if(shouldShowPickaxe()) {
			return State.ATTACKING;
		}else if(isCelebrating()) {
			return State.CELEBRATING;
		}else {
			return State.CROSSED;
		}
	}
	public int diggingeggcooldown=0;
	public int preThrowEgg=0;
	public BlockPos targetpos=null;
	public void tick(){
		super.tick();
		if(diggingeggcooldown>0) {
			diggingeggcooldown--;
		}
		if(preThrowEgg>0) {
			preThrowEgg--;
			if(preThrowEgg==0) {
				throwDiggingegg(targetpos);
			}
		}
	}
	public void preThrowDiggingegg(BlockPos targetPos) {
		preThrowEgg=16;
		targetpos=targetPos;
	}
	public void throwDiggingegg(BlockPos targetPos) {
		diggingeggcooldown=120;
		DiggingEggEntity egg=new DiggingEggEntity(RaidImMod.DIGGINGEGG,
				this,world);
		Vec3d tg=new Vec3d(targetPos).subtract(x,y,z).add(0,1,0);
		tg=tg.add(0,tg.distanceTo(new Vec3d(0,0,0))/18,0);
		egg.setVelocity(tg.x,tg.y,tg.z,2.0f,1.2f);
		world.spawnEntity(egg);
	}
}
