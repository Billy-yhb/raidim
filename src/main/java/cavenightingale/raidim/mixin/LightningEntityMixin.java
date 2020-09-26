package cavenightingale.raidim.mixin;

import cavenightingale.raidim.Lightning;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin extends Entity implements Lightning {
	public LightningEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	private boolean fire = true;

	@Override
	public void raidim_setFire(boolean fire) {
		this.fire = fire;
	}

	@Override
	public boolean raidim_getFire() {
		return fire;
	}

	@Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
	private void raidim_onSpawnFire(int unused, CallbackInfo info) {
		if(!fire)
			info.cancel();
	}
}
