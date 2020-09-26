package cavenightingale.raidim.entity;

import cavenightingale.raidim.Lightning;
import cavenightingale.raidim.RaidImMod;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class ElectricianEntity extends SpellcastingIllagerEntity {

	public ElectricianEntity(EntityType<? extends SpellcastingIllagerEntity> entityType_1, World world_1) {
		super(entityType_1, world_1);
	}

	public ElectricianEntity(World world_1) {
		this(RaidImMod.ELECTRICIAN_ENTITY_TYPE, world_1);
	}

	private HashSet<UUID> victims = new HashSet<>();
	public static DefaultAttributeContainer.Builder createElectricianAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3499999940395355D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0)
				.add(EntityAttributes.GENERIC_ARMOR, 6.0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initGoals() {
		Predicate<LivingEntity> s = (e) -> !victims.contains(e.getUuid());
		super.initGoals();
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new FleeEntityGoal(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
		this.goalSelector.add(2, new LightningGoal());
		this.goalSelector.add(3, new LookAtTargetGoal());
		this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.6D));
		this.goalSelector.add(5, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.add(1, (new RevengeGoal(this, new Class[]{RaiderEntity.class})).setGroupRevenge());
		this.targetSelector.add(2, (new FollowTargetGoal(this, PlayerEntity.class, 10,  true, false, s)).setMaxTimeWithoutVisibility(300));
		this.targetSelector.add(3, (new FollowTargetGoal(this, AbstractTraderEntity.class,10,  true, false,s)).setMaxTimeWithoutVisibility(300));
		this.targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class, 10,  true, false, s));
	}

	@Override
	protected SoundEvent getCastSpellSound() {
		return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
	}

	@Override
	public void addBonusForWave(int var1, boolean var2) {
	}

	@Override
	public SoundEvent getCelebratingSound() {
		return SoundEvents.ENTITY_EVOKER_CELEBRATE;
	}

	@Override
	public void tick() {
		super.tick();
		if(this.isAlive()) {
			List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class,
					new Box(getPos(), getPos()).expand(200.0), p -> victims.contains(p.getUuid()));
			for (LivingEntity entity : entities) {
				if (Math.random() < 0.4 && entity.isAlive() && entity.isAttackable() &&
						!entity.isInvulnerableTo(DamageSource.LIGHTNING_BOLT)) {
					if (world instanceof ServerWorld) {
						LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
						lightningEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
						((Lightning)lightningEntity).raidim_setFire(false);
						world.spawnEntity(lightningEntity);
					}
				}else if(entity.getHealth() <= 0) {
					victims.remove(entity.getUuid());
				}
			}
		}
		if(getTarget() != null && victims.contains(getTarget().getUuid()))
			setTarget(null);
	}

	@Override
	public void readCustomDataFromTag(CompoundTag cpd) {
		ListTag victimList = cpd.getList("electricianVictims", NbtType.INT_ARRAY);
		try {
			victims.clear();
			for(Tag tag : victimList)
				victims.add(NbtHelper.toUuid(tag));
		}catch (IllegalArgumentException exception) {
			RaidImMod.LOGGER.warn("Cannot load victim list of the electrician " + getUuidAsString());
		}
	}

	@Override
	public void writeCustomDataToTag(CompoundTag cpd) {
		ListTag victimList = new ListTag();
		int i = 0;
		for(UUID uuid : victims)
			victimList.add(i++, NbtHelper.fromUuid(uuid));
		cpd.put("electricianVictims", victimList);
	}

	public class LightningGoal extends CastSpellGoal{

		public boolean canStart() {
			return getTarget() != null && super.canStart() && (!victims.contains(getTarget()));
		}

		@Override
		protected void castSpell() {
			LivingEntity tg = getTarget();
			victims.add(tg.getUuid());
		}

		@Override
		protected int getSpellTicks() {
			return 100;
		}

		@Override
		protected int startTimeDelay() {
			return 200;
		}

		@Override
		protected SoundEvent getSoundPrepare() {
			return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
		}

		@Override
		protected Spell getSpell() {
			return Spell.WOLOLO;
		}
	}
}