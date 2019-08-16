package billy.raidim.entity;

import java.util.HashSet;
import java.util.function.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class ElectricianEntity extends SpellcastingIllagerEntity {

	public ElectricianEntity(EntityType<? extends SpellcastingIllagerEntity> entityType_1, World world_1) {
		super(entityType_1, world_1);
	}

	HashSet<LivingEntity> attacking=new HashSet<>();
	protected void initAttributes() {
		super.initAttributes();
		this.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(64d);
		this.getAttributeInstance(EntityAttributes.ARMOR).setBaseValue(6d);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initGoals() {
		Predicate<LivingEntity> s=(e)->!attacking.contains(e);
		super.initGoals();
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(2, new FleeEntityGoal(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
		this.goalSelector.add(3, new LightningGoal(this));
		this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
		this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.add(1, (new RevengeGoal(this, new Class[]{RaiderEntity.class})).setGroupRevenge());
		this.targetSelector.add(2, (new FollowTargetGoal(this, PlayerEntity.class,10, true,false,s)).setMaxTimeWithoutVisibility(300));
		this.targetSelector.add(3, (new FollowTargetGoal(this, AbstractTraderEntity.class,10, false,false,s)).setMaxTimeWithoutVisibility(300));
		this.targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class,10, false,false,s));
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
			for(LivingEntity e:attacking) {
				if(Math.random()<0.4&&e.isAlive()) {
					LightningEntity lightning=new LightningEntity(world,e.x,e.y,e.z,false);
					((ServerWorld)world).addLightning(lightning);
				}
			}
		}
		for(Object e:attacking.toArray()) {
			if(!((LivingEntity)e).isAlive())
				attacking.remove(e);
		}
		if(attacking.contains(getTarget())) {
			setTarget(null);
		}
	}
	public class LightningGoal extends CastSpellGoal{
		
		public boolean canStart() {
			return getTarget()!=null&&super.canStart()&&(!attacking.contains(getTarget()));
		}

		public LightningGoal() {
		}
		public LightningGoal(Entity e) {
		}
		@Override
		protected void castSpell() {
			LivingEntity tg=getTarget();
			attacking.add(tg);
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
