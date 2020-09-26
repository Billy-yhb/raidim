package cavenightingale.raidim.mixin;

import cavenightingale.raidim.Lightning;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	private void setFireTicks(int ticks) {};

	@Inject(method = "onStruckByLightning", at = @At("TAIL"))
	private void raidim_onOnStruckByLightning(ServerWorld world, LightningEntity entity, CallbackInfo info) {
		if(!((Lightning)entity).raidim_getFire())
			setFireTicks(-1);
	}
}
