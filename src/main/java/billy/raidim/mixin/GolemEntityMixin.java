package billy.raidim.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.world.World;

@Mixin(GolemEntity.class)
public abstract class GolemEntityMixin extends MobEntityWithAi {
	public GolemEntityMixin(EntityType<? extends MobEntityWithAi> entityType_1, World world_1) {
		super(entityType_1, world_1);
	}
	@Override
	public void tick() {
		if(this.getStatusEffect(StatusEffects.BLINDNESS)!=null) {
			if(this.getTarget()!=null) {
				this.setTarget(null);
			}
		}
		super.tick();
	}
}
