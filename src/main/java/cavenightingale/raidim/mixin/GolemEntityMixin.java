package cavenightingale.raidim.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GolemEntity.class)
public abstract class GolemEntityMixin extends PathAwareEntity {
	private GolemEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void tick() {
		if(getStatusEffect(StatusEffects.BLINDNESS) != null && getTarget() != null)
			setTarget(null);
		super.tick();
	}

	@Override
	public boolean canTarget(LivingEntity target) {
		return super.canTarget(target) && getStatusEffect(StatusEffects.BLINDNESS) == null;
	}
}
